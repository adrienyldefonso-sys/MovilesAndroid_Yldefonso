package com.tecsup.catalogo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

val Typography = Typography(
    displayLarge = Typography().displayLarge.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold),
    displayMedium = Typography().displayMedium.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold),
    displaySmall = Typography().displaySmall.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold),
    titleLarge = Typography().titleLarge.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold),
    titleMedium = Typography().titleMedium.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold),
    titleSmall = Typography().titleSmall.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium),
    bodyLarge = Typography().bodyLarge.copy(fontFamily = FontFamily.Default),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = FontFamily.Default),
    bodySmall = Typography().bodySmall.copy(fontFamily = FontFamily.Default),
    labelLarge = Typography().labelLarge.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium),
    labelMedium = Typography().labelMedium.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium),
    labelSmall = Typography().labelSmall.copy(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium)
)
