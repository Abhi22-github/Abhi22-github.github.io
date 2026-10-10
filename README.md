# Abhishek Satpute — Portfolio

A flat-design portfolio built entirely in Kotlin with Compose Multiplatform, compiled to WebAssembly.

## Editing content

All copy, apps, websites and projects live in
[`PortfolioData.kt`](shared/src/commonMain/kotlin/com/roaa/portfolio/data/PortfolioData.kt).
Add an entry to `playApps`, `websites` or `projects` and the page picks it up.
App icons and fonts live in `shared/src/commonMain/composeResources/`.

| File | What it contains |
| --- | --- |
| `theme/Theme.kt` | Flat colour palette and type styles (Space Grotesk + Inter) |
| `components/Components.kt` | Scroll reveal, flat buttons, tags, section headers |
| `sections/Hero.kt` | Hero with parallax shapes, role rotator and orbiting app icons |
| `sections/AppsSection.kt` + `PhoneMockups.kt` | Play Store showcase with animated phone screens |
| `sections/WebsitesSection.kt` | Website cards with animated browser previews |
| `sections/WorkSection.kt` | Skills marquee and open-source grid |
| `sections/ContactSection.kt` | Contact panel and footer |

## Running

- Dev server: `./gradlew :webApp:wasmJsBrowserDevelopmentRun`
- Production build: `./gradlew :webApp:wasmJsBrowserDistribution` → `webApp/build/dist/wasmJs/productionExecutable`

## Deploying

`.github/workflows/deploy.yml` builds the Wasm site and publishes it to GitHub Pages on every push to `main`
(set **Settings → Pages → Source** to *GitHub Actions*).
