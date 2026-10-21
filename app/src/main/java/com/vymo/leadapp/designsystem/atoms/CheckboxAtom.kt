package com.vymo.leadapp.designsystem.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.vymo.leadapp.designsystem.tokens.DesignTokens

/**
 * Checkbox atom with integrated label and click target.
 * Controlled primitive. Does not know domain concepts.
 */
@Composable
fun CheckboxAtom(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Checkbox,
                onClick = { onCheckedChange(!checked) }
            )
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = if (enabled) onCheckedChange else null,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = if (isError) DesignTokens.Colors.Error else DesignTokens.Colors.Primary,
                uncheckedColor = if (isError) DesignTokens.Colors.Error else DesignTokens.Colors.Border,
                checkmarkColor = DesignTokens.Colors.OnPrimary
            )
        )
        Spacer(modifier = Modifier.width(DesignTokens.Spacing.Small))
        Text(
            text = label,
            style = DesignTokens.Typography.Body.copy(
                color = if (isError) DesignTokens.Colors.Error else DesignTokens.Colors.TextPrimary
            )
        )
    }
}
