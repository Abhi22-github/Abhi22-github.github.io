package com.roaa.portfolio

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import com.roaa.portfolio.sections.AppsSection
import com.roaa.portfolio.sections.ContactSection
import com.roaa.portfolio.sections.Footer
import com.roaa.portfolio.sections.HeroSection
import com.roaa.portfolio.sections.NavBar
import com.roaa.portfolio.sections.Section
import com.roaa.portfolio.sections.SkillsMarquee
import com.roaa.portfolio.sections.WebsitesSection
import com.roaa.portfolio.sections.WorkSection
import com.roaa.portfolio.theme.Flat
import com.roaa.portfolio.theme.LocalFonts
import com.roaa.portfolio.theme.PortfolioTheme
import com.roaa.portfolio.theme.rememberPortfolioFonts
import kotlinx.coroutines.launch

private val MaxContentWidth = 1240.dp

@Composable
fun App() {
    PortfolioTheme {
        CompositionLocalProvider(LocalFonts provides rememberPortfolioFonts()) {
            Portfolio()
        }
    }
}

@Composable
private fun Portfolio() {
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    // Y offset (px) of each section inside the scrolling column.
    val anchors = remember { mutableStateMapOf<Section, Int>() }

    val scrolled by remember { derivedStateOf { scroll.value > 24 } }
    val progress by remember {
        derivedStateOf { if (scroll.maxValue in 1 until Int.MAX_VALUE) scroll.value.toFloat() / scroll.maxValue else 0f }
    }
    val active by remember {
        derivedStateOf {
            val probe = scroll.value + 240
            Section.entries.lastOrNull { (anchors[it] ?: Int.MAX_VALUE) <= probe } ?: Section.Top
        }
    }

    fun navigate(section: Section) {
        scope.launch {
            val target = if (section == Section.Top) 0 else (anchors[section] ?: return@launch) - 90
            scroll.animateScrollTo(target.coerceAtLeast(0), tween(900, easing = FastOutSlowInEasing))
        }
    }

    fun Modifier.anchor(section: Section) = onGloballyPositioned { anchors[section] = it.positionInParent().y.toInt() }

    BoxWithConstraints(Modifier.fillMaxSize().background(Flat.Paper)) {
        val compact = maxWidth < 760.dp
        val columns = when {
            maxWidth < 760.dp -> 1
            maxWidth < 1100.dp -> 2
            else -> 4
        }
        val gutter = if (compact) 20.dp else 48.dp
        val gap = if (compact) 96.dp else 150.dp

        Column(
            Modifier.fillMaxSize().verticalScroll(scroll),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(Modifier.widthIn(max = MaxContentWidth).anchor(Section.Top)) {
                HeroSection(
                    compact = compact,
                    onExploreApps = { navigate(Section.Apps) },
                    onContact = { navigate(Section.Contact) },
                )
            }
            SkillsMarquee(compact)
            Spacer(Modifier.height(gap))

            val contentModifier = Modifier.widthIn(max = MaxContentWidth).fillMaxWidth().padding(horizontal = gutter)
            Box(contentModifier.anchor(Section.Apps)) { AppsSection(compact) }
            Spacer(Modifier.height(gap))
            Box(contentModifier.anchor(Section.Websites)) { WebsitesSection(compact) }
            Spacer(Modifier.height(gap))
            Box(contentModifier.anchor(Section.Work)) { WorkSection(compact, columns) }
            Spacer(Modifier.height(gap))
            Box(contentModifier.anchor(Section.Contact)) { ContactSection(compact) }
            Spacer(Modifier.height(48.dp))
            Box(contentModifier) { Footer(compact, onBackToTop = { navigate(Section.Top) }) }
            Spacer(Modifier.height(40.dp))
        }

        NavBar(
            compact = compact,
            scrolled = scrolled,
            progress = progress,
            active = active,
            onNavigate = ::navigate,
        )
    }
}
