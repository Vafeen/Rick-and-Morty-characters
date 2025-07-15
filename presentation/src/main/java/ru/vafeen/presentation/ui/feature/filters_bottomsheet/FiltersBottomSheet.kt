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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.common.components.AppButton
import ru.vafeen.presentation.ui.common.utils.getMainColorForThisTheme
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.Filters
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersEffect
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersIntent
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersViewModel
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
@OptIn(ExperimentalMaterial3Api::class)
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
                .verticalScroll(rememberScrollState())
        ) {
            // Name input field
            OutlinedTextField(
                value = state.filters.name ?: "",
                onValueChange = { viewModel.handleEvent(FiltersIntent.NameChanged(it)) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Type input field
            OutlinedTextField(
                value = state.filters.type ?: "",
                onValueChange = { viewModel.handleEvent(FiltersIntent.TypeChanged(it)) },
                label = { Text("Type") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Species input field
            OutlinedTextField(
                value = state.filters.species ?: "",
                onValueChange = { viewModel.handleEvent(FiltersIntent.SpeciesChanged(it)) },
                label = { Text("Species") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Life status filter chips
            Text("Status", style = MaterialTheme.typography.labelMedium)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LifeStatus.entries.forEach { status ->
                    FilterChip(
                        selected = state.filters.status == status,
                        onClick = {
                            viewModel.handleEvent(
                                FiltersIntent.StatusChanged(
                                    if (state.filters.status == status) null else status
                                )
                            )
                        },
                        label = { Text(status.toString()) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gender filter chips
            Text("Gender", style = MaterialTheme.typography.labelMedium)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Gender.entries.forEach { gender ->
                    FilterChip(
                        selected = state.filters.gender == gender,
                        onClick = {
                            viewModel.handleEvent(
                                FiltersIntent.GenderChanged(
                                    if (state.filters.gender == gender) null else gender
                                )
                            )
                        },
                        label = { Text(gender.toString()) }
                    )
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
