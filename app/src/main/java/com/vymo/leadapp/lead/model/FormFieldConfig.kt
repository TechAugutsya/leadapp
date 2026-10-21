package com.vymo.leadapp.lead.model

/**
 * Supported field types for the dynamic form.
 */
enum class FieldType {
    TEXT,
    EMAIL,
    SELECT,
    TEXTAREA,
    CHECKBOX
}

/**
 * Configuration for a single dynamic form field.
 * Adding a new field to the form only requires adding an entry of this type.
 */
data class FormFieldConfig(
    val name: String,
    val type: FieldType,
    val label: String,
    val hint: String? = null,
    val placeholder: String? = null,
    val required: Boolean = false,
    val options: List<String> = emptyList(),
    val maxLength: Int? = null,
    val visibleWhen: ((values: Map<String, String>) -> Boolean)? = null,
    val customValidation: ((value: String, allValues: Map<String, String>) -> String?)? = null
) {
    /**
     * Determines if this field should be rendered and validated given current form values.
     */
    fun isVisible(values: Map<String, String>): Boolean {
        return visibleWhen?.invoke(values) ?: true
    }
}
