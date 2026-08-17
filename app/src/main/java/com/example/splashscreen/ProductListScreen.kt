package com.example.splashscreen
import com.example.splashscreen.error.ErrorMapper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.splashscreen.viewmodel.ProductListUiState
import com.example.splashscreen.viewmodel.ProductListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    showTopBar: Boolean = true,
    initialQuery: String = "",
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) {
            viewModel.search(initialQuery)
        } else {
            viewModel.fetchProducts()
        }
    }

    val content: @Composable (PaddingValues) -> Unit = { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                ProductListUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is ProductListUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.products_error))

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = ErrorMapper.userMessage(state.error),
                            fontSize = 13.sp
                        )
                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (initialQuery.isNotBlank()) {
                                    viewModel.search(initialQuery)
                                } else {
                                    viewModel.fetchProducts()
                                }
                            }
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }

                is ProductListUiState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {

                        if (state.fromCache && initialQuery.isBlank()) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = stringResource(R.string.products_cached_notice),
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    ),
                                    fontSize = 13.sp
                                )
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.products,
                                key = { it.id }
                            ) { product ->

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        navController.navigate(
                                            "product_detail/${product.id}"
                                        )
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = product.thumbnail,
                                            contentDescription = product.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(72.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                        )

                                        Spacer(Modifier.width(12.dp))

                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = product.title,
                                                fontSize = 16.sp
                                            )

                                            Spacer(Modifier.height(4.dp))

                                            Text(
                                                text = product.category,
                                                fontSize = 13.sp
                                            )

                                            Spacer(Modifier.height(4.dp))

                                            Text(
                                                text = "$${product.price}",
                                                fontSize = 13.sp
                                            )
                                        }

                                        Text(
                                            text = "★ ${product.rating}",
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showTopBar) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource(R.string.products_title))
                    }
                )
            }
        ) { padding ->
            content(padding)
        }
    } else {
        content(PaddingValues(0.dp))
    }
}
