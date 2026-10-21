package com.vymo.leadapp.designsystem.atoms

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import com.vymo.leadapp.designsystem.tokens.DesignTokens

/**
 * Text field atom.
 * Controlled primitive with styling from DesignTokens. Does not know domain concepts.
 */
@Composable
fun TextFieldAtom(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    var hasGainedFocus by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            if (maxLength == null || newText.length <= maxLength) {
                onValueChange(newText)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onFocusChanged != null) {
                    Modifier.onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            hasGainedFocus = true
                            onFocusChanged(true)
                        } else if (hasGainedFocus) {
                            onFocusChanged(false)
                        }
                    }
                } else Modifier
            ),
        enabled = enabled,
        singleLine = singleLine,
        isError = isError,
        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    style = DesignTokens.Typography.Body.copy(color = DesignTokens.Colors.TextMuted)
                )
            }
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(DesignTokens.CornerRadius.Medium),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = DesignTokens.Typography.Body,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DesignTokens.Colors.BorderFocused,
            unfocusedBorderColor = DesignTokens.Colors.Border,
            errorBorderColor = DesignTokens.Colors.BorderError,
            focusedContainerColor = DesignTokens.Colors.Surface,
            unfocusedContainerColor = DesignTokens.Colors.Surface,
            errorContainerColor = DesignTokens.Colors.Surface,
            cursorColor = DesignTokens.Colors.Primary
        ),
        interactionSource = interactionSource
    )
}
