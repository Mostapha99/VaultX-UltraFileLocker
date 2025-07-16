package com.vaultx.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedCyberpunkBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")
    
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "animation_progress"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawAnimatedCircuits(
                drawScope = this,
                progress = animationProgress,
                primaryColor = MaterialTheme.colorScheme.primary,
                secondaryColor = MaterialTheme.colorScheme.secondary
            )
        }
        
        content()
    }
}

private fun drawAnimatedCircuits(
    drawScope: DrawScope,
    progress: Float,
    primaryColor: Color,
    secondaryColor: Color
) {
    val width = drawScope.size.width
    val height = drawScope.size.height
    
    // Draw animated circuit lines
    for (i in 0..5) {
        val startX = (width * 0.1f) + (i * width * 0.15f)
        val animatedY = height * 0.2f + (sin(progress * 2 * Math.PI + i) * height * 0.1f).toFloat()
        
        drawScope.drawLine(
            color = primaryColor.copy(alpha = 0.3f),
            start = Offset(startX, animatedY),
            end = Offset(startX + width * 0.1f, animatedY + height * 0.1f),
            strokeWidth = 2f
        )
    }
    
    // Draw animated dots
    for (i in 0..8) {
        val angle = progress * 2 * Math.PI + (i * Math.PI / 4)
        val radius = width * 0.3f
        val centerX = width / 2
        val centerY = height / 2
        
        val x = centerX + (cos(angle) * radius).toFloat()
        val y = centerY + (sin(angle) * radius).toFloat()
        
        drawScope.drawCircle(
            color = secondaryColor.copy(alpha = 0.4f),
            radius = 4f,
            center = Offset(x, y)
        )
    }
    
    // Draw grid pattern
    val gridSpacing = 50f
    for (x in 0..(width / gridSpacing).toInt()) {
        drawScope.drawLine(
            color = primaryColor.copy(alpha = 0.1f),
            start = Offset(x * gridSpacing, 0f),
            end = Offset(x * gridSpacing, height),
            strokeWidth = 1f
        )
    }
    
    for (y in 0..(height / gridSpacing).toInt()) {
        drawScope.drawLine(
            color = primaryColor.copy(alpha = 0.1f),
            start = Offset(0f, y * gridSpacing),
            end = Offset(width, y * gridSpacing),
            strokeWidth = 1f
        )
    }
}

@Composable
fun GlassmorphismBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                    )
                )
            )
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // Draw subtle geometric shapes
            val width = size.width
            val height = size.height
            
            // Large circles with low opacity
            drawCircle(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                radius = width * 0.4f,
                center = Offset(width * 0.2f, height * 0.3f)
            )
            
            drawCircle(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                radius = width * 0.3f,
                center = Offset(width * 0.8f, height * 0.7f)
            )
        }
        
        content()
    }
}

