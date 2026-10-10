package com.roaa.portfolio.sections

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate

private object Clay {
    val SkinLight = Color(0xFFE6A97E)
    val Skin = Color(0xFFC4855B)
    val SkinShade = Color(0xFF8F5A3A)
    val Hair = Color(0xFF17110F)
    val BeardLight = Color(0xFF3E302B)
    val Beard = Color(0xFF1B1412)
    val Iris = Color(0xFF2B1B14)
    val Lip = Color(0xFF8E4E40)
    val LipLight = Color(0xFFA9634F)

    val CapLight = Color(0xFF41424A)
    val Cap = Color(0xFF131317)
    val Brim = Color(0xFF1C1C21)

    val Gold = Color(0xFFC9A55A)
    val GoldLight = Color(0xFFF0D893)

    val JacketLight = Color(0xFF52555E)
    val Jacket = Color(0xFF1F2025)
    val TeeLight = Color(0xFFFFFFFF)
    val Tee = Color(0xFFD7D7D3)
}

/**
 * A clay-style "3D" caricature drawn entirely in Compose, modelled on the profile
 * photo: black cap, thin gold glasses, full beard, gold stud, white tee and a
 * black zip jacket. Soft radial lighting gives every shape volume; it blinks,
 * breathes, and turns toward the pointer ([look] is -1..1 on each axis).
 */
@Composable
fun Avatar3D(look: Offset, modifier: Modifier = Modifier) {
    val t = rememberInfiniteTransition()
    val blink by t.animateFloat(
        1f, 1f,
        infiniteRepeatable(
            keyframes {
                durationMillis = 4200
                1f at 0; 1f at 3800; 0.1f at 3900; 1f at 4050; 0.1f at 4120; 1f at 4200
            },
        ),
    )
    val breathe by t.animateFloat(0f, 1f, infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), RepeatMode.Reverse))

    Canvas(modifier) {
        // Work in a 100×100 unit space.
        val u = size.minDimension / 100f
        scale(u, u, pivot = Offset.Zero) {
            drawBody(breathe)
            val lx = look.x.coerceIn(-1f, 1f)
            val ly = look.y.coerceIn(-1f, 1f)
            rotate(lx * 4f, pivot = Offset(50f, 72f)) {
                translate(lx * 1.2f, ly * 0.8f - breathe * 0.6f) {
                    drawHead(lx, ly, blink)
                }
            }
        }
    }
}

private fun radial(center: Offset, radius: Float, inner: Color, outer: Color, edge: Color? = null) =
    Brush.radialGradient(listOfNotNull(inner, outer, edge), center, radius)

private fun mirrored(build: Path.(sign: Float) -> Unit): List<Path> =
    listOf(-1f, 1f).map { sign -> Path().apply { build(sign) } }

private fun DrawScope.drawBody(breathe: Float) {
    val lift = breathe * 0.8f

    // Jacket
    drawRoundRect(
        radial(Offset(34f, 82f), 62f, Clay.JacketLight, Clay.Jacket),
        topLeft = Offset(8f, 76f - lift),
        size = Size(84f, 42f),
        cornerRadius = CornerRadius(26f),
    )
    // White tee showing through the open jacket
    drawRect(
        radial(Offset(48f, 80f), 26f, Clay.TeeLight, Clay.Tee),
        topLeft = Offset(40f, 74f - lift),
        size = Size(20f, 30f),
    )
    // Neck
    drawRoundRect(
        radial(Offset(47f, 64f), 18f, Clay.Skin, Clay.SkinShade),
        topLeft = Offset(42f, 61f),
        size = Size(16f, 17f - lift),
        cornerRadius = CornerRadius(6f),
    )
    // Shadow the head casts on the neck
    drawOval(Color(0x40000000), topLeft = Offset(42f, 61f), size = Size(16f, 6f))
    // Crew-neck rib
    drawArc(
        Clay.Tee, 0f, 180f, false,
        topLeft = Offset(41.5f, 70f - lift), size = Size(17f, 8f),
        style = Stroke(1.8f),
    )
    // Stand-up jacket collar + zip edges
    mirrored { s ->
        moveTo(50f + s * 9f, 69f - lift)
        lineTo(50f + s * 14.5f, 67.5f - lift)
        lineTo(50f + s * 15f, 79f - lift)
        lineTo(50f + s * 10.5f, 84f - lift)
        close()
    }.forEach { drawPath(it, radial(Offset(46f, 70f), 18f, Clay.JacketLight, Clay.Jacket)) }
    listOf(-1f, 1f).forEach { s ->
        drawLine(Color(0x66FFFFFF), Offset(50f + s * 10.2f, 83f - lift), Offset(50f + s * 8.5f, 100f), 0.6f)
    }
}

