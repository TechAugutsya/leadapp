package com.vymo.leadapp.lead.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vymo.leadapp.designsystem.tokens.DesignTokens
import com.vymo.leadapp.lead.model.FormFieldConfig

/**
 * Form composable that takes the config and renders one field per entry.
 * Selects between compact (1 column) and expanded (2 columns) based on available width.
 */
@Composable
fun LeadForm(
    config: List<FormFieldConfig>,
    values: Map<String, String>,
    getVisibleError: (String) -> String?,
    onFieldValueChange: (String, String) -> Unit,
    onFieldFocusChanged: (String, Boolean) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val isExpanded = maxWidth >= DesignTokens.TwoColumnBreakpoint

        if (isExpanded) {
            LeadFormExpandedLayout(
                config = config,
                values = values,
                getVisibleError = getVisibleError,
                onFieldValueChange = onFieldValueChange,
                onFieldFocusChanged = onFieldFocusChanged,
                onSubmit = onSubmit
            )
        } else {
            LeadFormCompactLayout(
                config = config,
                values = values,
                getVisibleError = getVisibleError,
                onFieldValueChange = onFieldValueChange,
                onFieldFocusChanged = onFieldFocusChanged,
                onSubmit = onSubmit
            )
        }
    }
}
