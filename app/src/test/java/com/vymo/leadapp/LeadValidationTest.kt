package com.vymo.leadapp

import com.vymo.leadapp.lead.model.LeadFormConfig
import com.vymo.leadapp.lead.model.validateLeadForm
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LeadValidationTest {

    private val config = LeadFormConfig.fields

    @Test
    fun `empty initial form reports errors for all required visible fields`() {
        val initialValues = mapOf(
            LeadFormConfig.FIELD_FULL_NAME to "",
            LeadFormConfig.FIELD_EMAIL to "",
            LeadFormConfig.FIELD_LEAD_TYPE to "",
            LeadFormConfig.FIELD_COMPANY_NAME to "",
            LeadFormConfig.FIELD_PHONE to "",
            LeadFormConfig.FIELD_NOTES to "",
            LeadFormConfig.FIELD_CONSENT to "false"
        )

        val errors = validateLeadForm(config, initialValues)

        // Required visible fields must have errors
        assertNotNull(errors[LeadFormConfig.FIELD_FULL_NAME])
        assertNotNull(errors[LeadFormConfig.FIELD_EMAIL])
        assertNotNull(errors[LeadFormConfig.FIELD_LEAD_TYPE])
        assertNotNull(errors[LeadFormConfig.FIELD_PHONE])
        assertNotNull(errors[LeadFormConfig.FIELD_CONSENT])

        // Notes is optional -> null error
        assertNull(errors[LeadFormConfig.FIELD_NOTES])

        // Company name is hidden when Lead type is empty -> null error
        assertNull(errors[LeadFormConfig.FIELD_COMPANY_NAME])
    }

    @Test
    fun `company name is required only when lead type is Company`() {
        // Case 1: Individual -> companyName is not visible, no error
        val individualValues = mapOf(
            LeadFormConfig.FIELD_LEAD_TYPE to LeadFormConfig.LEAD_TYPE_INDIVIDUAL,
            LeadFormConfig.FIELD_COMPANY_NAME to ""
        )
        val individualErrors = validateLeadForm(config, individualValues)
        assertNull(individualErrors[LeadFormConfig.FIELD_COMPANY_NAME])

        // Case 2: Company but empty company name -> error
        val companyEmptyValues = mapOf(
            LeadFormConfig.FIELD_LEAD_TYPE to LeadFormConfig.LEAD_TYPE_COMPANY,
            LeadFormConfig.FIELD_COMPANY_NAME to ""
        )
        val companyEmptyErrors = validateLeadForm(config, companyEmptyValues)
        assertNotNull(companyEmptyErrors[LeadFormConfig.FIELD_COMPANY_NAME])

        // Case 3: Company with valid name -> no error
        val companyValidValues = mapOf(
            LeadFormConfig.FIELD_LEAD_TYPE to LeadFormConfig.LEAD_TYPE_COMPANY,
            LeadFormConfig.FIELD_COMPANY_NAME to "Acme Corp"
        )
        val companyValidErrors = validateLeadForm(config, companyValidValues)
        assertNull(companyValidErrors[LeadFormConfig.FIELD_COMPANY_NAME])
    }

    @Test
    fun `validates email format correctly`() {
        val invalidEmails = listOf("invalid", "test@", "@domain.com", "test@domain")
        for (email in invalidEmails) {
            val errors = validateLeadForm(config, mapOf(LeadFormConfig.FIELD_EMAIL to email))
            assertNotNull("Expected error for $email", errors[LeadFormConfig.FIELD_EMAIL])
        }

        val validEmails = listOf("john.doe@example.com", "user@sub.domain.co", "lead+test@corp.org")
        for (email in validEmails) {
            val errors = validateLeadForm(config, mapOf(LeadFormConfig.FIELD_EMAIL to email))
            assertNull("Expected no error for $email", errors[LeadFormConfig.FIELD_EMAIL])
        }
    }

    @Test
    fun `validates phone number must be 10 digits`() {
        val invalidPhones = listOf("123", "123456789", "12345678901", "abcdefghij")
        for (phone in invalidPhones) {
            val errors = validateLeadForm(config, mapOf(LeadFormConfig.FIELD_PHONE to phone))
            assertNotNull("Expected error for $phone", errors[LeadFormConfig.FIELD_PHONE])
        }

        val validErrors = validateLeadForm(config, mapOf(LeadFormConfig.FIELD_PHONE to "9876543210"))
        assertNull(validErrors[LeadFormConfig.FIELD_PHONE])
    }

    @Test
    fun `notes field enforces 200 char max length`() {
        val validNotes = "A".repeat(200)
        val validErrors = validateLeadForm(config, mapOf(LeadFormConfig.FIELD_NOTES to validNotes))
        assertNull(validErrors[LeadFormConfig.FIELD_NOTES])

        val excessiveNotes = "A".repeat(201)
        val excessiveErrors = validateLeadForm(config, mapOf(LeadFormConfig.FIELD_NOTES to excessiveNotes))
        assertNotNull(excessiveErrors[LeadFormConfig.FIELD_NOTES])
    }

    @Test
    fun `consent checkbox requires true to pass`() {
        val falseConsent = validateLeadForm(config, mapOf(LeadFormConfig.FIELD_CONSENT to "false"))
        assertNotNull(falseConsent[LeadFormConfig.FIELD_CONSENT])

        val trueConsent = validateLeadForm(config, mapOf(LeadFormConfig.FIELD_CONSENT to "true"))
        assertNull(trueConsent[LeadFormConfig.FIELD_CONSENT])
    }

    @Test
    fun `fully filled valid form produces zero errors`() {
        val completeValidForm = mapOf(
            LeadFormConfig.FIELD_FULL_NAME to "John Doe",
            LeadFormConfig.FIELD_EMAIL to "john.doe@example.com",
            LeadFormConfig.FIELD_LEAD_TYPE to LeadFormConfig.LEAD_TYPE_COMPANY,
            LeadFormConfig.FIELD_COMPANY_NAME to "Vymo Inc",
            LeadFormConfig.FIELD_PHONE to "9876543210",
            LeadFormConfig.FIELD_NOTES to "Interested in enterprise CRM integration.",
            LeadFormConfig.FIELD_CONSENT to "true"
        )

        val errors = validateLeadForm(config, completeValidForm)
        assertTrue(errors.values.all { it == null })
    }
}
