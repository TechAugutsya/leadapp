package com.vymo.leadapp.designsystem.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vymo.leadapp.designsystem.tokens.DesignTokens

/**
 * Field Molecule:
 * Composes a label (with required indicator), the interactive control (atom),
 * helper hint, and validation error message.
 *
 * It is completely generic and domain-agnostic.
 */
@Composable
fun FieldMolecule(
    modifier: Modifier = Modifier,
    label: String? = null,
    isRequired: Boolean = false,
    hint: String? = null,
    error: String? = null,
    control: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Label with required marker
        if (!label.isNullOrBlank()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = DesignTokens.Spacing.Small)
            ) {
                Text(
                    text = label,
                    style = DesignTokens.Typography.Label
                )
                if (isRequired) {
                    Spacer(modifier = Modifier.width(DesignTokens.Spacing.XSmall))
                    Text(
                        text = "*",
                        style = DesignTokens.Typography.Label.copy(
                            color = DesignTokens.Colors.Error
                        )
                    )
                }
            }
        }

        // Control slot (Atom)
        control()

        // Error message with smooth animated appearance
        AnimatedVisibility(
            visible = !error.isNullOrBlank(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = DesignTokens.Spacing.XSmall, start = DesignTokens.Spacing.XSmall)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ErrorOutline,
                    contentDescription = "Error",
                    tint = DesignTokens.Colors.Error,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(DesignTokens.Spacing.XSmall))
                Text(
                    text = error.orEmpty(),
                    style = DesignTokens.Typography.Error
                )
            }
        }

        // Helper hint (shown when no error is present)
        if (error.isNullOrBlank() && !hint.isNullOrBlank()) {
            Text(
                text = hint,
                style = DesignTokens.Typography.Hint,
                modifier = Modifier.padding(
                    top = DesignTokens.Spacing.XSmall,
                    start = DesignTokens.Spacing.XSmall
                )
            )
        }

        Spacer(modifier = Modifier.height(DesignTokens.Spacing.Large))
    }
}
