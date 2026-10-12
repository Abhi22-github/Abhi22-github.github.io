package com.roaa.portfolio.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roaa.portfolio.components.ArrowIcon
import com.roaa.portfolio.components.Reveal
import com.roaa.portfolio.components.handClick
import com.roaa.portfolio.components.link
import com.roaa.portfolio.components.rememberHoverSource
import androidx.compose.ui.platform.LocalUriHandler
import com.roaa.portfolio.components.ButtonStyle
import com.roaa.portfolio.components.FlatButton
import com.roaa.portfolio.data.Profile
import com.roaa.portfolio.data.playApps
import com.roaa.portfolio.theme.Flat
import com.roaa.portfolio.theme.body
import com.roaa.portfolio.theme.display
import com.roaa.portfolio.theme.label

private enum class Glyph { Mail, Code, LinkedIn, Play }

private data class ContactLink(val title: String, val handle: String, val url: String, val color: Color, val glyph: Glyph)

private val contactLinks = listOf(
    ContactLink("Email", "Send me a message", "mailto:${Profile.EMAIL}", Flat.Coral, Glyph.Mail),
    ContactLink("GitHub", "@Abhi22-github", Profile.GITHUB, Flat.Ink, Glyph.Code),
    ContactLink("LinkedIn", "abhishek-satpute", Profile.LINKEDIN, Flat.Blue, Glyph.LinkedIn),
    ContactLink("Google Play", "${playApps.size} apps live", Profile.PLAY_STORE, Flat.Mint, Glyph.Play),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContactSection(compact: Boolean) {
    val uri = LocalUriHandler.current
    val t = rememberInfiniteTransition()
    val spin by t.animateFloat(0f, 360f, infiniteRepeatable(tween(20000, easing = LinearEasing)))

    Reveal(distance = 80.dp) {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(if (compact) 28.dp else 44.dp))
                .background(Flat.Blue),
        ) {
            // Decorative flat shapes
            Canvas(Modifier.matchParentSize()) {
                drawCircle(Flat.Yellow, radius = size.minDimension * 0.22f, center = Offset(size.width * 0.96f, size.height * 0.05f))
                drawCircle(Color(0xFF175BD6), radius = size.minDimension * 0.5f, center = Offset(size.width * 0.02f, size.height * 1.05f))
                val sq = size.minDimension * 0.12f
                val c = Offset(size.width * 0.72f, size.height * 0.2f)
                rotate(spin, c) {
                    drawRect(Flat.Coral, topLeft = c - Offset(sq / 2, sq / 2), size = Size(sq, sq))
                }
            }
            Column(Modifier.padding(if (compact) 26.dp else 72.dp)) {
                Text("04 / CONTACT", style = label(13.sp, Flat.Sky))
                Spacer(Modifier.height(18.dp))
                Text(
                    "Got an idea?\nLet's build it.",
                    style = display(if (compact) 44.sp else 86.sp, Flat.White),
                )
                Spacer(Modifier.height(22.dp))
                Text(
                    "A project, a role, or just a chat about apps — my inbox is open.",
                    style = body(if (compact) 16.sp else 20.sp, Flat.Sky),
                    modifier = Modifier.fillMaxWidth(if (compact) 1f else 0.6f),
                )
                Spacer(Modifier.height(32.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    itemVerticalAlignment = Alignment.CenterVertically,
                ) {
                    EmailPill(compact)
                    FlatButton(
                        "Download résumé",
                        { uri.openUri(Profile.RESUME) },
                        color = Flat.White,
                        contentColor = Flat.Blue,
                        style = ButtonStyle.Outline,
                        arrowDown = true,
                    )
                }
                Spacer(Modifier.height(if (compact) 32.dp else 48.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    maxItemsInEachRow = if (compact) 1 else 4,
                ) {
                    contactLinks.forEach { ContactTile(it, Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun EmailPill(compact: Boolean) {
    val (source, hoveredState) = rememberHoverSource()
    val hovered by hoveredState
    val bg by animateColorAsState(if (hovered) Flat.Yellow else Flat.White)
    Row(
        Modifier
            .hoverable(source)
            .link("mailto:${Profile.EMAIL}")
            .background(bg, RoundedCornerShape(50))
            .padding(start = if (compact) 18.dp else 28.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(Profile.EMAIL, style = display(if (compact) 16.sp else 26.sp, Flat.Ink, FontWeight.Medium))
        Spacer(Modifier.width(if (compact) 12.dp else 20.dp))
        Box(
            Modifier.size(if (compact) 40.dp else 56.dp).background(Flat.Ink, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            ArrowIcon(Flat.White, Modifier.size(if (compact) 16.dp else 22.dp), diagonal = !hovered)
        }
    }
}

@Composable
private fun ContactTile(link: ContactLink, modifier: Modifier) {
    val (source, hoveredState) = rememberHoverSource()
    val hovered by hoveredState
    val lift by animateFloatAsState(if (hovered) 1f else 0f)
    Row(
        modifier
            .hoverable(source)
            .link(link.url)
            .graphicsLayer { translationY = -lift * 6.dp.toPx() }
            .background(Flat.White, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(48.dp).graphicsLayer { rotationZ = lift * 8f }.background(link.color, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            GlyphIcon(link.glyph)
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(link.title, style = display(17.sp))
            Text(link.handle, style = body(12.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun GlyphIcon(glyph: Glyph) {
    when (glyph) {
        Glyph.LinkedIn -> Text("in", style = display(20.sp, Flat.White))
        Glyph.Code -> Text("</>", style = display(15.sp, Flat.White))
        Glyph.Mail -> Canvas(Modifier.size(width = 22.dp, height = 16.dp)) {
            val s = 2.2.dp.toPx()
            drawRoundRect(Flat.White, style = Stroke(s), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()))
            val p = Path().apply {
                moveTo(s, s); lineTo(size.width / 2, size.height * 0.58f); lineTo(size.width - s, s)
            }
            drawPath(p, Flat.White, style = Stroke(s, join = StrokeJoin.Round))
        }
        Glyph.Play -> Canvas(Modifier.size(18.dp).graphicsLayer { translationX = 2.dp.toPx() }) {
            val p = Path().apply { moveTo(0f, 0f); lineTo(size.width, size.height / 2); lineTo(0f, size.height); close() }
            drawPath(p, Flat.White)
        }
    }
}

@Composable
fun Footer(compact: Boolean, onBackToTop: () -> Unit) {
    val (source, hoveredState) = rememberHoverSource()
    val hovered by hoveredState
    val content: @Composable () -> Unit = {
        Column {
            Text("© 2026 ${Profile.NAME}", style = body(14.sp, Flat.Ink, FontWeight.SemiBold))
            Text("Built with Compose Multiplatform & WebAssembly.", style = body(13.sp))
        }
        Row(
            Modifier.hoverable(source).handClick(onBackToTop),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Back to top", style = body(14.sp, Flat.Ink, FontWeight.SemiBold))
            Spacer(Modifier.width(10.dp))
            Box(
                Modifier.size(36.dp).background(if (hovered) Flat.Coral else Flat.Ink, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                ArrowIcon(Flat.White, Modifier.size(14.dp).graphicsLayer { rotationZ = -90f })
            }
        }
    }
    if (compact) {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) { content() }
    } else {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) { content() }
    }
}
