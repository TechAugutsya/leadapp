package com.vymo.leadapp.lead.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.vymo.leadapp.designsystem.atoms.CheckboxAtom
import com.vymo.leadapp.designsystem.atoms.SelectAtom
import com.vymo.leadapp.designsystem.atoms.TextAreaAtom
import com.vymo.leadapp.designsystem.atoms.TextFieldAtom
import com.vymo.leadapp.designsystem.molecules.FieldMolecule
import com.vymo.leadapp.designsystem.tokens.DesignTokens
import com.vymo.leadapp.lead.model.FieldType
import com.vymo.leadapp.lead.model.FormFieldConfig
import com.vymo.leadapp.lead.model.LeadFormConfig

/**
 * Maps a single [FormFieldConfig] to the design system [FieldMolecule] and [Atom].
 * Completely decoupled: takes configuration, current value, visible error, and callbacks.
 */
@Composable
fun DynamicFormField(
    field: FormFieldConfig,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    when (field.type) {
        FieldType.CHECKBOX -> {
            FieldMolecule(
                modifier = modifier,
                label = null, // Checkbox atom renders label inline
                isRequired = false,
                hint = field.hint,
                error = error
            ) {
                CheckboxAtom(
                    checked = value == "true",
                    onCheckedChange = { checked ->
                        onValueChange(checked.toString())
                        onFocusChanged(false)
                    },
                    label = field.label,
                    isError = !error.isNullOrBlank()
                )
            }
        }

        FieldType.SELECT -> {
            FieldMolecule(
                modifier = modifier,
                label = field.label,
                isRequired = field.required,
                hint = field.hint,
                error = error
            ) {
                SelectAtom(
                    selectedValue = value.ifBlank { null },
                    options = field.options,
                    onValueSelected = onValueChange,
                    placeholder = field.placeholder ?: "Select ${field.label}",
                    isError = !error.isNullOrBlank(),
                    onFocusChanged = onFocusChanged
                )
            }
        }

        FieldType.TEXTAREA -> {
            FieldMolecule(
                modifier = modifier,
                label = field.label,
                isRequired = field.required,
                hint = field.hint,
                error = error
            ) {
                TextAreaAtom(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = field.placeholder,
                    isError = !error.isNullOrBlank(),
                    maxLength = field.maxLength,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default
                    ),
                    onFocusChanged = onFocusChanged
                )
            }
        }

        FieldType.EMAIL -> {
            FieldMolecule(
                modifier = modifier,
                label = field.label,
                isRequired = field.required,
                hint = field.hint,
                error = error
            ) {
                TextFieldAtom(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = field.placeholder,
                    isError = !error.isNullOrBlank(),
                    maxLength = field.maxLength,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Email,
                            contentDescription = null,
                            tint = DesignTokens.Colors.TextMuted
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    onFocusChanged = onFocusChanged
                )
            }
        }

        FieldType.TEXT -> {
            val isPhone = field.name == LeadFormConfig.FIELD_PHONE
            val isCompany = field.name == LeadFormConfig.FIELD_COMPANY_NAME

            FieldMolecule(
                modifier = modifier,
                label = field.label,
                isRequired = field.required,
                hint = field.hint,
                error = error
            ) {
                TextFieldAtom(
                    value = value,
                    onValueChange = { input ->
                        if (isPhone) {
                            val digitsOnly = input.filter { it.isDigit() }
                            if (digitsOnly.length <= 10) {
                                onValueChange(digitsOnly)
                            }
                        } else {
                            onValueChange(input)
                        }
                    },
                    placeholder = field.placeholder,
                    isError = !error.isNullOrBlank(),
                    maxLength = field.maxLength,
                    leadingIcon = {
                        Icon(
                            imageVector = when {
                                isPhone -> Icons.Rounded.Phone
                                isCompany -> Icons.Rounded.Business
                                else -> Icons.Rounded.Person
                            },
                            contentDescription = null,
                            tint = DesignTokens.Colors.TextMuted
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (isPhone) KeyboardType.Phone else KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    onFocusChanged = onFocusChanged
                )
            }
        }
    }
}
