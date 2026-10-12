package com.roaa.portfolio.sections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalUriHandler
import com.roaa.portfolio.components.ButtonStyle
import com.roaa.portfolio.components.FlatButton
import com.roaa.portfolio.components.handClick
import com.roaa.portfolio.components.rememberHoverSource
import com.roaa.portfolio.data.Profile
import com.roaa.portfolio.data.playApps
import com.roaa.portfolio.theme.Flat
import com.roaa.portfolio.theme.body
import com.roaa.portfolio.theme.display
import com.roaa.portfolio.theme.label
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import portfolio.shared.generated.resources.Res
import portfolio.shared.generated.resources.avatar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private enum class ShapeKind { Circle, Square, Triangle, Ring, Plus, Zigzag }

private data class FloatingShape(
    val kind: ShapeKind,
    val color: Color,
    val x: Float,
    val y: Float,
    val size: Dp,
    val depth: Float,
    val phase: Float,
)

private val heroShapes = listOf(
    FloatingShape(ShapeKind.Circle, Flat.Yellow, 0.06f, 0.18f, 26.dp, 0.6f, 0f),
    FloatingShape(ShapeKind.Triangle, Flat.Coral, 0.46f, 0.08f, 30.dp, 1.0f, 1.2f),
    FloatingShape(ShapeKind.Square, Flat.Mint, 0.9f, 0.12f, 22.dp, 0.8f, 2.1f),
    FloatingShape(ShapeKind.Ring, Flat.Blue, 0.03f, 0.78f, 34.dp, 1.2f, 0.7f),
    FloatingShape(ShapeKind.Plus, Flat.Violet, 0.52f, 0.9f, 24.dp, 0.7f, 3.0f),
    FloatingShape(ShapeKind.Zigzag, Flat.Amber, 0.94f, 0.86f, 46.dp, 0.9f, 1.7f),
    FloatingShape(ShapeKind.Circle, Flat.Sky, 0.38f, 0.55f, 14.dp, 1.4f, 2.6f),
)

