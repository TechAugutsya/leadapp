package com.vymo.leadapp.designsystem.atoms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vymo.leadapp.designsystem.tokens.DesignTokens

/**
 * Button atom for primary actions.
 * Controlled primitive. Does not know domain concepts.
 */
@Composable
fun ButtonAtom(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(DesignTokens.CornerRadius.Medium),
        colors = ButtonDefaults.buttonColors(
            containerColor = DesignTokens.Colors.Primary,
            contentColor = DesignTokens.Colors.OnPrimary,
            disabledContainerColor = DesignTokens.Colors.Border,
            disabledContentColor = DesignTokens.Colors.TextMuted
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (leadingIcon != null) {
            leadingIcon()
        }
        Text(
            text = text,
            style = DesignTokens.Typography.Button
        )
    }
}
