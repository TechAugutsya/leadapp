package com.vymo.leadapp

import com.vymo.leadapp.lead.model.LeadFormConfig
import com.vymo.leadapp.lead.viewmodel.LeadFormViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LeadFormViewModelTest {

    @Test
    fun `initial state has empty fields and no errors visible before submit`() {
        val viewModel = LeadFormViewModel()
        val state = viewModel.uiState.value

        assertEquals("", state.values[LeadFormConfig.FIELD_FULL_NAME])
        assertEquals("false", state.values[LeadFormConfig.FIELD_CONSENT])
        assertFalse(state.isSubmitted)
        assertFalse(state.submitAttempted)
        // No visible error should be shown initially
        assertNull(state.getVisibleError(LeadFormConfig.FIELD_FULL_NAME))
    }

    @Test
    fun `focus loss triggers visible error for invalid field`() {
        val viewModel = LeadFormViewModel()
        
        // Field loses focus while empty
        viewModel.onFieldFocusChanged(LeadFormConfig.FIELD_FULL_NAME, false)

        val state = viewModel.uiState.value
        assertNotNull(state.getVisibleError(LeadFormConfig.FIELD_FULL_NAME))
        // Other fields that have not lost focus should not show errors
        assertNull(state.getVisibleError(LeadFormConfig.FIELD_EMAIL))
    }

    @Test
    fun `reset submission clears all fields back to blank and resets errors`() {
        val viewModel = LeadFormViewModel()

        // Fill out some fields
        viewModel.onFieldValueChange(LeadFormConfig.FIELD_FULL_NAME, "Jane Doe")
        viewModel.onFieldValueChange(LeadFormConfig.FIELD_EMAIL, "jane@example.com")
        viewModel.onFieldValueChange(LeadFormConfig.FIELD_LEAD_TYPE, LeadFormConfig.LEAD_TYPE_INDIVIDUAL)
        viewModel.onFieldValueChange(LeadFormConfig.FIELD_PHONE, "9876543210")
        viewModel.onFieldValueChange(LeadFormConfig.FIELD_CONSENT, "true")

        // Submit successfully
        viewModel.onSubmit()
        assertTrue(viewModel.uiState.value.isSubmitted)
        assertEquals("Jane Doe", viewModel.uiState.value.submittedValues[LeadFormConfig.FIELD_FULL_NAME])

        // Tap "Capture Another Lead"
        viewModel.onResetSubmission()

        val resetState = viewModel.uiState.value
        assertFalse(resetState.isSubmitted)
        assertFalse(resetState.submitAttempted)
        assertTrue(resetState.submittedValues.isEmpty())
        assertTrue(resetState.touchedFields.isEmpty())

        // All fields should be blank
        assertEquals("", resetState.values[LeadFormConfig.FIELD_FULL_NAME])
        assertEquals("", resetState.values[LeadFormConfig.FIELD_EMAIL])
        assertEquals("", resetState.values[LeadFormConfig.FIELD_LEAD_TYPE])
        assertEquals("", resetState.values[LeadFormConfig.FIELD_PHONE])
        assertEquals("", resetState.values[LeadFormConfig.FIELD_NOTES])
        assertEquals("false", resetState.values[LeadFormConfig.FIELD_CONSENT])

        // Errors should NOT be visible to the user on re-capture
        assertNull(resetState.getVisibleError(LeadFormConfig.FIELD_FULL_NAME))
        assertNull(resetState.getVisibleError(LeadFormConfig.FIELD_EMAIL))
        assertNull(resetState.getVisibleError(LeadFormConfig.FIELD_PHONE))
    }

    @Test
    fun `submitting invalid form blocks submission and reveals every current error`() {
        val viewModel = LeadFormViewModel()

        // Form is initially empty and invalid. No errors shown yet.
        assertNull(viewModel.uiState.value.getVisibleError(LeadFormConfig.FIELD_FULL_NAME))
        assertNull(viewModel.uiState.value.getVisibleError(LeadFormConfig.FIELD_EMAIL))
        assertNull(viewModel.uiState.value.getVisibleError(LeadFormConfig.FIELD_LEAD_TYPE))
        assertNull(viewModel.uiState.value.getVisibleError(LeadFormConfig.FIELD_PHONE))
        assertNull(viewModel.uiState.value.getVisibleError(LeadFormConfig.FIELD_CONSENT))

        // Attempt submit
        viewModel.onSubmit()

        val state = viewModel.uiState.value
        // Submission is blocked
        assertFalse(state.isSubmitted)
        assertTrue(state.submitAttempted)

        // ALL errors are now visible under every invalid field
        assertNotNull(state.getVisibleError(LeadFormConfig.FIELD_FULL_NAME))
        assertNotNull(state.getVisibleError(LeadFormConfig.FIELD_EMAIL))
        assertNotNull(state.getVisibleError(LeadFormConfig.FIELD_LEAD_TYPE))
        assertNotNull(state.getVisibleError(LeadFormConfig.FIELD_PHONE))
        assertNotNull(state.getVisibleError(LeadFormConfig.FIELD_CONSENT))

        // Optional fields produce no errors
        assertNull(state.getVisibleError(LeadFormConfig.FIELD_NOTES))
        // Company name is not visible for empty lead type, so no error
        assertNull(state.getVisibleError(LeadFormConfig.FIELD_COMPANY_NAME))
    }
}
