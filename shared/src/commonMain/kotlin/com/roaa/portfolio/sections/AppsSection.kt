package com.roaa.portfolio.sections

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roaa.portfolio.components.ButtonStyle
import com.roaa.portfolio.components.FlatButton
import com.roaa.portfolio.components.Reveal
import com.roaa.portfolio.components.SectionHeader
import com.roaa.portfolio.components.Tag
import com.roaa.portfolio.components.rememberHoverSource
import com.roaa.portfolio.data.PlayApp
import com.roaa.portfolio.data.Profile
import com.roaa.portfolio.data.playApps
import com.roaa.portfolio.theme.Flat
import com.roaa.portfolio.theme.body
import com.roaa.portfolio.theme.display
import com.roaa.portfolio.theme.label
import org.jetbrains.compose.resources.painterResource

private val StageHeight = 660.dp

@Composable
fun AppsSection(compact: Boolean) {
    val uri = LocalUriHandler.current
    Column(verticalArrangement = Arrangement.spacedBy(if (compact) 28.dp else 40.dp)) {
        SectionHeader(
            index = "01",
            eyebrow = "Apps",
            title = "Shipped to Google Play.",
            subtitle = "Real products with real users — designed, built and published end-to-end under my publisher name, ${Profile.PUBLISHER}.",
            compact = compact,
        )
        playApps.forEachIndexed { i, app ->
            Reveal(distance = 80.dp) {
                AppShowcase(app, index = i, compact = compact, flipped = i % 2 == 1)
            }
        }
        Reveal {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                FlatButton(
                    "See all apps by ${Profile.PUBLISHER}",
                    onClick = { uri.openUri(Profile.PLAY_STORE) },
                    style = ButtonStyle.Outline,
                )
            }
        }
    }
}

@Composable
private fun AppShowcase(app: PlayApp, index: Int, compact: Boolean, flipped: Boolean) {
    val (source, hoveredState) = rememberHoverSource()
    val hovered by hoveredState
    val tilt by animateFloatAsState(if (hovered) (if (flipped) 4f else -4f) else 0f, spring(stiffness = 120f))
    val lift by animateFloatAsState(if (hovered) 1.04f else 1f, spring(stiffness = 120f))

    val stage: @Composable (Modifier) -> Unit = { m ->
        Box(
            m.clip(RoundedCornerShape(28.dp)).background(app.accent),
            contentAlignment = Alignment.Center,
        ) {
            StageDecor(Modifier.matchParentSize())
            PhoneMockup(
                app,
                Modifier
                    .padding(vertical = if (compact) 36.dp else 48.dp)
                    .graphicsLayer {
                        rotationZ = tilt
                        scaleX = lift
                        scaleY = lift
                    },
            )
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .hoverable(source)
            .background(app.accentSoft, RoundedCornerShape(36.dp))
            .padding(if (compact) 14.dp else 20.dp),
    ) {
        if (compact) {
            Column {
                stage(Modifier.fillMaxWidth())
                AppDetails(app, index, compact = true, Modifier.padding(start = 8.dp, end = 8.dp, top = 28.dp, bottom = 12.dp))
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (flipped) stage(Modifier.weight(0.9f).height(StageHeight))
                AppDetails(
                    app,
                    index,
                    compact = false,
                    Modifier.weight(1.1f).padding(horizontal = 44.dp, vertical = 36.dp),
                )
                if (!flipped) stage(Modifier.weight(0.9f).height(StageHeight))
            }
        }
    }
}

@Composable
private fun StageDecor(modifier: Modifier) {
    val light = Color.White.copy(alpha = 0.14f)
    Canvas(modifier) {
        drawCircle(light, radius = size.minDimension * 0.42f, center = center)
        drawCircle(light, radius = size.minDimension * 0.28f, center = Offset(size.width * 0.1f, size.height * 0.12f))
        drawRect(light, topLeft = Offset(size.width * 0.82f, size.height * 0.72f), size = androidx.compose.ui.geometry.Size(size.width * 0.3f, size.width * 0.3f))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AppDetails(app: PlayApp, index: Int, compact: Boolean, modifier: Modifier) {
    val uri = LocalUriHandler.current
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(app.icon),
                contentDescription = "${app.name} icon",
                modifier = Modifier.size(if (compact) 56.dp else 68.dp).clip(RoundedCornerShape(22)),
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(app.name, style = display(if (compact) 22.sp else 26.sp))
                Text("${Profile.PUBLISHER} · Updated ${app.updated}", style = body(13.sp))
            }
            Text(
                "0${index + 1}",
                style = display(if (compact) 34.sp else 52.sp, app.accent),
            )
        }
        Spacer(Modifier.height(if (compact) 22.dp else 30.dp))
        Text(app.tagline, style = display(if (compact) 30.sp else 42.sp))
        Spacer(Modifier.height(14.dp))
        Text(app.description, style = body(if (compact) 15.sp else 17.sp))
        Spacer(Modifier.height(22.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            app.features.forEach { Tag(it, Flat.White) }
        }
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(
                Modifier.background(Flat.Ink, RoundedCornerShape(16.dp)).padding(horizontal = 16.dp, vertical = 10.dp),
            ) {
                Text(app.downloads, style = display(22.sp, Flat.White))
                Text("DOWNLOADS", style = label(10.sp, Flat.Sky))
            }
            Column(
                Modifier.background(Flat.White, RoundedCornerShape(16.dp)).padding(horizontal = 16.dp, vertical = 10.dp),
            ) {
                Text("Free", style = display(22.sp))
                Text("ON ANDROID", style = label(10.sp, Flat.InkSoft))
            }
        }
        Spacer(Modifier.height(28.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            FlatButton("Get it on Google Play", onClick = { uri.openUri(app.playUrl) }, color = Flat.Ink)
            app.sourceUrl?.let { url ->
                FlatButton("Source code", onClick = { uri.openUri(url) }, style = ButtonStyle.Outline, showArrow = false)
            }
        }
    }
}
