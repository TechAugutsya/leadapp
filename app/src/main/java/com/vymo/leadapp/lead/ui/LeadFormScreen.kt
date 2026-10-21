package com.vymo.leadapp.lead.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContactMail
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vymo.leadapp.designsystem.atoms.ButtonAtom
import com.vymo.leadapp.designsystem.tokens.DesignTokens
import com.vymo.leadapp.lead.viewmodel.LeadFormViewModel

/**
 * Screen hosting the lead capture flow.
 * Respects system bars, keyboard insets, and adapts layout across screen dimensions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadFormScreen(
    viewModel: LeadFormViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.Background),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DesignTokens.Colors.PrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ContactMail,
                                contentDescription = null,
                                tint = DesignTokens.Colors.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.size(DesignTokens.Spacing.Small))
                        Text(
                            text = "Lead Capture",
                            style = DesignTokens.Typography.TitleMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DesignTokens.Colors.Surface
                )
            )
        },
        containerColor = DesignTokens.Colors.Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = DesignTokens.Spacing.Large, vertical = DesignTokens.Spacing.Large)
        ) {
            AnimatedVisibility(
                visible = uiState.isSubmitted,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SubmittedValuesCard(
                    submittedValues = uiState.submittedValues,
                    onReset = viewModel::onResetSubmission
                )
            }

            AnimatedVisibility(
                visible = !uiState.isSubmitted,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    shape = RoundedCornerShape(DesignTokens.CornerRadius.Large),
                    colors = CardDefaults.cardColors(containerColor = DesignTokens.Colors.Surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(DesignTokens.Spacing.Large)
                    ) {
                        Text(
                            text = "Prospect Details",
                            style = DesignTokens.Typography.TitleLarge
                        )
                        Text(
                            text = "Please complete all required fields below to register the lead.",
                            style = DesignTokens.Typography.Hint,
                            modifier = Modifier.padding(
                                top = DesignTokens.Spacing.XSmall,
                                bottom = DesignTokens.Spacing.Large
                            )
                        )

                        LeadForm(
                            config = viewModel.config,
                            values = uiState.values,
                            getVisibleError = { fieldName -> uiState.getVisibleError(fieldName) },
                            onFieldValueChange = viewModel::onFieldValueChange,
                            onFieldFocusChanged = viewModel::onFieldFocusChanged,
                            onSubmit = viewModel::onSubmit
                        )
                    }
                }
            }
        }
    }
}

/**
 * Displays the submitted lead values cleanly on screen.
 */
@Composable
fun SubmittedValuesCard(
    submittedValues: Map<String, String>,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(DesignTokens.CornerRadius.Large),
        colors = CardDefaults.cardColors(containerColor = DesignTokens.Colors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DesignTokens.Spacing.XXLarge)
        ) {
            // Success header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(DesignTokens.CornerRadius.Medium))
                    .background(DesignTokens.Colors.SuccessContainer)
                    .padding(DesignTokens.Spacing.Large)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Success",
                    tint = DesignTokens.Colors.Success,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.size(DesignTokens.Spacing.Medium))
                Column {
                    Text(
                        text = "Lead Submitted Successfully",
                        style = DesignTokens.Typography.TitleMedium.copy(
                            color = DesignTokens.Colors.OnSuccess
                        )
                    )
                    Text(
                        text = "The captured configuration values are listed below.",
                        style = DesignTokens.Typography.Hint.copy(
                            color = DesignTokens.Colors.OnSuccess.copy(alpha = 0.8f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(DesignTokens.Spacing.XXLarge))

            Text(
                text = "Submitted Form Values",
                style = DesignTokens.Typography.TitleMedium
            )

            Spacer(modifier = Modifier.height(DesignTokens.Spacing.Medium))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(DesignTokens.CornerRadius.Medium))
                    .background(DesignTokens.Colors.SurfaceVariant)
                    .border(
                        width = 1.dp,
                        color = DesignTokens.Colors.Border,
                        shape = RoundedCornerShape(DesignTokens.CornerRadius.Medium)
                    )
                    .padding(DesignTokens.Spacing.Medium)
            ) {
                submittedValues.entries.forEachIndexed { index, entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = DesignTokens.Spacing.Small),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = entry.key,
                            style = DesignTokens.Typography.Label.copy(
                                color = DesignTokens.Colors.TextSecondary
                            )
                        )
                        Text(
                            text = entry.value.ifBlank { "—" },
                            style = DesignTokens.Typography.Body
                        )
                    }
                    if (index < submittedValues.size - 1) {
                        HorizontalDivider(
                            color = DesignTokens.Colors.Border.copy(alpha = 0.5f),
                            thickness = 0.8.dp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(DesignTokens.Spacing.XXLarge))

            ButtonAtom(
                text = "Capture Another Lead",
                onClick = onReset,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.RestartAlt,
                        contentDescription = null,
                        tint = DesignTokens.Colors.OnPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(DesignTokens.Spacing.Small))
                }
            )
        }
    }
}
