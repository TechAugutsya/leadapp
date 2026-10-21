package com.vymo.leadapp.lead.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vymo.leadapp.designsystem.atoms.ButtonAtom
import com.vymo.leadapp.designsystem.tokens.DesignTokens
import com.vymo.leadapp.lead.model.FormFieldConfig
import com.vymo.leadapp.lead.model.LeadFormConfig

/**
 * Owns the Expanded layout (two columns) for tablets or landscape orientation.
 * - Full name and Email share a row
 * - Lead type and Company name share a row (Company name conditionally rendered)
 * - Phone occupies the left column
 * - Notes and Consent span full width
 */
@Composable
fun LeadFormExpandedLayout(
    config: List<FormFieldConfig>,
    values: Map<String, String>,
    getVisibleError: (String) -> String?,
    onFieldValueChange: (String, String) -> Unit,
    onFieldFocusChanged: (String, Boolean) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fieldMap = config.associateBy { it.name }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Row 1: Full name and Email share a row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.Large)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                RenderConfigField(
                    name = LeadFormConfig.FIELD_FULL_NAME,
                    fieldMap = fieldMap,
                    values = values,
                    getVisibleError = getVisibleError,
                    onFieldValueChange = onFieldValueChange,
                    onFieldFocusChanged = onFieldFocusChanged
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                RenderConfigField(
                    name = LeadFormConfig.FIELD_EMAIL,
                    fieldMap = fieldMap,
                    values = values,
                    getVisibleError = getVisibleError,
                    onFieldValueChange = onFieldValueChange,
                    onFieldFocusChanged = onFieldFocusChanged
                )
            }
        }

        // Row 2: Lead type & Company name
        val companyField = fieldMap[LeadFormConfig.FIELD_COMPANY_NAME]
        val isCompanyVisible = companyField?.isVisible(values) == true
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.Large)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                RenderConfigField(
                    name = LeadFormConfig.FIELD_LEAD_TYPE,
                    fieldMap = fieldMap,
                    values = values,
                    getVisibleError = getVisibleError,
                    onFieldValueChange = onFieldValueChange,
                    onFieldFocusChanged = onFieldFocusChanged
                )
            }

            if (isCompanyVisible) {
                Column(modifier = Modifier.weight(1f)) {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        RenderConfigField(
                            name = LeadFormConfig.FIELD_COMPANY_NAME,
                            fieldMap = fieldMap,
                            values = values,
                            getVisibleError = getVisibleError,
                            onFieldValueChange = onFieldValueChange,
                            onFieldFocusChanged = onFieldFocusChanged
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Row 3: Phone number
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.Large)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                RenderConfigField(
                    name = LeadFormConfig.FIELD_PHONE,
                    fieldMap = fieldMap,
                    values = values,
                    getVisibleError = getVisibleError,
                    onFieldValueChange = onFieldValueChange,
                    onFieldFocusChanged = onFieldFocusChanged
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        // Row 4: Notes (spans full width)
        RenderConfigField(
            name = LeadFormConfig.FIELD_NOTES,
            fieldMap = fieldMap,
            values = values,
            getVisibleError = getVisibleError,
            onFieldValueChange = onFieldValueChange,
            onFieldFocusChanged = onFieldFocusChanged
        )

        // Row 5: Consent (spans full width)
        RenderConfigField(
            name = LeadFormConfig.FIELD_CONSENT,
            fieldMap = fieldMap,
            values = values,
            getVisibleError = getVisibleError,
            onFieldValueChange = onFieldValueChange,
            onFieldFocusChanged = onFieldFocusChanged
        )

        Spacer(modifier = Modifier.height(DesignTokens.Spacing.Medium))

        // Submit button
        ButtonAtom(
            text = "Submit Lead",
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RenderConfigField(
    name: String,
    fieldMap: Map<String, FormFieldConfig>,
    values: Map<String, String>,
    getVisibleError: (String) -> String?,
    onFieldValueChange: (String, String) -> Unit,
    onFieldFocusChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val field = fieldMap[name] ?: return
    DynamicFormField(
        field = field,
        value = values[name] ?: "",
        error = getVisibleError(name),
        onValueChange = { newValue -> onFieldValueChange(name, newValue) },
        onFocusChanged = { isFocused -> onFieldFocusChanged(name, isFocused) },
        modifier = modifier
    )
}
