package com.roaa.portfolio.data

import androidx.compose.ui.graphics.Color
import com.roaa.portfolio.theme.Flat
import org.jetbrains.compose.resources.DrawableResource
import portfolio.shared.generated.resources.Res
import portfolio.shared.generated.resources.icon_expensetracker
import portfolio.shared.generated.resources.icon_klef
import portfolio.shared.generated.resources.icon_playbox

/**
 * All site content lives here. To add an app, website or project,
 * append an entry to the matching list below — the UI picks it up automatically.
 */
object Profile {
    const val NAME = "Abhishek Satpute"
    const val FIRST_NAME = "Abhishek"
    const val EMAIL = "satpute.abhi22@gmail.com"
    const val GITHUB = "https://github.com/Abhi22-github"
    const val LINKEDIN = "https://www.linkedin.com/in/abhishek-satpute"
    const val PLAY_STORE = "https://play.google.com/store/apps/developer?id=ROAA"
    const val PUBLIC_REPOS = 20

    /** Served from webApp resources, next to index.html. */
    const val RESUME = "Abhishek_Satpute_Resume.pdf"

    val roles = listOf(
        "Android Developer",
        "Self-Taught Developer",
        "Problem Solver",
        "Curious Technologist",
    )

    const val INTRO =
        "I build Android apps that people actually use — taking each one from a rough idea " +
            "to a polished product on Google Play. I care about clean design, speed and privacy, " +
            "and I love turning everyday problems into simple tools that just work."

    /** Grouped as on the résumé; each group gets one accent colour in the skills band. */
    val skills = listOf(
        SkillGroup("Languages", Flat.Yellow, listOf("Kotlin", "Java", "Swift")),
        SkillGroup(
            "Android",
            Flat.Mint,
            listOf("Jetpack Compose", "Android SDK", "Kotlin Multiplatform", "Coroutines & Flow", "Hilt", "Dagger 2", "RxJava", "Retrofit", "Gson", "WebSockets"),
        ),
        SkillGroup("Architecture", Flat.Blue, listOf("MVVM", "Clean Architecture", "Modularization", "Offline-first")),
        SkillGroup("Data", Flat.Coral, listOf("Room", "SQLite", "Firebase", "MongoDB", "Realm")),
        SkillGroup("Testing", Flat.Violet, listOf("JUnit", "Mockito", "Espresso", "Compose Testing")),
        SkillGroup(
            "Tools",
            Flat.Amber,
            listOf("Git", "Jenkins CI/CD", "Docker", "Linux & Shell", "Gradle", "Android Studio", "SonarQube", "Jira", "Figma", "UI/UX Design"),
        ),
    )
}

data class SkillGroup(val title: String, val accent: Color, val items: List<String>)

enum class MockKind { VideoPlayer, PasswordVault, ExpenseChart }

data class PlayApp(
    val name: String,
    val packageId: String,
    val tagline: String,
    val description: String,
    val features: List<String>,
    val downloads: String,
    val updated: String,
    val icon: DrawableResource,
    val accent: Color,
    val accentSoft: Color,
    val mock: MockKind,
    val sourceUrl: String?,
) {
    val playUrl get() = "https://play.google.com/store/apps/details?id=$packageId"
}

val playApps = listOf(
    PlayApp(
        name = "PlayBox",
        packageId = "com.roaa.playbox",
        tagline = "Your smart, offline video player.",
        description = "Watch and manage every local video with a smooth, organised, distraction-free " +
            "player. Built on Media3/ExoPlayer with Jetpack Compose — no internet, no tracking.",
        features = listOf("Smart folder browsing", "Smooth HD playback", "All popular formats", "Instant thumbnails", "100% offline"),
        downloads = "1K+",
        updated = "Mar 2026",
        icon = Res.drawable.icon_playbox,
        accent = Flat.Blue,
        accentSoft = Flat.Sky,
        mock = MockKind.VideoPlayer,
        sourceUrl = "https://github.com/Abhi22-github/ExoPlayer",
    ),
    PlayApp(
        name = "Klef Password Manager",
        packageId = "com.roaa.klef",
        tagline = "Passwords, passkeys & OTPs — locked tight.",
        description = "An encrypted vault for passwords, passkeys, 2FA codes and private notes, with " +
            "fast autofill across apps and websites and biometric unlock.",
        features = listOf("Encrypted vault", "Autofill", "Passkeys", "OTP / 2FA", "Biometric unlock", "Backup & restore"),
        downloads = "10+",
        updated = "Jun 2026",
        icon = Res.drawable.icon_klef,
        accent = Flat.Violet,
        accentSoft = Color(0xFFDCD3FF),
        mock = MockKind.PasswordVault,
        sourceUrl = null,
    ),
    PlayApp(
        name = "Expense Tracker",
        packageId = "com.roaa.expensetracker",
        tagline = "See exactly where your money goes.",
        description = "Log transactions in a few taps, set budgets, and get clear charts by day, week " +
            "or month. Banks and categories keep it organised; everything stays on your device.",
        features = listOf("Quick logging", "Smart budgets", "Visual insights", "Bank & category management", "Local backups"),
        downloads = "50+",
        updated = "Oct 2026",
        icon = Res.drawable.icon_expensetracker,
        accent = Flat.Amber,
        accentSoft = Color(0xFFFFE2B0),
        mock = MockKind.ExpenseChart,
        sourceUrl = "https://github.com/Abhi22-github/SpendTracker",
    ),
)

