package com.vymo.leadapp.designsystem.atoms

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import com.vymo.leadapp.designsystem.tokens.DesignTokens

/**
 * Text area atom for multi-line text input.
 * Controlled primitive. Does not know domain concepts.
 */
@Composable
fun TextAreaAtom(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    minLines: Int = 3,
    maxLines: Int = 5,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onFocusChanged: ((Boolean) -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    var hasGainedFocus by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
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
            singleLine = false,
            minLines = minLines,
            maxLines = maxLines,
            isError = isError,
            placeholder = placeholder?.let {
                {
                    Text(
                        text = it,
                        style = DesignTokens.Typography.Body.copy(color = DesignTokens.Colors.TextMuted)
                    )
                }
            },
            shape = RoundedCornerShape(DesignTokens.CornerRadius.Medium),
            keyboardOptions = keyboardOptions,
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

        if (maxLength != null) {
            Text(
                text = "${value.length} / $maxLength",
                style = DesignTokens.Typography.Hint.copy(
                    color = if (value.length > maxLength) DesignTokens.Colors.Error else DesignTokens.Colors.TextMuted
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = DesignTokens.Spacing.XSmall)
            )
        }
    }
}
