package ru.vafeen.presentation.ui.feature.filters_bottomsheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.common.components.AppButton
import ru.vafeen.presentation.ui.common.components.ThisThemeText
import ru.vafeen.presentation.ui.common.utils.getMainColorForThisTheme
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Composable display for the filters modal bottom sheet.
 *
 * Shows filter controls for character name, life status, gender, type, and species.
 * Allows users to reset or apply filters.
 *
 * @param initialFilters Initial state of the filters shown when the sheet appears.
 * @param onFiltersApplied Callback invoked when filters are applied, providing the selected filters.
 * @param onDismissRequest Callback invoked when the bottom sheet is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FiltersBottomSheet(
    initialFilters: Filters,
    onFiltersApplied: (Filters) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val viewModel = hiltViewModel<FiltersViewModel, FiltersViewModel.Factory> { factory ->
        factory.create(initialFilters)
    }
    val state by viewModel.state.collectAsState()
    val mainColor by rememberUpdatedState(
        state.settings.getMainColorForThisTheme(
            isSystemInDarkTheme()
        ) ?: AppTheme.colors.mainColor
    )
    // Handles one-time effects from the ViewModel such as filters applied event.
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is FiltersEffect.FiltersApplied -> {
                    onFiltersApplied(effect.filters)
                    onDismissRequest()
                }
            }
        }
    }
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = AppTheme.colors.text,
        unfocusedTextColor = AppTheme.colors.text,
        focusedBorderColor = AppTheme.colors.text,
        unfocusedBorderColor = AppTheme.colors.text,
        focusedPlaceholderColor = AppTheme.colors.text,
        unfocusedPlaceholderColor = AppTheme.colors.text,
        focusedLabelColor = AppTheme.colors.text,
        unfocusedLabelColor = AppTheme.colors.text,
        cursorColor = AppTheme.colors.text,
    )

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = Modifier.navigationBarsPadding(),
        onDismissRequest = onDismissRequest,
        containerColor = AppTheme.colors.buttonColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Name input field
            OutlinedTextField(
                value = state.filters.name ?: "",
                onValueChange = { viewModel.handleEvent(FiltersIntent.NameChanged(it)) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Type input field
            OutlinedTextField(
                value = state.filters.type ?: "",
                onValueChange = { viewModel.handleEvent(FiltersIntent.TypeChanged(it)) },
                label = { Text("Type") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Species input field
            OutlinedTextField(
                value = state.filters.species ?: "",
                onValueChange = { viewModel.handleEvent(FiltersIntent.SpeciesChanged(it)) },
                label = { Text(text = "Species") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Life status filter chips
            ThisThemeText(
                stringResource(R.string.status),
                style = MaterialTheme.typography.labelMedium
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LifeStatus.entries.forEach { status ->
                    Chip(
                        onClick = {
                            viewModel.handleEvent(
                                FiltersIntent.StatusChanged(
                                    if (state.filters.status == status) null else status
                                )
                            )
                        },
                        leadingIcon = if (state.filters.status == status) {
                            {
                                Icon(
                                    painter = painterResource(R.drawable.done),
                                    contentDescription = stringResource(R.string.selected_filter),
                                    tint = AppTheme.colors.text
                                )
                            }
                        } else null,
                        colors = ChipDefaults.chipColors(
                            contentColor = AppTheme.colors.text,
                            backgroundColor = AppTheme.colors.buttonColor
                        ),
                        border = BorderStroke(1.dp, AppTheme.colors.text),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        ThisThemeText(status.toString())
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gender filter chips
            ThisThemeText(
                stringResource(R.string.gender),
                style = MaterialTheme.typography.labelMedium
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Gender.entries.forEach { gender ->
                    Chip(
                        onClick = {
                            viewModel.handleEvent(
                                FiltersIntent.GenderChanged(
                                    if (state.filters.gender == gender) null else gender
                                )
                            )
                        },
                        leadingIcon = if (state.filters.gender == gender) {
                            {
                                Icon(
                                    painter = painterResource(R.drawable.done),
                                    contentDescription = stringResource(R.string.selected_filter),
                                    tint = AppTheme.colors.text
                                )
                            }
                        } else null,
                        colors = ChipDefaults.chipColors(
                            contentColor = AppTheme.colors.text,
                            backgroundColor = AppTheme.colors.buttonColor
                        ),
                        border = BorderStroke(1.dp, AppTheme.colors.text),
                        shape = RoundedCornerShape(10.dp)
                    ) { ThisThemeText(gender.toString()) }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reset and Apply buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppButton(
                    onClick = { viewModel.handleEvent(FiltersIntent.ResetFilters) },
                    color = mainColor
                ) {
                    Text(stringResource(R.string.reset))
                }
                AppButton(
                    onClick = { viewModel.handleEvent(FiltersIntent.ApplyFilters) },
                    color = mainColor
                ) {
                    Text(stringResource(R.string.apply_filters))
                }

            }
        }
    }
}