data class Website(
    val name: String,
    val url: String,
    val displayUrl: String,
    val description: String,
    val stack: List<String>,
    val accent: Color,
)

val websites = listOf(
    Website(
        name = "RepoDashboard",
        url = "https://multirepodashboard.vercel.app",
        displayUrl = "multirepodashboard.vercel.app",
        description = "A dashboard for tracking many repositories in one place — a Kotlin Multiplatform " +
            "project that ships to both Android and the web.",
        stack = listOf("Kotlin Multiplatform", "Compose Web", "Vercel"),
        accent = Flat.Mint,
    ),
    Website(
        name = "This Portfolio",
        url = "https://abhi22-github.github.io/",
        displayUrl = "abhi22-github.github.io",
        description = "The site you're on — a single-page portfolio built with Compose " +
            "Multiplatform and compiled to WebAssembly, with every animation hand-crafted.",
        stack = listOf("Compose Multiplatform", "Kotlin/Wasm", "GitHub Pages"),
        accent = Flat.Coral,
    ),
)

data class Project(
    val name: String,
    val description: String,
    val tags: List<String>,
    val url: String,
    val color: Color,
)

val projects = listOf(
    Project(
        "ComposeMultiPlatform",
        "One Compose Multiplatform codebase running on Android, iOS, desktop and the web.",
        listOf("KMP", "Compose"),
        "https://github.com/Abhi22-github/ComposeMultiPlatform",
        Flat.Blue,
    ),
    Project(
        "DocScanner",
        "Scan paper documents with the camera and turn them into clean digital copies.",
        listOf("Android", "Camera"),
        "https://github.com/Abhi22-github/DocScanner",
        Flat.Mint,
    ),
    Project(
        "SmartGitCommit",
        "A tool that helps write cleaner, more meaningful Git commit messages.",
        listOf("Kotlin", "Dev tools"),
        "https://github.com/Abhi22-github/SmartGitCommit",
        Flat.Violet,
    ),
    Project(
        "CallForwarding",
        "Manage call forwarding settings through USSD codes with a simple UI.",
        listOf("Android", "Telephony"),
        "https://github.com/Abhi22-github/CallForwarding",
        Flat.Coral,
    ),
    Project(
        "CleanModularization",
        "Clean Architecture with feature-based modules — Compose, Hilt, Room, Coroutines & Flow.",
        listOf("Architecture", "Hilt"),
        "https://github.com/Abhi22-github/CleanModularization",
        Flat.Amber,
    ),
    Project(
        "StickyNotes",
        "A lightweight notes app for quick thoughts that stick around.",
        listOf("Android", "Room"),
        "https://github.com/Abhi22-github/StickyNotes",
        Flat.Yellow,
    ),
    Project(
        "ComposeNotification",
        "Local notifications from Jetpack Compose, with Android 13+ runtime permission handling.",
        listOf("Android", "Notifications"),
        "https://github.com/Abhi22-github/ComposeNotification",
        Flat.BlueLight,
    ),
    Project(
        "First-Ios-App",
        "My first native iOS app in SwiftUI — state, navigation and an Android dev's take on iOS.",
        listOf("iOS", "SwiftUI"),
        "https://github.com/Abhi22-github/First-Ios-App",
        Flat.Ink,
    ),
)