@Composable
fun HeroSection(compact: Boolean, onExploreApps: () -> Unit, onContact: () -> Unit) {
    // Normalised pointer position (-1..1) drives a subtle parallax on the shapes.
    var pointer by remember { mutableStateOf(Offset.Zero) }
    val parallax by animateOffsetAsState(pointer, spring(stiffness = 60f))

    val t = rememberInfiniteTransition()
    val time by t.animateFloat(
        0f,
        (2 * PI).toFloat(),
        infiniteRepeatable(tween(9000, easing = LinearEasing))
    )

    BoxWithConstraints(
        Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        val p = event.changes.firstOrNull()?.position ?: continue
                        pointer = Offset(p.x / size.width * 2 - 1, p.y / size.height * 2 - 1)
                    }
                }
            },
    ) {
        // Background shapes
        Canvas(Modifier.matchParentSize()) {
            heroShapes.forEach { s ->
                val px = s.size.toPx()
                val drift = sin(time + s.phase) * 14.dp.toPx()
                val cx = s.x * size.width + parallax.x * 26.dp.toPx() * s.depth
                val cy = s.y * size.height + drift + parallax.y * 26.dp.toPx() * s.depth
                drawFlatShape(
                    s.kind,
                    s.color,
                    Offset(cx, cy),
                    px,
                    rotation = (time + s.phase) * 57.3f * 0.5f
                )
            }
        }

        if (compact) {
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                    .padding(top = 110.dp, bottom = 64.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                HeroVisual(
                    diameter = 290.dp,
                    time = time,
                    parallax = parallax,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(36.dp))
                HeroCopy(compact = true, onExploreApps, onContact)
            }
        } else {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 48.dp)
                    .padding(top = 150.dp, bottom = 110.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.weight(1.15f)) { HeroCopy(compact = false, onExploreApps, onContact) }
                Spacer(Modifier.width(32.dp))
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    HeroVisual(diameter = 420.dp, time = time, parallax = parallax)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeroCopy(compact: Boolean, onExploreApps: () -> Unit, onContact: () -> Unit) {
    val uri = LocalUriHandler.current
    // Staggered entrance for each line of the hero copy.
    val enter = remember { List(6) { Animatable(0f) } }
    LaunchedEffect(Unit) {
        enter.forEachIndexed { i, a ->
            launch {
                delay(i * 110L)
                a.animateTo(1f, tween(750, easing = FastOutSlowInEasing))
            }
        }
    }
    fun Modifier.entrance(i: Int) = graphicsLayer {
        alpha = enter[i].value
        translationY = (1 - enter[i].value) * 36.dp.toPx()
    }

    Column {
        Row(
            Modifier.entrance(0).background(Flat.Ink, RoundedCornerShape(50))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LiveDot()
            Spacer(Modifier.width(8.dp))
            Text("3 apps live on Google Play", style = body(13.sp, Flat.White, FontWeight.Medium))
        }
        Spacer(Modifier.height(28.dp))
        Text(
            "Hi, I'm",
            style = display(if (compact) 26.sp else 34.sp, Flat.InkSoft, FontWeight.Medium),
            modifier = Modifier.entrance(1)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            Profile.NAME,
            style = display(if (compact) 50.sp else 84.sp),
            modifier = Modifier.entrance(2)
        )
        Spacer(Modifier.height(18.dp))
        Box(Modifier.entrance(3)) { RoleRotator(compact) }
        Spacer(Modifier.height(24.dp))
        Text(
            Profile.INTRO,
            style = body(if (compact) 16.sp else 19.sp),
            modifier = Modifier.entrance(4).fillMaxWidth(if (compact) 1f else 0.92f),
        )
        Spacer(Modifier.height(36.dp))
        FlowRow(
            Modifier.entrance(5),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            FlatButton("Explore my apps", onExploreApps, color = Flat.Blue)
            FlatButton(
                "Say hello",
                onContact,
                color = Flat.Ink,
                style = ButtonStyle.Outline,
                showArrow = false
            )
            FlatButton(
                "Download résumé",
                { uri.openUri(Profile.RESUME) },
                color = Flat.Ink,
                style = ButtonStyle.Outline,
                arrowDown = true,
            )
        }
        Spacer(Modifier.height(48.dp))
        Row(
            Modifier.entrance(5),
            horizontalArrangement = Arrangement.spacedBy(if (compact) 24.dp else 44.dp)
        ) {
            CountUpStat(playApps.size, "", "Apps on\nGoogle Play", compact)
            CountUpStat(1, "K+", "Downloads\nand counting", compact)
            CountUpStat(Profile.PUBLIC_REPOS, "+", "Open-source\nrepos", compact)
        }
    }
}

@Composable
private fun LiveDot() {
    val t = rememberInfiniteTransition()
    val ring by t.animateFloat(0f, 1f, infiniteRepeatable(tween(1400, easing = LinearEasing)))
    Box(Modifier.size(12.dp), contentAlignment = Alignment.Center) {
        Box(
            Modifier.size(12.dp).graphicsLayer {
                scaleX = 0.6f + ring
                scaleY = 0.6f + ring
                alpha = 1f - ring
            }.background(Flat.Mint, CircleShape),
        )
        Box(Modifier.size(8.dp).background(Flat.Mint, CircleShape))
    }
}

@Composable
private fun RoleRotator(compact: Boolean) {
    var index by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(2400)
            index = (index + 1) % Profile.roles.size
        }
    }
    val colors = listOf(Flat.Blue, Flat.Coral, Flat.Mint, Flat.Violet)
    AnimatedContent(
        targetState = index,
        transitionSpec = {
            (slideInVertically(tween(450)) { it } + fadeIn(tween(450)))
                .togetherWith(slideOutVertically(tween(450)) { -it } + fadeOut(tween(300)))
        },
    ) { i ->
        Text(
            Profile.roles[i],
            style = display(if (compact) 24.sp else 34.sp, Flat.White),
            modifier = Modifier
                .background(colors[i % colors.size], RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun CountUpStat(target: Int, suffix: String, caption: String, compact: Boolean) {
    val value = remember { Animatable(0f) }
    LaunchedEffect(target) {
        delay(700)
        value.animateTo(target.toFloat(), tween(1400, easing = FastOutSlowInEasing))
    }
    Column {
        Text("${value.value.roundToInt()}$suffix", style = display(if (compact) 30.sp else 42.sp))
        Spacer(Modifier.height(4.dp))
        Text(caption, style = body(if (compact) 12.sp else 14.sp))
    }
}

/**
 * Photo on the front, the clay 3D avatar on the back. Hover (desktop) or tap
 * (touch) flips the card around its vertical axis.
 */
@Composable
private fun FlipAvatar(size: Dp, look: Offset) {
    val (source, hoveredState) = rememberHoverSource()
    val hovered by hoveredState
    var tapped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (hovered != tapped) 180f else 0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 140f),
    )
    val faceModifier = Modifier.fillMaxSize().clip(CircleShape).background(Flat.Sky)
    Box(
        Modifier
            .size(size)
            .hoverable(source)
            .handClick { tapped = !tapped }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 14f * density
            },
    ) {
        Image(
            painterResource(Res.drawable.avatar),
            contentDescription = Profile.NAME,
            contentScale = ContentScale.Crop,
            modifier = faceModifier,
        )
//        if (rotation <= 90f) {
//            Image(
//                painterResource(Res.drawable.avatar),
//                contentDescription = Profile.NAME,
//                contentScale = ContentScale.Crop,
//                modifier = faceModifier,
//            )
//        } else {
//            // Counter-rotate so the back face isn't mirrored.
//            Avatar3D(look, faceModifier.graphicsLayer { rotationY = 180f })
//        }
    }
}

