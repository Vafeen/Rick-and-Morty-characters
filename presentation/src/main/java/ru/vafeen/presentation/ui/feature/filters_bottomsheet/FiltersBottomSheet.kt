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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.domain.model.Gender
import ru.vafeen.domain.model.LifeStatus
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersEffect
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersIntent
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersState
import ru.vafeen.presentation.ui.feature.filters_bottomsheet.FiltersViewModel
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Composable display for the filters modal bottom sheet.
 *
 * Shows filter controls for character name, life status, and gender.
 * Allows users to reset or apply filters.
 *
 * @param initialFilters Initial state of the filters shown when the sheet appears.
 * @param onFiltersApplied Callback invoked when filters are applied, providing the selected filters.
 * @param onDismissRequest Callback invoked when the bottom sheet is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    initialFilters: FiltersState,
    onFiltersApplied: (FiltersState) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val viewModel = hiltViewModel<FiltersViewModel, FiltersViewModel.Factory> { factory ->
        factory.create(initialFilters)
    }
    val state by viewModel.state.collectAsState()

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
                value = state.name ?: "",
                onValueChange = { viewModel.handleEvent(FiltersIntent.NameChanged(it)) },
                label = { Text("Name") },
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
                        selected = state.status == status,
                        onClick = {
                            viewModel.handleEvent(
                                FiltersIntent.StatusChanged(
                                    if (state.status == status) null else status
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
                        selected = state.gender == gender,
                        onClick = {
                            viewModel.handleEvent(
                                FiltersIntent.GenderChanged(
                                    if (state.gender == gender) null else gender
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
                OutlinedButton(
                    onClick = { viewModel.handleEvent(FiltersIntent.ResetFilters) }
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = { viewModel.handleEvent(FiltersIntent.ApplyFilters) }
                ) {
                    Text("Apply Filters")
                }
            }
        }
    }
}