private fun DrawScope.drawHead(lx: Float, ly: Float, blink: Float) {
    // Facial features shift further than the skull to fake a head turn.
    val fx = lx * 2.2f
    val fy = ly * 1.4f

    // Ears + gold stud (on the viewer's right, as in the photo)
    listOf(30f, 70f).forEach { x ->
        drawCircle(radial(Offset(x - 1f, 47f), 6f, Clay.Skin, Clay.SkinShade), 5f, Offset(x, 48f))
    }
    drawCircle(radial(Offset(70.6f, 52.6f), 1.6f, Clay.GoldLight, Clay.Gold), 1.3f, Offset(71f, 53f))

    // Skull
    drawOval(
        radial(Offset(42f + fx, 40f + fy), 36f, Clay.SkinLight, Clay.Skin, Clay.SkinShade),
        topLeft = Offset(30f, 19f),
        size = Size(40f, 50f),
    )
    // Shade under the brim
    drawOval(Color(0x30000000), topLeft = Offset(31f, 33f + fy * 0.3f), size = Size(38f, 5f))

    // Short sideburns running from the cap into the beard
    mirrored { s ->
        moveTo(50f + s * 19.8f, 32f)
        cubicTo(50f + s * 20f, 41f, 50f + s * 19.6f, 46f, 50f + s * 18.6f, 50f)
        lineTo(50f + s * 16f, 50f)
        cubicTo(50f + s * 16.8f, 45f, 50f + s * 16.8f, 38f, 50f + s * 16f, 32f)
        close()
    }.forEach { drawPath(it, Clay.Hair) }

    // Full beard with a slightly pointed chin
    val beard = Path().apply {
        moveTo(30.8f, 49f)
        cubicTo(30.5f, 64f, 40f, 76f, 50f + fx * 0.4f, 77f)
        cubicTo(60f, 76f, 69.5f, 64f, 69.2f, 49f)
        lineTo(67f + fx * 0.3f, 52.5f)
        cubicTo(65.5f + fx, 57f, 60f + fx, 58f + fy, 50f + fx, 58f + fy)
        cubicTo(40f + fx, 58f + fy, 34.5f + fx, 57f, 33f + fx * 0.3f, 52.5f)
        close()
    }
    drawPath(beard, radial(Offset(44f + fx, 62f), 22f, Clay.BeardLight, Clay.Beard))

    // Closed-lip half smile
    drawOval(Clay.LipLight, topLeft = Offset(46.3f + fx, 60.2f + fy), size = Size(7.4f, 2.4f))
    val upperLip = Path().apply {
        moveTo(45.5f + fx, 60.4f + fy)
        cubicTo(47.5f + fx, 59.4f + fy, 52.5f + fx, 59.4f + fy, 54.5f + fx, 60.4f + fy)
        cubicTo(52.5f + fx, 61.4f + fy, 47.5f + fx, 61.4f + fy, 45.5f + fx, 60.4f + fy)
        close()
    }
    drawPath(upperLip, Clay.Lip)

    // Moustache flowing into the beard
    val stache = Path().apply {
        moveTo(42.5f + fx, 61f + fy)
        cubicTo(43.5f + fx, 56f + fy, 48f + fx, 55.6f + fy, 50f + fx, 57f + fy)
        cubicTo(52f + fx, 55.6f + fy, 56.5f + fx, 56f + fy, 57.5f + fx, 61f + fy)
        cubicTo(55.5f + fx, 58.6f + fy, 52.5f + fx, 59f + fy, 50f + fx, 59.3f + fy)
        cubicTo(47.5f + fx, 59f + fy, 44.5f + fx, 58.6f + fy, 42.5f + fx, 61f + fy)
        close()
    }
    drawPath(stache, Clay.Beard)

    // Nose
    drawOval(
        radial(Offset(48.8f + fx, 49.5f + fy), 5.5f, Clay.SkinLight, Clay.Skin),
        topLeft = Offset(46f + fx, 46f + fy),
        size = Size(8f, 8.5f),
    )
    drawArc(
        Clay.SkinShade, 20f, 140f, false,
        topLeft = Offset(46f + fx, 49.5f + fy), size = Size(8f, 4.5f),
        style = Stroke(0.8f, cap = StrokeCap.Round),
    )
    drawCircle(Color(0x55FFFFFF), 0.9f, Offset(49f + fx, 50f + fy))

    // Calm, slightly heavy-lidded eyes (with blink)
    listOf(41.5f, 58.5f).forEach { x ->
        val ex = x + fx
        val ey = 45.5f + fy
        drawOval(Color(0xFFF4EEE8), topLeft = Offset(ex - 2.6f, ey - 1.7f * blink), size = Size(5.2f, 3.4f * blink))
        if (blink > 0.4f) {
            drawCircle(Clay.Iris, 1.75f * blink, Offset(ex + lx * 0.6f, ey + ly * 0.3f))
            drawCircle(Color.White, 0.55f, Offset(ex + lx * 0.6f + 0.6f, ey + ly * 0.3f - 0.6f))
        }
        // Upper lid
        drawArc(
            Clay.SkinShade, 195f, 150f, false,
            topLeft = Offset(ex - 3f, ey - 2.4f), size = Size(6f, 3.6f),
            style = Stroke(0.7f, cap = StrokeCap.Round),
        )
    }

    // Thick, straight brows
    listOf(Triple(35.5f, 46.5f, -1f), Triple(53.5f, 64.5f, 1f)).forEach { (a, b, side) ->
        drawLine(
            Clay.Hair,
            Offset(a + fx, 40.6f + fy + if (side < 0) 0.5f else -0.2f),
            Offset(b + fx, 40.6f + fy + if (side < 0) -0.2f else 0.5f),
            2.3f, StrokeCap.Round,
        )
    }

    // Thin gold square-aviator glasses with a double bridge
    val frame = Stroke(0.75f)
    listOf(33.4f, 52.1f).forEach { x ->
        drawRoundRect(Color(0x1AFFF2D0), Offset(x + fx, 40.3f + fy), Size(14.5f, 11.5f), CornerRadius(3.6f))
        drawRoundRect(Clay.Gold, Offset(x + fx, 40.3f + fy), Size(14.5f, 11.5f), CornerRadius(3.6f), style = frame)
        drawLine(Color(0x88FFFFFF), Offset(x + 3f + fx, 49.5f + fy), Offset(x + 7f + fx, 42.3f + fy), 0.6f, StrokeCap.Round)
    }
    drawLine(Clay.GoldLight, Offset(47.9f + fx, 41.2f + fy), Offset(52.1f + fx, 41.2f + fy), 0.7f) // top bar
    drawArc(
        Clay.Gold, 200f, 140f, false,
        topLeft = Offset(47.6f + fx, 44f + fy), size = Size(4.8f, 3f),
        style = Stroke(0.7f),
    )
    drawLine(Clay.Gold, Offset(33.4f + fx, 42f + fy), Offset(30f, 43.5f), 0.75f)
    drawLine(Clay.Gold, Offset(66.6f + fx, 42f + fy), Offset(70f, 43.5f), 0.75f)

    // Cap sits a little higher on the head so the brows and some forehead show.
    translate(0f, -3.5f) { drawCap(fx * 0.5f) }
}

