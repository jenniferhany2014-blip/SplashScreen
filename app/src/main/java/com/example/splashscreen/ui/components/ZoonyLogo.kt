package com.example.splashscreen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.splashscreen.R

/** Circular Zoony Store brand logo, used on auth screens (Login, Sign-Up). */
@Composable
fun ZoonyLogo(size: Dp = 120.dp, contentDescription: String) {
    Image(
        painter = painterResource(id = R.drawable.my_logo1),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
    )
}