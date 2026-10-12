package com.roaa.portfolio.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.font.FontWeight
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
import com.roaa.portfolio.theme.label

/** Static skills band: one row per résumé group, chips fill with the group's accent on hover. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsBand(compact: Boolean) {
    val gap = if (compact) 8.dp else 10.dp
    Box(
        Modifier
            .fillMaxWidth()
            .background(Flat.Ink)
            .padding(horizontal = if (compact) 20.dp else 48.dp, vertical = if (compact) 36.dp else 56.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            Modifier.widthIn(max = 1240.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(if (compact) 22.dp else 18.dp),
        ) {
            Text("SKILLS & TOOLS", style = label(13.sp, Flat.Sky), modifier = Modifier.padding(bottom = if (compact) 0.dp else 10.dp))
            Profile.skills.forEachIndexed { row, group ->
                Reveal(delayMillis = row * 70, distance = 20.dp) {
                    val chips: @Composable () -> Unit = {
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(gap), verticalArrangement = Arrangement.spacedBy(gap)) {
                            group.items.forEachIndexed { i, skill ->
                                SkillChip(skill, group.accent, square = i % 2 == 1, compact = compact)
                            }
                        }
                    }
                    if (compact) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            GroupLabel(group.title, group.accent)
                            chips()
                        }
                    } else {
                        Row {
                            Box(Modifier.width(170.dp).padding(top = 12.dp)) { GroupLabel(group.title, group.accent) }
                            chips()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupLabel(title: String, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).background(accent, CircleShape))
        Spacer(Modifier.width(10.dp))
        Text(title.uppercase(), style = label(12.sp, Flat.White))
    }
}

@Composable
private fun SkillChip(text: String, accent: Color, square: Boolean, compact: Boolean) {
    val (source, hoveredState) = rememberHoverSource()
    val hovered by hoveredState
    val onAccent = if (accent == Flat.Yellow || accent == Flat.Amber || accent == Flat.Mint) Flat.Ink else Flat.White
    val bg by animateColorAsState(if (hovered) accent else Flat.White.copy(alpha = 0.06f))
    val fg by animateColorAsState(if (hovered) onAccent else Flat.White)
    val dot by animateColorAsState(if (hovered) onAccent else accent)
    val lift by animateFloatAsState(if (hovered) 1f else 0f)
    Row(
        Modifier
            .hoverable(source)
            .graphicsLayer { translationY = -lift * 4.dp.toPx() }
            .background(bg, RoundedCornerShape(50))
            .border(1.5.dp, if (hovered) accent else Flat.White.copy(alpha = 0.16f), RoundedCornerShape(50))
            .padding(horizontal = if (compact) 14.dp else 18.dp, vertical = if (compact) 8.dp else 9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(if (compact) 9.dp else 11.dp)
                .graphicsLayer { rotationZ = lift * 45f }
                .background(dot, if (square) RoundedCornerShape(2.dp) else CircleShape),
        )
        Spacer(Modifier.width(if (compact) 8.dp else 10.dp))
        Text(text, style = display(if (compact) 14.sp else 16.sp, fg, FontWeight.Medium), softWrap = false)
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
