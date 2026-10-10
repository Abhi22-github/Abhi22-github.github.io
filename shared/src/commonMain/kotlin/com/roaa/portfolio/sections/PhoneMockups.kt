package com.roaa.portfolio.sections

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roaa.portfolio.data.MockKind
import com.roaa.portfolio.data.PlayApp
import com.roaa.portfolio.theme.Flat
import com.roaa.portfolio.theme.body
import com.roaa.portfolio.theme.display
import kotlin.math.PI
import kotlin.math.sin

private val ScreenBg = Color(0xFFFBFAF6)
private val LineGrey = Color(0xFFE4E1D8)

/** A flat phone frame with a live, looping illustration of the app inside. */
@Composable
fun PhoneMockup(app: PlayApp, modifier: Modifier = Modifier) {
    val float = rememberInfiniteTransition()
    val bob by float.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    )
    Box(
        modifier
            .width(250.dp)
            .aspectRatio(0.5f)
            .graphicsLayer { translationY = bob * 8.dp.toPx() }
            .background(Flat.Ink, RoundedCornerShape(38.dp))
            .padding(9.dp),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(30.dp))
                .background(ScreenBg),
        ) {
            when (app.mock) {
                MockKind.VideoPlayer -> VideoPlayerScreen(app)
                MockKind.PasswordVault -> PasswordVaultScreen(app)
                MockKind.ExpenseChart -> ExpenseScreen(app)
            }
            // Camera pill
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .size(width = 64.dp, height = 18.dp)
                    .background(Flat.Ink, RoundedCornerShape(50)),
            )
        }
    }
}

@Composable
private fun ScreenTitle(title: String, accent: Color) {
    Row(
        Modifier.fillMaxWidth().padding(top = 36.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = display(18.sp), modifier = Modifier.weight(1f))
        Box(Modifier.size(26.dp).background(accent, CircleShape))
    }
}

@Composable
private fun SkeletonLine(widthFraction: Float, color: Color = LineGrey, height: Int = 7) {
    Box(Modifier.fillMaxWidth(widthFraction).height(height.dp).background(color, RoundedCornerShape(50)))
}

// ---------------------------------------------------------------- PlayBox

