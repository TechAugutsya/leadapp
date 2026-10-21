package com.vymo.leadapp.lead.model

/**
 * Lead capture form specification.
 * Contains the field definitions, options, and constraints.
 */
object LeadFormConfig {

    const val FIELD_FULL_NAME = "fullName"
    const val FIELD_EMAIL = "email"
    const val FIELD_LEAD_TYPE = "leadType"
    const val FIELD_COMPANY_NAME = "companyName"
    const val FIELD_PHONE = "phone"
    const val FIELD_NOTES = "notes"
    const val FIELD_CONSENT = "consent"

    const val LEAD_TYPE_INDIVIDUAL = "Individual"
    const val LEAD_TYPE_COMPANY = "Company"

    val fields: List<FormFieldConfig> = listOf(
        FormFieldConfig(
            name = FIELD_FULL_NAME,
            type = FieldType.TEXT,
            label = "Full name",
            placeholder = "Enter your full name",
            required = true
        ),
        FormFieldConfig(
            name = FIELD_EMAIL,
            type = FieldType.EMAIL,
            label = "Email address",
            placeholder = "name@example.com",
            required = true
        ),
        FormFieldConfig(
            name = FIELD_LEAD_TYPE,
            type = FieldType.SELECT,
            label = "Lead type",
            placeholder = "Select lead type",
            options = listOf(LEAD_TYPE_INDIVIDUAL, LEAD_TYPE_COMPANY),
            required = true
        ),
        FormFieldConfig(
            name = FIELD_COMPANY_NAME,
            type = FieldType.TEXT,
            label = "Company name",
            placeholder = "Enter company name",
            required = true,
            visibleWhen = { values -> values[FIELD_LEAD_TYPE] == LEAD_TYPE_COMPANY }
        ),
        FormFieldConfig(
            name = FIELD_PHONE,
            type = FieldType.TEXT,
            label = "Phone number",
            placeholder = "10-digit mobile number",
            hint = "Format: exactly 10 digits",
            required = true,
            maxLength = 10
        ),
        FormFieldConfig(
            name = FIELD_NOTES,
            type = FieldType.TEXTAREA,
            label = "Notes",
            placeholder = "Additional requirements or notes...",
            hint = "Optional context for our team",
            required = false,
            maxLength = 200
        ),
        FormFieldConfig(
            name = FIELD_CONSENT,
            type = FieldType.CHECKBOX,
            label = "I consent to receive contact regarding this inquiry.",
            required = true
        )
    )
}
