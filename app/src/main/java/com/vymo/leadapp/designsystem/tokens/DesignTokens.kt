package com.vymo.leadapp.designsystem.tokens

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Design Tokens for the Lead App Design System.
 * Encapsulates color, spacing, radius, typography, and responsive breakpoints.
 */
object DesignTokens {

    object Colors {
        val Primary = Color(0xFF1E5BB8)
        val PrimaryVariant = Color(0xFF15448C)
        val PrimaryContainer = Color(0xFFE6EFFF)
        val OnPrimary = Color(0xFFFFFFFF)
        val OnPrimaryContainer = Color(0xFF0F2E60)

        val Background = Color(0xFFF8FAFC)
        val Surface = Color(0xFFFFFFFF)
        val SurfaceVariant = Color(0xFFF1F5F9)
        val OnSurface = Color(0xFF0F172A)
        val OnSurfaceVariant = Color(0xFF475569)

        val Border = Color(0xFFCBD5E1)
        val BorderFocused = Color(0xFF1E5BB8)
        val BorderError = Color(0xFFDC2626)

        val TextPrimary = Color(0xFF0F172A)
        val TextSecondary = Color(0xFF475569)
        val TextMuted = Color(0xFF94A3B8)

        val Error = Color(0xFFDC2626)
        val ErrorContainer = Color(0xFFFEE2E2)
        val OnError = Color(0xFFFFFFFF)

        val Success = Color(0xFF16A34A)
        val SuccessContainer = Color(0xFFDCFCE7)
        val OnSuccess = Color(0xFF14532D)
    }

    object Spacing {
        val XSmall: Dp = 4.dp
        val Small: Dp = 8.dp
        val Medium: Dp = 12.dp
        val Large: Dp = 16.dp
        val XLarge: Dp = 20.dp
        val XXLarge: Dp = 24.dp
        val Huge: Dp = 32.dp
    }

    object CornerRadius {
        val Small: Dp = 8.dp
        val Medium: Dp = 12.dp
        val Large: Dp = 16.dp
        val Full: Dp = 999.dp
    }

    object Typography {
        val TitleLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            color = Colors.TextPrimary
        )

        val TitleMedium = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            color = Colors.TextPrimary
        )

        val Label = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Colors.TextPrimary
        )

        val Body = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = Colors.TextPrimary
        )

        val Hint = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Colors.TextMuted
        )

        val Error = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Colors.Error
        )

        val Button = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            color = Colors.OnPrimary
        )
    }

    /**
     * Width threshold where the layout adapts from 1 column (compact) to 2 columns (expanded).
     */
    val TwoColumnBreakpoint: Dp = 600.dp
}
