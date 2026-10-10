package com.roaa.portfolio.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roaa.portfolio.components.ArrowIcon
import com.roaa.portfolio.components.ButtonStyle
import com.roaa.portfolio.components.FlatButton
import com.roaa.portfolio.components.Reveal
import com.roaa.portfolio.components.SectionHeader
import com.roaa.portfolio.components.link
import com.roaa.portfolio.components.rememberHoverSource
import com.roaa.portfolio.data.Profile
import com.roaa.portfolio.data.Project
import com.roaa.portfolio.data.projects
import com.roaa.portfolio.theme.Flat
import com.roaa.portfolio.theme.body
import com.roaa.portfolio.theme.display

/** Infinite skills ticker on a slightly tilted ink band. */
@Composable
fun SkillsMarquee(compact: Boolean) {
    val dots = listOf(Flat.Yellow, Flat.Coral, Flat.Mint, Flat.Blue, Flat.Violet, Flat.Amber)
    Box(
        Modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationZ = -2f
                scaleX = 1.06f
            }
            .background(Flat.Ink)
            .padding(vertical = if (compact) 18.dp else 26.dp),
    ) {
        Row(
            Modifier.fillMaxWidth().basicMarquee(iterations = Int.MAX_VALUE, velocity = 70.dp, repeatDelayMillis = 0, spacing = MarqueeSpacing(0.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(2) {
                Profile.skills.forEachIndexed { i, skill ->
                    Text(skill, style = display(if (compact) 22.sp else 32.sp, Flat.White), softWrap = false)
                    Box(
                        Modifier.padding(horizontal = if (compact) 18.dp else 28.dp)
                            .size(if (compact) 12.dp else 16.dp)
                            .background(dots[i % dots.size], if (i % 2 == 0) CircleShape else RoundedCornerShape(3.dp)),
                    )
                }
            }
        }
    }
}

@Composable
fun WorkSection(compact: Boolean, columns: Int) {
    val uri = LocalUriHandler.current
    Column(verticalArrangement = Arrangement.spacedBy(if (compact) 28.dp else 40.dp)) {
        SectionHeader(
            index = "03",
            eyebrow = "Open source",
            title = "More things I've built.",
            subtitle = "Experiments, tools and learning projects — everything from clean-architecture templates to my first SwiftUI app.",
            compact = compact,
            accent = Flat.Coral,
        )
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            projects.chunked(columns).forEachIndexed { row, chunk ->
                Row(Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                    chunk.forEachIndexed { col, project ->
                        Reveal(Modifier.weight(1f).fillMaxHeight(), delayMillis = col * 90 + row * 30) {
                            ProjectCard(project)
                        }
                    }
                    repeat(columns - chunk.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
        Reveal {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                FlatButton("Browse everything on GitHub", onClick = { uri.openUri(Profile.GITHUB) }, style = ButtonStyle.Outline)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProjectCard(project: Project) {
    val (source, hoveredState) = rememberHoverSource()
    val hovered by hoveredState
    val onColor = if (project.color == Flat.Yellow || project.color == Flat.Sky) Flat.Ink else Flat.White
    val bg by animateColorAsState(if (hovered) project.color else Flat.White)
    val fg by animateColorAsState(if (hovered) onColor else Flat.Ink)
    val fgSoft by animateColorAsState(if (hovered) onColor.copy(alpha = 0.85f) else Flat.InkSoft)
    val lift by animateFloatAsState(if (hovered) 1f else 0f)

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .heightIn(min = 250.dp)
            .hoverable(source)
            .link(project.url)
            .graphicsLayer { translationY = -lift * 6.dp.toPx() }
            .background(bg, RoundedCornerShape(26.dp))
            .padding(24.dp),
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                Modifier.size(46.dp)
                    .graphicsLayer { rotationZ = lift * -12f }
                    .background(if (hovered) Color.White.copy(alpha = 0.22f) else project.color, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text("</>", style = display(15.sp, if (hovered) onColor else if (project.color == Flat.Yellow) Flat.Ink else Flat.White))
            }
            Spacer(Modifier.weight(1f))
            ArrowIcon(fg, Modifier.size(20.dp).graphicsLayer { translationX = lift * 3.dp.toPx(); translationY = -lift * 3.dp.toPx() }, diagonal = true)
        }
        Spacer(Modifier.height(22.dp))
        Text(project.name, style = display(21.sp, fg))
        Spacer(Modifier.height(8.dp))
        Text(project.description, style = body(14.sp, fgSoft), modifier = Modifier.weight(1f, fill = false))
        Spacer(Modifier.height(18.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            project.tags.forEach {
                Text(
                    it,
                    style = body(12.sp, fg),
                    modifier = Modifier.background(if (hovered) Color.White.copy(alpha = 0.2f) else Flat.Paper, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }
        }
    }
}
