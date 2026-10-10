package com.roaa.portfolio.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roaa.portfolio.theme.Flat
import com.roaa.portfolio.theme.body
import com.roaa.portfolio.theme.display
import com.roaa.portfolio.theme.label

/** Hover state for desktop pointers; always false on touch. */
@Composable
fun rememberHoverSource(): Pair<MutableInteractionSource, State<Boolean>> {
    val source = remember { MutableInteractionSource() }
    return source to source.collectIsHoveredAsState()
}

/** Makes the element open [url] in a new tab, with a hand cursor. */
fun Modifier.link(url: String): Modifier = composed {
    val uriHandler = LocalUriHandler.current
    handClick { uriHandler.openUri(url) }
}

/** Click with a hand cursor and no ripple/hover overlay — components animate their own hover state. */
fun Modifier.handClick(onClick: () -> Unit): Modifier =
    pointerHoverIcon(PointerIcon.Hand).clickable(interactionSource = null, indication = null, onClick = onClick)

/**
 * Fades and slides content up the first time it scrolls into the viewport.
 */
@Composable
fun Reveal(
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    distance: Dp = 48.dp,
    content: @Composable () -> Unit,
) {
    var shown by remember { mutableStateOf(false) }
    val windowHeight = LocalWindowInfo.current.containerSize.height
    val distancePx = with(LocalDensity.current) { distance.toPx() }
    val progress by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(durationMillis = 800, delayMillis = delayMillis, easing = FastOutSlowInEasing),
    )
    Box(
        modifier
            .onGloballyPositioned { coords ->
                if (!shown && windowHeight > 0 && coords.positionInWindow().y < windowHeight * 0.92f) shown = true
            }
            .graphicsLayer {
                alpha = progress
                translationY = (1f - progress) * distancePx
            },
    ) { content() }
}

enum class ButtonStyle { Solid, Outline }

@Composable
fun FlatButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Flat.Ink,
    contentColor: Color = Flat.White,
    style: ButtonStyle = ButtonStyle.Solid,
    showArrow: Boolean = true,
) {
    val (source, hovered) = rememberHoverSource()
    val isHovered by hovered
    val bg by animateColorAsState(
        when {
            style == ButtonStyle.Solid && isHovered -> Flat.Coral
            style == ButtonStyle.Solid -> color
            isHovered -> color
            else -> color.copy(alpha = 0f)
        },
    )
    val fg by animateColorAsState(
        when {
            style == ButtonStyle.Solid -> contentColor
            isHovered -> contentColor
            else -> color
        },
    )
    val arrowShift by animateFloatAsState(if (isHovered) 1f else 0f)
    Row(
        modifier
            .hoverable(source)
            .handClick(onClick)
            .background(bg, RoundedCornerShape(50))
            .then(if (style == ButtonStyle.Outline) Modifier.border(2.dp, color, RoundedCornerShape(50)) else Modifier)
            .padding(horizontal = 26.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text, style = body(15.sp, fg, FontWeight.SemiBold))
        if (showArrow) {
            Spacer(Modifier.width(10.dp))
            ArrowIcon(fg, Modifier.size(16.dp).graphicsLayer { translationX = arrowShift * 5.dp.toPx() })
        }
    }
}

/** A plain right arrow, drawn so it never depends on font glyph coverage. */
@Composable
fun ArrowIcon(color: Color, modifier: Modifier = Modifier, diagonal: Boolean = false) {
    Canvas(modifier.graphicsLayer { rotationZ = if (diagonal) -45f else 0f }) {
        val w = size.width
        val h = size.height
        val stroke = w * 0.13f
        drawLine(color, Offset(w * 0.08f, h / 2), Offset(w * 0.9f, h / 2), stroke, StrokeCap.Round)
        val head = Path().apply {
            moveTo(w * 0.55f, h * 0.18f)
            lineTo(w * 0.9f, h / 2)
            lineTo(w * 0.55f, h * 0.82f)
        }
        drawPath(head, color, style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun Tag(text: String, background: Color, color: Color = Flat.Ink, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier.background(background, RoundedCornerShape(50)).padding(horizontal = 12.dp, vertical = 6.dp),
        style = body(13.sp, color, FontWeight.Medium),
    )
}

@Composable
fun SectionHeader(
    index: String,
    eyebrow: String,
    title: String,
    subtitle: String?,
    compact: Boolean,
    accent: Color = Flat.Blue,
    dark: Boolean = false,
) {
    Reveal {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(10.dp).background(accent, RoundedCornerShape(50)))
                Spacer(Modifier.width(10.dp))
                Text(
                    "$index / ${eyebrow.uppercase()}",
                    style = label(13.sp, if (dark) Flat.Sky else Flat.InkSoft),
                )
            }
            Text(title, style = display(if (compact) 38.sp else 60.sp, if (dark) Flat.White else Flat.Ink))
            if (subtitle != null) {
                Text(
                    subtitle,
                    modifier = Modifier.padding(top = 2.dp),
                    style = body(if (compact) 16.sp else 18.sp, if (dark) Flat.Sky else Flat.InkSoft),
                )
            }
        }
    }
}
