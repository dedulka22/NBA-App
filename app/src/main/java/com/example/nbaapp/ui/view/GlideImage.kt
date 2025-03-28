package com.example.nbaapp.ui.view

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/**
 * Composable for loading an image using Glide
 * @param playerImageUrl Image URL
 */
@SuppressLint("RememberReturnType")
@Composable
fun GlideImage(playerImageUrl: String) {
    val context = LocalContext.current

    // State to hold the loaded image bitmap
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }

    // Use LaunchedEffect to start Glide loading once the composable is composed
    LaunchedEffect(playerImageUrl) {
        Glide.with(context)
            .asDrawable()
            .load(playerImageUrl)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    // Optionally handle clearing
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    // Convert to ImageBitmap and store in the state
                    imageBitmap.value = resource.toBitmap().asImageBitmap()
                }
            })
    }

    imageBitmap.value?.let {
        Image(
            bitmap = it,
            contentDescription = "Player Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shadow(8.dp, shape = RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    } ?: BasketballCircularProgressIndicator()
}
