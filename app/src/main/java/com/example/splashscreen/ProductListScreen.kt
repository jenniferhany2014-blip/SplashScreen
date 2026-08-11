package com.example.splashscreen

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
import androidx.lifecycle.viewmodel.compose.viewModel
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
    viewModel: ProductListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) viewModel.search(initialQuery) else viewModel.fetchProducts()
    }

    val content: @Composable (PaddingValues) -> Unit = { padding ->
        Box(modifier = modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                ProductListUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is ProductListUiState.Error -> {
                    Column(
                        Modifier.align(Alignment.Center).padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.products_error))
                        Spacer(Modifier.height(8.dp))
                        Text(state.message, fontSize = 13.sp)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchProducts() }) { Text(stringResource(R.string.retry)) }
                    }
                }
                is ProductListUiState.Success -> {
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.products, key = { it.id }) { product ->
                            Card(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate("detail/${product.id}") }) {
                                Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = product.thumbnail,
                                        contentDescription = product.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(product.title, fontSize = 16.sp)
                                        Spacer(Modifier.height(4.dp))
                                        Text(product.category, fontSize = 13.sp)
                                        Spacer(Modifier.height(4.dp))
                                        Text("$${product.price}", fontSize = 13.sp)
                                    }
                                    Text("★ ${product.rating}", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showTopBar) {
        Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.products_title)) }) }) { padding -> content(padding) }
    } else {
        content(PaddingValues(0.dp))
    }
}