@Composable
private fun VideoPlayerScreen(app: PlayApp) {
    val t = rememberInfiniteTransition()
    val progress by t.animateFloat(0f, 1f, infiniteRepeatable(tween(7000, easing = LinearEasing)))
    val pulse by t.animateFloat(
        1f, 1.18f,
        infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    )
    Column(Modifier.fillMaxSize()) {
        ScreenTitle("PlayBox", app.accent)
        // Player
        Box(
            Modifier
                .padding(horizontal = 14.dp)
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clip(RoundedCornerShape(16.dp))
                .background(app.accent),
        ) {
            // Flat "scene": sun + hills
            Canvas(Modifier.fillMaxSize()) {
                drawCircle(Flat.Yellow, radius = size.minDimension * 0.13f, center = Offset(size.width * 0.78f, size.height * 0.3f))
                drawCircle(Flat.BlueLight, radius = size.width * 0.45f, center = Offset(size.width * 0.15f, size.height * 1.15f))
                drawCircle(Color(0xFF0F4FCC), radius = size.width * 0.42f, center = Offset(size.width * 0.85f, size.height * 1.25f))
            }
            Box(
                Modifier.align(Alignment.Center).size(44.dp)
                    .graphicsLayer { scaleX = pulse; scaleY = pulse }
                    .background(Flat.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(Modifier.size(16.dp).graphicsLayer { translationX = 2.dp.toPx() }) {
                    val p = Path().apply {
                        moveTo(0f, 0f); lineTo(size.width, size.height / 2); lineTo(0f, size.height); close()
                    }
                    drawPath(p, app.accent)
                }
            }
            // Progress
            Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(10.dp).height(5.dp)) {
                Box(Modifier.fillMaxSize().background(Color(0x55FFFFFF), RoundedCornerShape(50)))
                Box(Modifier.fillMaxHeight().fillMaxWidth(progress).background(Flat.White, RoundedCornerShape(50)))
            }
        }
        Spacer(Modifier.height(14.dp))
        Text("Folders", style = display(14.sp), modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))
        val folders = listOf(
            "Camera" to Flat.Sky, "Movies" to Flat.Yellow,
            "Downloads" to Color(0xFFB8F0DC), "Shows" to Color(0xFFFFD0D0),
        )
        Column(Modifier.padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            folders.chunked(2).forEachIndexed { row, pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pair.forEachIndexed { col, (name, color) ->
                        val i = row * 2 + col
                        // Each tile "lights up" when the playhead passes its quarter.
                        val active = progress in (i / 4f)..((i + 1) / 4f)
                        Column(
                            Modifier.weight(1f)
                                .background(if (active) app.accent else color, RoundedCornerShape(12.dp))
                                .padding(10.dp),
                        ) {
                            FolderGlyph(if (active) Flat.White else Flat.Ink)
                            Spacer(Modifier.height(10.dp))
                            Text(name, style = body(11.sp, if (active) Flat.White else Flat.Ink, FontWeight.SemiBold))
                            Text("${12 + i * 7} videos", style = body(9.sp, if (active) Flat.Sky else Flat.InkSoft))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FolderGlyph(color: Color) {
    Canvas(Modifier.size(width = 20.dp, height = 15.dp)) {
        drawRoundRect(color, topLeft = Offset(0f, 0f), size = Size(size.width * 0.45f, size.height * 0.4f), cornerRadius = CornerRadius(3f))
        drawRoundRect(color, topLeft = Offset(0f, size.height * 0.2f), size = Size(size.width, size.height * 0.8f), cornerRadius = CornerRadius(4f))
    }
}

// ---------------------------------------------------------------- Klef

@Composable
private fun PasswordVaultScreen(app: PlayApp) {
    val t = rememberInfiniteTransition()
    // 0..12 dots typed, hold, then clear.
    val typed by t.animateFloat(
        0f, 15f,
        infiniteRepeatable(keyframes { durationMillis = 5200; 0f at 0; 12f at 3000; 12f at 4800; 15f at 5200 }),
    )
    val scan by t.animateFloat(0f, 1f, infiniteRepeatable(tween(1800, easing = LinearEasing)))
    val dots = typed.toInt().coerceAtMost(12)
    val strong = typed >= 12f && typed < 15f

    Column(Modifier.fillMaxSize()) {
        ScreenTitle("Klef", app.accent)
        // Biometric unlock
        Box(Modifier.fillMaxWidth().height(116.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(104.dp)) {
                val c = center
                drawCircle(app.accentSoft, radius = size.minDimension / 2)
                // Fingerprint-ish concentric arcs
                for (i in 0 until 4) {
                    val r = size.minDimension * (0.12f + i * 0.08f)
                    drawArc(
                        color = app.accent,
                        startAngle = 200f - i * 8f,
                        sweepAngle = 140f + i * 16f,
                        useCenter = false,
                        topLeft = Offset(c.x - r, c.y - r),
                        size = Size(r * 2, r * 2),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                    )
                }
                // Scanning line
                val y = size.height * (0.18f + 0.64f * scan)
                drawLine(Flat.Mint, Offset(size.width * 0.18f, y), Offset(size.width * 0.82f, y), 3.dp.toPx(), StrokeCap.Round)
            }
        }
        // Password field
        Column(Modifier.padding(horizontal = 14.dp)) {
            Text("Generated password", style = body(10.sp, Flat.InkSoft, FontWeight.Medium))
            Spacer(Modifier.height(6.dp))
            Row(
                Modifier.fillMaxWidth()
                    .background(if (strong) Flat.Mint else Flat.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(dots) {
                        Box(Modifier.size(7.dp).background(if (strong) Flat.White else Flat.Ink, CircleShape))
                    }
                }
                Text(if (strong) "STRONG" else "", style = body(9.sp, Flat.White, FontWeight.SemiBold))
            }
        }
        Spacer(Modifier.height(14.dp))
        val entries = listOf(
            Triple("G", Flat.Coral, 0.7f),
            Triple("in", Flat.Blue, 0.55f),
            Triple("N", Flat.Ink, 0.62f),
        )
        Column(Modifier.padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            entries.forEach { (letter, color, w) ->
                Row(
                    Modifier.fillMaxWidth().background(Flat.White, RoundedCornerShape(12.dp)).padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.size(28.dp).background(color, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Text(letter, style = display(12.sp, Flat.White))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        SkeletonLine(w, Flat.Ink.copy(alpha = 0.8f), 6)
                        SkeletonLine(w * 0.6f)
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------- Expense Tracker

@Composable
private fun ExpenseScreen(app: PlayApp) {
    val t = rememberInfiniteTransition()
    val sweep by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(keyframes { durationMillis = 6000; 0f at 0; 1f at 1800 using FastOutSlowInEasing; 1f at 6000 }),
    )
    val wave by t.animateFloat(0f, (2 * PI).toFloat(), infiniteRepeatable(tween(4000, easing = LinearEasing)))
    val segments = listOf(0.32f to app.accent, 0.24f to Flat.Coral, 0.18f to Flat.Blue, 0.14f to Flat.Mint, 0.12f to Flat.Violet)

    Column(Modifier.fillMaxSize()) {
        ScreenTitle("Spending", app.accent)
        Row(Modifier.padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(104.dp), contentAlignment = Alignment.Center) {
                Canvas(Modifier.fillMaxSize()) {
                    val stroke = 16.dp.toPx()
                    val inset = stroke / 2
                    drawArc(LineGrey, 0f, 360f, false, Offset(inset, inset), Size(size.width - stroke, size.height - stroke), style = Stroke(stroke))
                    var start = -90f
                    segments.forEach { (share, color) ->
                        val full = 360f * share
                        drawArc(color, start, (full - 3f).coerceAtLeast(0f) * sweep, false, Offset(inset, inset), Size(size.width - stroke, size.height - stroke), style = Stroke(stroke))
                        start += full
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${(sweep * 84).toInt()}%", style = display(18.sp))
                    Text("of budget", style = body(8.sp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Food", "Bills", "Travel", "Fun").forEachIndexed { i, name ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).background(segments[i].second, CircleShape))
                        Spacer(Modifier.width(6.dp))
                        Text(name, style = body(10.sp, Flat.Ink, FontWeight.Medium))
                    }
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        // Weekly bars
        Column(
            Modifier.padding(horizontal = 14.dp).fillMaxWidth()
                .background(Flat.White, RoundedCornerShape(14.dp)).padding(12.dp),
        ) {
            Text("This week", style = body(10.sp, Flat.InkSoft, FontWeight.Medium))
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth().height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                val base = listOf(0.45f, 0.7f, 0.35f, 0.9f, 0.55f, 0.75f, 0.4f)
                base.forEachIndexed { i, h ->
                    val v = (h + 0.12f * sin(wave + i)).coerceIn(0.1f, 1f)
                    Box(
                        Modifier.width(14.dp).fillMaxHeight(v)
                            .background(if (i == 3) app.accent else app.accentSoft, RoundedCornerShape(5.dp)),
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Column(Modifier.padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(Triple("Groceries", "-1,240", Flat.Mint), Triple("Electricity", "-860", Flat.Coral)).forEach { (n, amt, c) ->
                Row(
                    Modifier.fillMaxWidth().background(Flat.White, RoundedCornerShape(10.dp)).padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.size(18.dp).background(c, RoundedCornerShape(6.dp)))
                    Spacer(Modifier.width(8.dp))
                    Text(n, style = body(10.sp, Flat.Ink, FontWeight.Medium), modifier = Modifier.weight(1f))
                    Text(amt, style = display(11.sp))
                }
            }
        }
    }
}
