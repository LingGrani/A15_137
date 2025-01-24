package com.example.projekakhirpam.ui.component

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimeDatePickerSQL(
    modifier: Modifier = Modifier,
    onValueChangedEvent: (LocalDateTime) -> Unit,
) {
    var datetime by remember { mutableStateOf<LocalDateTime?>(null) }
    Column {
        Row (
            modifier = modifier
        ){
            DatePickerFieldToModal(
                modifier = Modifier.weight(3f),
                date = {
                    var test = convertMillisToYearMonthDay(it.selectedDateMillis)
                    if (test != null) {
                        datetime = datetime?.withYear(test.first)
                        datetime = datetime?.withMonth(test.second)
                        datetime = datetime?.withDayOfMonth(test.third)
                    }
                }
            )
            TimePickerFieldToModal(
                modifier = Modifier.weight(2f),
                time = { timePickerState ->
                    datetime = datetime?.withHour(timePickerState.hour)
                    datetime = datetime?.withMinute(timePickerState.minute)
                }
            )
        }
    }
    datetime?.let { onValueChangedEvent(it) }
}

fun convertMillisToYearMonthDay(millis: Long?): Triple<Int, Int, Int>? {
    return millis?.let {
        val localDate = Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        Triple(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }
}


/////////////////////////DATE/////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    date: (DatePickerState) -> Unit
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("Date") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                selectedDate = it.selectedDateMillis
                date(it)},
            onDismiss = { showModal = false }
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (DatePickerState) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
/////////////////////////DATE/////////////////////////

/////////////////////////TIME/////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerFieldToModal(
    modifier: Modifier = Modifier,
    time: (TimePickerState) -> Unit
) {
    var selectedTime by remember { mutableStateOf("") }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedTime,
        onValueChange = {},
        label = { Text("Time") },
        placeholder = { Text("HH/MM") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedTime) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DialWithDialogExample(
            onConfirm = { timePickerState ->
                selectedTime = " ${timePickerState.hour} : ${timePickerState.minute} "
                time(timePickerState) },
            onDismiss = { showModal = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithDialogExample(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = {
            onConfirm(timePickerState)
            onDismiss()
        }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}
/////////////////////////TIME/////////////////////////