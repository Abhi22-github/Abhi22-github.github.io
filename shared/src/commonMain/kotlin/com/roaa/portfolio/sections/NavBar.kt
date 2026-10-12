package com.roaa.portfolio.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roaa.portfolio.components.handClick
import com.roaa.portfolio.components.rememberHoverSource
import com.roaa.portfolio.data.Profile
import com.roaa.portfolio.theme.Flat
import com.roaa.portfolio.theme.body
import com.roaa.portfolio.theme.display

enum class Section(val title: String) { Top("Home"), Apps("Apps"), Websites("Websites"), Work("Work"), Contact("Contact") }

@Composable
fun NavBar(
    compact: Boolean,
    scrolled: Boolean,
    progress: Float,
    active: Section,
    onNavigate: (Section) -> Unit,
) {
    // Fade to a transparent *paper* colour, not Color.Transparent (transparent black),
    // otherwise the in-between frames flash dark grey.
    val bg by animateColorAsState(if (scrolled) Flat.Paper else Flat.Paper.copy(alpha = 0f))
    val vPad by animateDpAsState(if (scrolled) 12.dp else 22.dp)
    Column(Modifier.fillMaxWidth().background(bg)) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = if (compact) 20.dp else 48.dp, vertical = vPad),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(Modifier.handClick { onNavigate(Section.Top) }, verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).background(Flat.Ink, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Text("AS", style = display(16.sp, Flat.Yellow))
                }
                if (!compact) {
                    Spacer(Modifier.width(12.dp))
                    Text(Profile.FIRST_NAME, style = display(20.sp))
                    Text(".", style = display(20.sp, Flat.Coral))
                }
            }
            if (!compact) {
                Spacer(Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    listOf(Section.Apps, Section.Websites, Section.Work).forEach {
                        NavLink(it.title, selected = active == it) { onNavigate(it) }
                    }
                    Spacer(Modifier.width(10.dp))
                    ContactChip { onNavigate(Section.Contact) }
                }
            } else {
                // Every section stays reachable on phones; the links scroll sideways if a narrow screen can't fit them.
                Box(Modifier.weight(1f).padding(start = 8.dp), contentAlignment = Alignment.CenterEnd) {
                    Row(Modifier.horizontalScroll(rememberScrollState()), verticalAlignment = Alignment.CenterVertically) {
                        listOf(Section.Apps, Section.Websites, Section.Work).forEach {
                            NavLink(it.title, selected = active == it, compact = true) { onNavigate(it) }
                        }
                    }
                }
                Spacer(Modifier.width(4.dp))
                ContactChip(compact = true) { onNavigate(Section.Contact) }
            }
        }
        // Scroll progress
        Box(Modifier.fillMaxWidth().height(3.dp)) {
            Box(Modifier.fillMaxWidth(progress).height(3.dp).background(if (scrolled) Flat.Coral else Color.Transparent))
        }
    }
}

@Composable
private fun NavLink(text: String, selected: Boolean, compact: Boolean = false, onClick: () -> Unit) {
    val (source, hoveredState) = rememberHoverSource()
    val hovered by hoveredState
    val bg by animateColorAsState(
        when {
            selected -> Flat.Ink
            hovered -> Flat.PaperDeep
            else -> Flat.PaperDeep.copy(alpha = 0f)
        },
    )
    Text(
        text,
        style = body(if (compact) 13.sp else 15.sp, if (selected) Flat.White else Flat.Ink, FontWeight.SemiBold),
        softWrap = false,
        modifier = Modifier
            .hoverable(source)
            .handClick(onClick)
            .background(bg, RoundedCornerShape(50))
            .padding(horizontal = if (compact) 10.dp else 16.dp, vertical = if (compact) 7.dp else 9.dp),
    )
}

@Composable
private fun ContactChip(compact: Boolean = false, onClick: () -> Unit) {
    val (source, hoveredState) = rememberHoverSource()
    val hovered by hoveredState
    val bg by animateColorAsState(if (hovered) Flat.Ink else Flat.Coral)
    Text(
        "Let's talk",
        style = body(if (compact) 13.sp else 15.sp, Flat.White, FontWeight.SemiBold),
        softWrap = false,
        modifier = Modifier
            .widthIn(min = 0.dp)
            .hoverable(source)
            .handClick(onClick)
            .background(bg, RoundedCornerShape(50))
            .padding(horizontal = if (compact) 12.dp else 18.dp, vertical = if (compact) 7.dp else 9.dp),
    )
}
