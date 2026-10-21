package com.vymo.leadapp.lead.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vymo.leadapp.designsystem.atoms.ButtonAtom
import com.vymo.leadapp.designsystem.tokens.DesignTokens
import com.vymo.leadapp.lead.model.FormFieldConfig

/**
 * Owns the Compact layout (single column, full-width fields) for phone portrait mode.
 * Each field renders vertically. Company name appears/disappears with animation based on config rules.
 */
@Composable
fun LeadFormCompactLayout(
    config: List<FormFieldConfig>,
    values: Map<String, String>,
    getVisibleError: (String) -> String?,
    onFieldValueChange: (String, String) -> Unit,
    onFieldFocusChanged: (String, Boolean) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        config.forEach { field ->
            val isVisible = field.isVisible(values)
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                DynamicFormField(
                    field = field,
                    value = values[field.name] ?: "",
                    error = getVisibleError(field.name),
                    onValueChange = { newValue -> onFieldValueChange(field.name, newValue) },
                    onFocusChanged = { isFocused -> onFieldFocusChanged(field.name, isFocused) }
                )
            }
        }

        Spacer(modifier = Modifier.height(DesignTokens.Spacing.Small))

        ButtonAtom(
            text = "Submit Lead",
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