@Composable
private fun HeroVisual(diameter: Dp, time: Float, parallax: Offset, modifier: Modifier = Modifier) {
    val pop = remember { Animatable(0f) }
    LaunchedEffect(Unit) { pop.animateTo(1f, spring(dampingRatio = 0.55f, stiffness = 120f)) }
    Box(
        modifier.size(diameter).graphicsLayer {
            scaleX = pop.value
            scaleY = pop.value
            translationX = -parallax.x * 10.dp.toPx()
            translationY = -parallax.y * 10.dp.toPx()
        },
        contentAlignment = Alignment.Center,
    ) {
        // Solid blob + rotating dashed orbit
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(
                Flat.Blue,
                radius = size.minDimension * 0.4f,
                center = center + Offset(size.width * 0.04f, size.height * 0.04f)
            )
            drawCircle(
                Flat.Yellow,
                radius = size.minDimension * 0.4f,
                center = center - Offset(size.width * 0.03f, size.height * 0.03f)
            )
            rotate(time * 57.3f / 3f) {
                drawCircle(
                    Flat.Ink,
                    radius = size.minDimension * 0.48f,
                    style = Stroke(
                        2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 14f))
                    ),
                )
            }
        }
        FlipAvatar(diameter * 0.66f, parallax)
        // Shipped app icons orbit the photo.
        val radius = diameter * 0.48f
        playApps.forEachIndexed { i, app ->
            val angle = time + i * (2 * PI / playApps.size).toFloat()
            val iconSize = diameter * 0.17f
            Image(
                painterResource(app.icon),
                contentDescription = app.name,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (cos(angle) * radius.toPx()).roundToInt(),
                            (sin(angle) * radius.toPx()).roundToInt()
                        )
                    }
                    .size(iconSize)
                    .background(Flat.Paper, RoundedCornerShape(28))
                    .padding(4.dp)
                    .clip(RoundedCornerShape(24)),
            )
        }
        Text(
            "ANDROID DEV",
            style = label(12.sp, Flat.White),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = diameter * 0.04f)
                .background(Flat.Ink, RoundedCornerShape(50))
                .padding(horizontal = 14.dp, vertical = 7.dp),
        )
    }
}

private fun DrawScope.drawFlatShape(
    kind: ShapeKind,
    color: Color,
    center: Offset,
    size: Float,
    rotation: Float
) {
    translate(center.x, center.y) {
        rotate(rotation, pivot = Offset.Zero) {
            val h = size / 2
            when (kind) {
                ShapeKind.Circle -> drawCircle(color, h, Offset.Zero)
                ShapeKind.Square -> drawRect(color, Offset(-h, -h), Size(size, size))
                ShapeKind.Ring -> drawCircle(color, h, Offset.Zero, style = Stroke(size * 0.22f))
                ShapeKind.Triangle -> drawPath(
                    Path().apply {
                        moveTo(0f, -h); lineTo(h, h * 0.8f); lineTo(
                        -h,
                        h * 0.8f
                    ); close()
                    },
                    color,
                )

                ShapeKind.Plus -> {
                    val t = size * 0.3f
                    drawRect(color, Offset(-t / 2, -h), Size(t, size))
                    drawRect(color, Offset(-h, -t / 2), Size(size, t))
                }

                ShapeKind.Zigzag -> {
                    val p = Path()
                    val steps = 4
                    p.moveTo(-h, 0f)
                    for (i in 1..steps) p.lineTo(
                        -h + size * i / steps,
                        if (i % 2 == 1) -h * 0.4f else 0f
                    )
                    drawPath(p, color, style = Stroke(size * 0.12f))
                }
            }
        }
    }
}
