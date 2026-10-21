package com.vymo.leadapp.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import com.vymo.leadapp.designsystem.tokens.DesignTokens

/**
 * Select atom for picking one item from a list of options.
 * Controlled primitive. Does not know domain concepts.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAtom(
    selectedValue: String?,
    options: List<String>,
    onValueSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Select an option",
    isError: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var hasBeenOpened by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                focusManager.clearFocus()
                expanded = !expanded
                if (expanded) {
                    hasBeenOpened = true
                    onFocusChanged?.invoke(true)
                } else if (hasBeenOpened) {
                    onFocusChanged?.invoke(false)
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedValue ?: "",
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            isError = isError,
            placeholder = {
                Text(
                    text = placeholder,
                    style = DesignTokens.Typography.Body.copy(color = DesignTokens.Colors.TextMuted)
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            shape = RoundedCornerShape(DesignTokens.CornerRadius.Medium),
            textStyle = DesignTokens.Typography.Body,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DesignTokens.Colors.BorderFocused,
                unfocusedBorderColor = DesignTokens.Colors.Border,
                errorBorderColor = DesignTokens.Colors.BorderError,
                disabledBorderColor = DesignTokens.Colors.Border,
                focusedContainerColor = DesignTokens.Colors.Surface,
                unfocusedContainerColor = DesignTokens.Colors.Surface,
                disabledContainerColor = DesignTokens.Colors.SurfaceVariant,
                errorContainerColor = DesignTokens.Colors.Surface,
                cursorColor = DesignTokens.Colors.Primary,
                focusedTextColor = DesignTokens.Colors.TextPrimary,
                unfocusedTextColor = DesignTokens.Colors.TextPrimary,
                disabledTextColor = DesignTokens.Colors.TextMuted
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            interactionSource = interactionSource
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                if (hasBeenOpened) {
                    onFocusChanged?.invoke(false)
                }
            },
            modifier = Modifier
                .background(DesignTokens.Colors.Surface)
                .exposedDropdownSize()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = DesignTokens.Typography.Body,
                            color = DesignTokens.Colors.TextPrimary
                        )
                    },
                    onClick = {
                        onValueSelected(option)
                        expanded = false
                        if (hasBeenOpened) {
                            onFocusChanged?.invoke(false)
                        }
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = DesignTokens.Colors.TextPrimary
                    )
                )
            }
        }
    }
}