private fun DrawScope.drawCap(bx: Float) {
    // Crown
    val crown = Path().apply {
        moveTo(28f, 38f)
        cubicTo(24.5f, 16f, 38f, 8.5f, 50f + bx * 0.5f, 8.5f)
        cubicTo(62f, 8.5f, 75.5f, 16f, 72f, 38f)
        cubicTo(66f, 34.2f, 58f, 33.2f, 50f + bx, 33.2f)
        cubicTo(42f, 33.2f, 34f, 34.2f, 28f, 38f)
        close()
    }
    drawPath(crown, radial(Offset(42f, 16f), 34f, Clay.CapLight, Clay.Cap))
    // Panel seams + top button
    listOf(-1f, 1f).forEach { s ->
        drawLine(Color(0x12FFFFFF), Offset(50f + bx * 0.5f, 10f), Offset(50f + bx + s * 9f, 33.6f), 0.4f)
    }
    drawCircle(Clay.Cap, 1.7f, Offset(50f + bx * 0.5f, 9.2f))
    // Sheen
    drawArc(
        Color(0x33FFFFFF), 205f, 60f, false,
        topLeft = Offset(34f, 12f), size = Size(28f, 20f),
        style = Stroke(1.4f, cap = StrokeCap.Round),
    )

    // Curved brim, pointing at the viewer
    val brim = Path().apply {
        moveTo(27.5f, 37.6f)
        cubicTo(36f, 32.6f, 64f, 32.6f, 72.5f, 37.6f)
        cubicTo(70f, 41.6f, 60f + bx, 40.2f, 50f + bx, 40.4f)
        cubicTo(40f + bx, 40.2f, 30f, 41.6f, 27.5f, 37.6f)
        close()
    }
    drawPath(brim, radial(Offset(46f + bx, 35f), 26f, Color(0xFF2E2E35), Clay.Brim))
    drawArc(
        Color(0x30FFFFFF), 200f, 140f, false,
        topLeft = Offset(30f, 34f), size = Size(40f, 6f),
        style = Stroke(0.6f, cap = StrokeCap.Round),
    )
}
