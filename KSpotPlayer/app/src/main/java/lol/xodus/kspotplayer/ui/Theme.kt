package lol.xodus.kspotplayer.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import lol.xodus.kspotplayer.R


/*
* Sources [MidContrast]
* Primary: #BA1A1A
* Secondary: #BA8A1A
* Tertiary: #BA1A5D
* Error: #BA1A1A
* Neutral: #FBE3E3
* Neutral Variant: #EABFBF
* */

val primaryLight = Color(0xFF5E231E)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFA25850)
val onPrimaryContainerLight = Color(0xFFFFFFFF)
val secondaryLight = Color(0xFF483200)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFF8A671C)
val onSecondaryContainerLight = Color(0xFFFFFFFF)
val tertiaryLight = Color(0xFF5C2235)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFF9E586B)
val onTertiaryContainerLight = Color(0xFFFFFFFF)
val errorLight = Color(0xFF5E231E)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFA25850)
val onErrorContainerLight = Color(0xFFFFFFFF)
val backgroundLight = Color(0xFFFFF8F7)
val onBackgroundLight = Color(0xFF231918)
val surfaceLight = Color(0xFFFFF8F7)
val onSurfaceLight = Color(0xFF170F0F)
val surfaceVariantLight = Color(0xFFF4DDDD)
val onSurfaceVariantLight = Color(0xFF413333)
val outlineLight = Color(0xFF5F4F4F)
val outlineVariantLight = Color(0xFF7A6969)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF382E2E)
val inverseOnSurfaceLight = Color(0xFFFFEDEC)
val inversePrimaryLight = Color(0xFFFFB4AB)
val surfaceDimLight = Color(0xFFD3C3C2)
val surfaceBrightLight = Color(0xFFFFF8F7)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFFFF0F0)
val surfaceContainerLight = Color(0xFFF6E4E4)
val surfaceContainerHighLight = Color(0xFFEAD9D9)
val surfaceContainerHighestLight = Color(0xFFDFCECD)

val primaryDark = Color(0xFFFFD2CC)
val onPrimaryDark = Color(0xFF48130F)
val primaryContainerDark = Color(0xFFCC7B72)
val onPrimaryContainerDark = Color(0xFF000000)
val secondaryDark = Color(0xFFFFD78D)
val onSecondaryDark = Color(0xFF332300)
val secondaryContainerDark = Color(0xFFB18B3D)
val onSecondaryContainerDark = Color(0xFF000000)
val tertiaryDark = Color(0xFFFFD1DA)
val onTertiaryDark = Color(0xFF471224)
val tertiaryContainerDark = Color(0xFFC77A8E)
val onTertiaryContainerDark = Color(0xFF000000)
val errorDark = Color(0xFFFFD2CC)
val onErrorDark = Color(0xFF48130F)
val errorContainerDark = Color(0xFFCC7B72)
val onErrorContainerDark = Color(0xFF000000)
val backgroundDark = Color(0xFF1A1110)
val onBackgroundDark = Color(0xFFF1DEDC)
val surfaceDark = Color(0xFF1A1111)
val onSurfaceDark = Color(0xFFFFFFFF)
val surfaceVariantDark = Color(0xFF524343)
val onSurfaceVariantDark = Color(0xFFEED7D7)
val outlineDark = Color(0xFFC2ADAD)
val outlineVariantDark = Color(0xFF9F8C8C)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFF0DEDE)
val inverseOnSurfaceDark = Color(0xFF322828)
val inversePrimaryDark = Color(0xFF74352E)
val surfaceDimDark = Color(0xFF1A1111)
val surfaceBrightDark = Color(0xFF4D4242)
val surfaceContainerLowestDark = Color(0xFF0D0606)
val surfaceContainerLowDark = Color(0xFF251B1C)
val surfaceContainerDark = Color(0xFF2F2526)
val surfaceContainerHighDark = Color(0xFF3B3030)
val surfaceContainerHighestDark = Color(0xFF463B3B)


private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)


@Immutable
data class ExtendedColorScheme(
    val contentColorHigh: Color,
    val contentColorMiddle: Color,
    val contentColorLow: Color,
    val effectShadow1: Color,
    val effectShadow2: Color,
    val effectLight1: Color,
    val effectLight2: Color,
    val effectPowerLight: Color,
)

val MaterialTheme.extendedColorScheme: ExtendedColorScheme
    @Composable
    get () = ExtendedColorScheme(
        contentColorHigh = LocalContentColor.current,
        contentColorMiddle = LocalContentColor.current.copy(alpha = 0.5f),
        contentColorLow = LocalContentColor.current.copy(alpha = 0.25f),
        effectShadow1 = Color(0xFF000000).copy(alpha = 0.5f),
        effectShadow2 = Color(0xFF000000).copy(alpha = 0.25f),
        effectLight1 = Color(0xFFFFFFFF),
        effectLight2 = Color(0xFFFFFFFF).copy(alpha = 0.5f),
        effectPowerLight = Color(0xFF5485F1).copy(alpha = 0.15f),
    )


val NotoSansKR: FontFamily
    @Composable
    get () = FontFamily(
        Font(
            resId = R.font.noto_sans_kr_thin,
            weight = FontWeight.Thin
        ),
        Font(
            resId = R.font.noto_sans_kr_extralight,
            weight = FontWeight.ExtraLight
        ),
        Font(
            resId = R.font.noto_sans_kr_light,
            weight = FontWeight.Light
        ),
        Font(
            resId = R.font.noto_sans_kr_regular,
            weight = FontWeight.Normal
        ),
        Font(
            resId = R.font.noto_sans_kr_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resId = R.font.noto_sans_kr_semibold,
            weight = FontWeight.SemiBold
        ),
        Font(
            resId = R.font.noto_sans_kr_bold,
            weight = FontWeight.Bold
        ),
        Font(
            resId = R.font.noto_sans_kr_extrabold,
            weight = FontWeight.ExtraBold
        ),
        Font(
            resId = R.font.noto_sans_kr_black,
            weight = FontWeight.Black
        ),
    )

val AppTypography: Typography
    @Composable
    get() = Typography().let { baseline ->
        Typography(
            displayLarge = baseline.displayLarge.copy(fontFamily = NotoSansKR),
            displayMedium = baseline.displayMedium.copy(fontFamily = NotoSansKR),
            displaySmall = baseline.displaySmall.copy(fontFamily = NotoSansKR),
            headlineLarge = baseline.headlineLarge.copy(fontFamily = NotoSansKR),
            headlineMedium = baseline.headlineMedium.copy(fontFamily = NotoSansKR),
            headlineSmall = baseline.headlineSmall.copy(fontFamily = NotoSansKR),
            titleLarge = baseline.titleLarge.copy(fontFamily = NotoSansKR),
            titleMedium = baseline.titleMedium.copy(fontFamily = NotoSansKR),
            titleSmall = baseline.titleSmall.copy(fontFamily = NotoSansKR),
            bodyLarge = baseline.bodyLarge.copy(fontFamily = NotoSansKR),
            bodyMedium = baseline.bodyMedium.copy(fontFamily = NotoSansKR),
            bodySmall = baseline.bodySmall.copy(fontFamily = NotoSansKR),
            labelLarge = baseline.labelLarge.copy(fontFamily = NotoSansKR),
            labelMedium = baseline.labelMedium.copy(fontFamily = NotoSansKR),
            labelSmall = baseline.labelSmall.copy(fontFamily = NotoSansKR),
        )
    }


@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() (() -> Unit)
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkScheme else lightScheme,
        typography = AppTypography,
        content = content
    )
}
