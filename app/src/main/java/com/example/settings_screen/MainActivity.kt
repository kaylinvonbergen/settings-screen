package com.example.settings_screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.settings_screen.ui.theme.SettingsscreenTheme
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Add


@OptIn(ExperimentalMaterial3Api::class) // for "experimental" UI
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettingsscreenTheme {
                SettingsScreenShell()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenShell() {

    var showHelpDialog by remember { mutableStateOf(false) }
    var isOver18 by remember { mutableStateOf(false) }
    var interests by remember {
        mutableStateOf(
            mutableListOf("Music", "Video games", "Photography", "Programming")
        )
    }
    var newInterestText by remember { mutableStateOf("") }
    var showAddInterestDialog by remember { mutableStateOf(false) }
    var preferredName by remember { mutableStateOf("John Doe") }
    var showNameDialog by remember { mutableStateOf(false) }
    var nameDraft by remember { mutableStateOf(preferredName) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                actions = {
                    IconButton(onClick = { showHelpDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Help")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column( // layout requirement
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            SettingRows(
                label = "Notifications",
                info = "Control alerts and reminders."
            ) {
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }
            Divider(modifier = Modifier.padding(vertical = 6.dp))
            SettingRows(
                label = "Preferred name",
                info = if (preferredName.isBlank()) "Tap to set your name." else "Current: $preferredName"
            ) {
                TextButton(onClick = {
                    nameDraft = preferredName
                    showNameDialog = true
                }) {
                    Text("Edit")
                }
            }
            Divider(modifier = Modifier.padding(vertical = 6.dp))
            SettingRows(
                label = "Interests",
                info = "Add and remove interests."
            ) {
                IconButton(onClick = { showAddInterestDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Interest")
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(interests) { interest ->
                    InputChip(
                        selected = false,
                        onClick = { },
                        label = { Text(interest, maxLines = 1) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Remove",
                                modifier = Modifier.clickable {
                                    interests = interests.toMutableList().apply { remove(interest) }
                                }
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Privacy",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SettingRows(
                label = "Over 18",
                info = "Confirm age for using this app."
            ) {
                Checkbox(
                    checked = isOver18,
                    onCheckedChange = { isOver18 = it }
                )
            }
            if (showHelpDialog) {
                AlertDialog(
                    onDismissRequest = { showHelpDialog = false },

                    title = {
                        Text("Settings Help")
                    },

                    text = {
                        Text(
                            "This screen lets you customize your preferences. " +
                                    "You can update your name, manage your interests, and control notifications and privacy options."
                        )
                    },

                    confirmButton = {
                        TextButton(
                            onClick = { showHelpDialog = false }
                        ) {
                            Text("Got it")
                        }
                    }
                )
            }
            if (showAddInterestDialog) {
                AlertDialog(
                    onDismissRequest = { showAddInterestDialog = false },
                    title = { Text("Add Interest") },
                    text = {
                        OutlinedTextField(
                            value = newInterestText,
                            onValueChange = { newInterestText = it },
                            label = { Text("Interest") },
                            singleLine = true
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (newInterestText.isNotBlank()) {
                                interests = interests.toMutableList().apply {
                                    add(newInterestText.trim())
                                }
                                newInterestText = ""
                            }
                            showAddInterestDialog = false
                        }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddInterestDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
            if (showNameDialog) {
                AlertDialog(
                    onDismissRequest = { showNameDialog = false },
                    title = { Text("Edit preferred name") },
                    text = {
                        OutlinedTextField(
                            value = nameDraft,
                            onValueChange = { nameDraft = it },
                            label = { Text("Preferred name") },
                            singleLine = true
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            preferredName = nameDraft.trim()
                            showNameDialog = false
                        }) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showNameDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

/**
 * Settings rows: label for function, and info string
 */
@Composable
fun SettingRows(
    label: String,
    info: String,
    control: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {}
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(text = info, style = MaterialTheme.typography.bodySmall)
        }
        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
            control()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenShellPreview() {
    SettingsscreenTheme {
        SettingsScreenShell()
    }
}