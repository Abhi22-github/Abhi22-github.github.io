package com.roaa.portfolio.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import portfolio.shared.generated.resources.Res
import portfolio.shared.generated.resources.inter_medium
import portfolio.shared.generated.resources.inter_regular
import portfolio.shared.generated.resources.inter_semibold
import portfolio.shared.generated.resources.spacegrotesk_bold
import portfolio.shared.generated.resources.spacegrotesk_medium

/**
 * Flat palette: solid fills only — no gradients, no shadows, no bevels.
 * Blue and amber are pulled from the PlayBox/Klef and Expense Tracker icons.
 */
object Flat {
    val Paper = Color(0xFFF7F4EC)
    val PaperDeep = Color(0xFFEDE8DA)
    val Ink = Color(0xFF14213D)
    val InkSoft = Color(0xFF4A5671)
    val White = Color(0xFFFFFFFF)

    val Blue = Color(0xFF1F6FFF)
    val BlueLight = Color(0xFF6EA2FF)
    val Amber = Color(0xFFF0A020)
    val Coral = Color(0xFFFF5D5D)
    val Mint = Color(0xFF14C38E)
    val Yellow = Color(0xFFFFCE3A)
    val Violet = Color(0xFF7A5CFF)
    val Sky = Color(0xFFBFD5FF)
}

class PortfolioFonts(val display: FontFamily, val body: FontFamily)

val LocalFonts = staticCompositionLocalOf<PortfolioFonts> { error("Fonts not provided") }

@Composable
fun rememberPortfolioFonts(): PortfolioFonts = PortfolioFonts(
    display = FontFamily(
        Font(Res.font.spacegrotesk_medium, FontWeight.Medium),
        Font(Res.font.spacegrotesk_bold, FontWeight.Bold),
    ),
    body = FontFamily(
        Font(Res.font.inter_regular, FontWeight.Normal),
        Font(Res.font.inter_medium, FontWeight.Medium),
        Font(Res.font.inter_semibold, FontWeight.SemiBold),
    ),
)

@Composable
fun display(size: TextUnit, color: Color = Flat.Ink, weight: FontWeight = FontWeight.Bold) = TextStyle(
    fontFamily = LocalFonts.current.display,
    fontWeight = weight,
    fontSize = size,
    lineHeight = size * 1.05f,
    letterSpacing = (-0.02).em,
    color = color,
)

@Composable
fun body(size: TextUnit = 16.sp, color: Color = Flat.InkSoft, weight: FontWeight = FontWeight.Normal) = TextStyle(
    fontFamily = LocalFonts.current.body,
    fontWeight = weight,
    fontSize = size,
    lineHeight = size * 1.6f,
    color = color,
)

@Composable
fun label(size: TextUnit = 13.sp, color: Color = Flat.Ink) = TextStyle(
    fontFamily = LocalFonts.current.display,
    fontWeight = FontWeight.Bold,
    fontSize = size,
    letterSpacing = 0.08.em,
    color = color,
)

@Composable
fun PortfolioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Flat.Blue,
            background = Flat.Paper,
            surface = Flat.Paper,
            onBackground = Flat.Ink,
            onSurface = Flat.Ink,
        ),
        content = content,
    )
}
