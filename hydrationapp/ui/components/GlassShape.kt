package com.example.hydrationapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class GlassShape(private val fillFraction: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val startWidth = 0.2f
        val endWidth = 1 - startWidth
        val path = Path().apply {

            val y = (1f - fillFraction) * size.height

            val x1 = size.width * startWidth * (1 - fillFraction) // / size.height
            val x2 = size.width - x1

            moveTo(x1, y)
            lineTo(x2, y)
            lineTo(size.width * endWidth, size.height)
            lineTo(size.width * startWidth, size.height)
            close()
        }
        return Outline.Generic(path)
    }

}

@Composable
@Preview
private fun PreviewGlassShape() {
    Box(
        modifier = Modifier
            .size(200.dp, 200.dp)
            .fillMaxWidth()
            .background(Color.Cyan, shape = GlassShape(1f))
            .border(2.dp, Color.Red, shape = GlassShape(1f))
    )

}