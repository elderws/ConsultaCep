package net.overclock.consultacep

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var resultText by remember { mutableStateOf("") }
        var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        var isTextFieldEnabled by remember { mutableStateOf(true) }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                enabled = isTextFieldEnabled,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            Button(
                onClick = {
                    isLoading = true
                    isTextFieldEnabled = false
                    resultText = ""
                },
                enabled = !isLoading,
                modifier = Modifier.padding(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    LaunchedEffect(Unit) {
                        delay(2000)
                        resultText = "Consulta realizada com sucesso!"
                        isLoading = false
                        isTextFieldEnabled = true
                    }
                } else {
                    Text("Consultar")
                }
            }
            Text(resultText)
        }
    }
}