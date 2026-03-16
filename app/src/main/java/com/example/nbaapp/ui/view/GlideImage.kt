package com.example.nbaapp.ui.view

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/**
 * Composable for loading an image using Glide
 * @param imageUrl Image URL
 * @param imageDescription Accessibility description for the image
 * @param height Height of the image
 */
@Composable
fun GlideImage(
    imageUrl: String,
    imageDescription: String = "Image",
    height: Dp = 200.dp
) {
    val context = LocalContext.current.applicationContext
    val drawable = remember { mutableStateOf<Drawable?>(null) }

    val target = remember(imageUrl) {
        object : CustomTarget<Drawable>() {
            override fun onLoadCleared(placeholder: Drawable?) {
                drawable.value = null
            }

            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                drawable.value = resource
            }
        }
    }

    DisposableEffect(imageUrl) {
        val glideRequest = Glide.with(context)
            .asDrawable()
            .load(imageUrl)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        glideRequest.into(target)

        onDispose {
            Glide.with(context).clear(target)
        }
    }

    val currentDrawable = drawable.value
    if (currentDrawable != null) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .shadow(8.dp, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .semantics { contentDescription = imageDescription }
        ) {
            drawIntoCanvas { canvas ->
                currentDrawable.setBounds(0, 0, size.width.toInt(), size.height.toInt())
                currentDrawable.draw(canvas.nativeCanvas)
            }
        }
    } else {
        BasketballCircularProgressIndicator()
    }
}
