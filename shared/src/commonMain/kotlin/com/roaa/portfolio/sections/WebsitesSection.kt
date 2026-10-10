package com.roaa.portfolio.sections

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roaa.portfolio.components.ArrowIcon
import com.roaa.portfolio.components.Reveal
import com.roaa.portfolio.components.SectionHeader
import com.roaa.portfolio.components.Tag
import com.roaa.portfolio.components.link
import com.roaa.portfolio.components.rememberHoverSource
import com.roaa.portfolio.data.Website
import com.roaa.portfolio.data.websites
import com.roaa.portfolio.theme.Flat
import com.roaa.portfolio.theme.body
import com.roaa.portfolio.theme.display
import kotlin.math.sin

@Composable
fun WebsitesSection(compact: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(if (compact) 28.dp else 40.dp)) {
        SectionHeader(
            index = "02",
            eyebrow = "Websites",
            title = "Built for the browser.",
            subtitle = "Not just Android — I build for the browser too.",
            compact = compact,
            accent = Flat.Mint,
        )
        if (compact) {
            websites.forEachIndexed { i, site -> Reveal(delayMillis = i * 120) { WebsiteCard(site, i, compact) } }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                websites.forEachIndexed { i, site ->
                    Reveal(Modifier.weight(1f), delayMillis = i * 150) { WebsiteCard(site, i, compact) }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WebsiteCard(site: Website, index: Int, compact: Boolean) {
    val (source, hoveredState) = rememberHoverSource()
    val hovered by hoveredState
    val lift by animateFloatAsState(if (hovered) 1f else 0f)

    Column(
        Modifier
            .fillMaxWidth()
            .hoverable(source)
            .link(site.url)
            .graphicsLayer { translationY = -lift * 8.dp.toPx() }
            .background(Flat.White, RoundedCornerShape(32.dp))
            .padding(16.dp),
    ) {
        // Browser chrome
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Flat.Ink)) {
            Row(Modifier.padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                listOf(Flat.Coral, Flat.Yellow, Flat.Mint).forEach {
                    Box(Modifier.padding(end = 6.dp).size(10.dp).background(it, CircleShape))
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    site.displayUrl,
                    style = body(12.sp, Flat.Sky),
                    maxLines = 1,
                    modifier = Modifier.weight(1f).background(Color(0xFF26355A), RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                )
            }
            Box(Modifier.fillMaxWidth().aspectRatio(16f / 9.5f).background(site.accent)) {
                if (index == 0) DashboardIllustration() else PortfolioIllustration()
            }
        }
        Column(Modifier.padding(horizontal = 10.dp, vertical = 22.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(site.name, style = display(if (compact) 26.sp else 30.sp), modifier = Modifier.weight(1f))
                Box(
                    Modifier.size(44.dp).background(if (hovered) site.accent else Flat.Paper, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    ArrowIcon(if (hovered) Flat.White else Flat.Ink, Modifier.size(18.dp), diagonal = true)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(site.description, style = body(16.sp))
            Spacer(Modifier.height(18.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                site.stack.forEach { Tag(it, Flat.Paper) }
            }
        }
    }
}

/** Mini dashboard: a grid of repo tiles whose sparkline bars breathe. */
@Composable
private fun DashboardIllustration() {
    val t = rememberInfiniteTransition()
    val wave by t.animateFloat(0f, 6.283f, infiniteRepeatable(tween(3600, easing = LinearEasing)))
    Row(Modifier.fillMaxSize().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        // Sidebar
        Column(
            Modifier.fillMaxHeight().width(54.dp).background(Color(0x33FFFFFF), RoundedCornerShape(10.dp)).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            repeat(5) { i ->
                Box(Modifier.fillMaxWidth(if (i == 0) 1f else 0.7f).height(7.dp).background(if (i == 1) Flat.White else Color(0x66FFFFFF), RoundedCornerShape(50)))
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(2) { r ->
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(3) { c ->
                        val i = r * 3 + c
                        Column(
                            Modifier.weight(1f).fillMaxHeight().background(Flat.White, RoundedCornerShape(10.dp)).padding(8.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(9.dp).background(listOf(Flat.Blue, Flat.Coral, Flat.Amber, Flat.Violet, Flat.Mint, Flat.Ink)[i], CircleShape))
                                Spacer(Modifier.width(5.dp))
                                Box(Modifier.fillMaxWidth(0.8f).height(5.dp).background(Color(0xFFE4E1D8), RoundedCornerShape(50)))
                            }
                            Row(Modifier.fillMaxWidth().weight(1f).padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.Bottom) {
                                repeat(5) { b ->
                                    val h = (0.55f + 0.4f * sin(wave + i * 0.9f + b * 0.8f)).coerceIn(0.15f, 1f)
                                    Box(Modifier.weight(1f).fillMaxHeight(h).background(if (b == 4) Flat.Mint else Color(0xFFB8F0DC), RoundedCornerShape(3.dp)))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** A tiny version of this site's hero — recursion, but flat. */
@Composable
private fun PortfolioIllustration() {
    val t = rememberInfiniteTransition()
    val spin by t.animateFloat(0f, 360f, infiniteRepeatable(tween(8000, easing = LinearEasing)))
    val bob by t.animateFloat(-1f, 1f, infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse))
    Row(Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.width(60.dp).height(10.dp).background(Flat.Ink, RoundedCornerShape(50)))
            Box(Modifier.fillMaxWidth(0.9f).height(18.dp).background(Flat.White, RoundedCornerShape(5.dp)))
            Box(Modifier.fillMaxWidth(0.6f).height(18.dp).background(Flat.White, RoundedCornerShape(5.dp)))
            Box(Modifier.width(96.dp).height(16.dp).background(Flat.Blue, RoundedCornerShape(5.dp)))
            Spacer(Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(Modifier.width(54.dp).height(16.dp).background(Flat.Ink, RoundedCornerShape(50)))
                Box(Modifier.width(40.dp).height(16.dp).background(Color(0x55FFFFFF), RoundedCornerShape(50)))
            }
        }
        Box(Modifier.size(96.dp).graphicsLayer { translationY = bob * 5.dp.toPx() }, contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize().graphicsLayer { rotationZ = spin }) {
                drawCircle(Flat.Yellow, radius = size.minDimension * 0.42f)
                drawCircle(Flat.Ink, radius = size.minDimension * 0.07f, center = Offset(size.width * 0.5f, size.height * 0.04f))
                drawCircle(Flat.Blue, radius = size.minDimension * 0.07f, center = Offset(size.width * 0.06f, size.height * 0.7f))
                drawCircle(Flat.Mint, radius = size.minDimension * 0.07f, center = Offset(size.width * 0.94f, size.height * 0.7f))
            }
            Box(Modifier.size(52.dp).background(Flat.Ink, CircleShape))
        }
    }
}
