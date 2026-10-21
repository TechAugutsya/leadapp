package com.vymo.leadapp.lead.model

/**
 * Pure validation function for dynamic forms.
 *
 * It receives the configuration list and the current values map,
 * and returns a map of field names to their respective error messages (or null if valid).
 *
 * Pure function: No side effects, deterministic, and easy to unit test.
 */
fun validateLeadForm(
    config: List<FormFieldConfig>,
    values: Map<String, String>
): Map<String, String?> {
    val errors = mutableMapOf<String, String?>()

    for (field in config) {
        // If the field is not visible in the current configuration, it produces no error.
        if (!field.isVisible(values)) {
            errors[field.name] = null
            continue
        }

        errors[field.name] = validateField(field, values)
    }

    return errors
}

/**
 * Validates a single field against its constraints and the current form values.
 */
fun validateField(
    field: FormFieldConfig,
    values: Map<String, String>
): String? {
    if (!field.isVisible(values)) {
        return null
    }

    val rawValue = values[field.name] ?: ""
    val trimmedValue = rawValue.trim()

    // 1. Checkbox requirement
    if (field.type == FieldType.CHECKBOX) {
        if (field.required && trimmedValue != "true") {
            return "You must accept to proceed"
        }
        return null
    }

    // 2. Required check for text/select/textarea
    if (field.required && trimmedValue.isEmpty()) {
        return "${field.label} is required"
    }

    // If empty and not required, no further checks needed
    if (trimmedValue.isEmpty()) {
        return null
    }

    // 3. Email validation
    if (field.type == FieldType.EMAIL || field.name == LeadFormConfig.FIELD_EMAIL) {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        if (!emailRegex.matches(trimmedValue)) {
            return "Please enter a valid email address"
        }
    }

    // 4. Phone validation (10 digits, allow common separators)
    if (field.name == LeadFormConfig.FIELD_PHONE) {
        val phoneDigits = trimmedValue.filter { it.isDigit() }
        if (phoneDigits.length != 10) {
            return "Phone number must contain exactly 10 digits"
        }
    }

    // 5. Max length constraint
    if (field.maxLength != null && trimmedValue.length > field.maxLength) {
        return "Must not exceed ${field.maxLength} characters"
    }

    // 6. Custom validation hook if provided
    field.customValidation?.let { customValidator ->
        val error = customValidator(trimmedValue, values)
        if (error != null) return error
    }

    return null
}
