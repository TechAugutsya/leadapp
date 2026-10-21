package com.vymo.leadapp.lead.viewmodel

import androidx.lifecycle.ViewModel
import com.vymo.leadapp.lead.model.FormFieldConfig
import com.vymo.leadapp.lead.model.LeadFormConfig
import com.vymo.leadapp.lead.model.validateField
import com.vymo.leadapp.lead.model.validateLeadForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * UI State for the Lead Capture Form.
 */
data class LeadFormUiState(
    val values: Map<String, String> = emptyMap(),
    val errors: Map<String, String?> = emptyMap(),
    val touchedFields: Set<String> = emptySet(),
    val isSubmitted: Boolean = false,
    val submittedValues: Map<String, String> = emptyMap(),
    val submitAttempted: Boolean = false
) {
    /**
     * Determines whether an error should be shown to the user.
     * Errors are shown when the field has lost focus (touched) or after submit is attempted.
     */
    fun getVisibleError(fieldName: String): String? {
        return if (submitAttempted || touchedFields.contains(fieldName)) errors[fieldName] else null
    }

    /**
     * Form is valid if every visible field has no error.
     */
    val isValid: Boolean
        get() = errors.values.all { it.isNullOrBlank() }
}

/**
 * ViewModel managing the state and validation of the dynamic lead form.
 */
class LeadFormViewModel(
    val config: List<FormFieldConfig> = LeadFormConfig.fields
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeadFormUiState())
    val uiState: StateFlow<LeadFormUiState> = _uiState.asStateFlow()

    init {
        // Initialize default values for the configured fields
        val initialValues = config.associate { it.name to "" }.toMutableMap()
        initialValues[LeadFormConfig.FIELD_CONSENT] = "false"

        val initialErrors = validateLeadForm(config, initialValues)
        _uiState.value = LeadFormUiState(
            values = initialValues,
            errors = initialErrors
        )
    }

    /**
     * Called when a field's value changes.
     */
    fun onFieldValueChange(fieldName: String, newValue: String) {
        _uiState.update { current ->
            val updatedValues = current.values.toMutableMap().apply {
                put(fieldName, newValue)
            }

            // Recompute validation with the pure function
            val updatedErrors = validateLeadForm(config, updatedValues)

            current.copy(
                values = updatedValues,
                errors = updatedErrors
            )
        }
    }

    /**
     * Called when a field gains or loses focus.
     * On focus loss, mark field as touched so its error is displayed.
     */
    fun onFieldFocusChanged(fieldName: String, isFocused: Boolean) {
        if (!isFocused) {
            _uiState.update { current ->
                current.copy(touchedFields = current.touchedFields + fieldName)
            }
        }
    }

    /**
     * Called when the submit button is clicked.
     * Validates all visible fields and shows errors only after submit.
     */
    fun onSubmit() {
        _uiState.update { current ->
            val allErrors = validateLeadForm(config, current.values)
            val allVisibleFieldNames = config
                .filter { it.isVisible(current.values) }
                .map { it.name }
                .toSet()

            val hasErrors = allErrors.values.any { !it.isNullOrBlank() }

            if (hasErrors) {
                current.copy(
                    errors = allErrors,
                    submitAttempted = true,
                    isSubmitted = false
                )
            } else {
                current.copy(
                    errors = allErrors,
                    submitAttempted = true,
                    isSubmitted = true,
                    submittedValues = current.values.filterKeys { allVisibleFieldNames.contains(it) }
                )
            }
        }
    }

    /**
     * Resets the form to capture another lead.
     * Clears all field values back to blank, resets touched fields, errors, and submission state.
     */
    fun onResetSubmission() {
        val resetValues = config.associate { it.name to "" }.toMutableMap()
        resetValues[LeadFormConfig.FIELD_CONSENT] = "false"
        val resetErrors = validateLeadForm(config, resetValues)

        _uiState.value = LeadFormUiState(
            values = resetValues,
            errors = resetErrors,
            touchedFields = emptySet(),
            isSubmitted = false,
            submittedValues = emptyMap(),
            submitAttempted = false
        )
    }
}
