package app.dreamlightpal.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String,
    contentDescription: String,
    saver: Saver<MutableState<TextFieldValue>, String> = Saver(
        save = { it.value.text },
        restore = { mutableStateOf(TextFieldValue(it)) }
    ),
    onTextChanged: (input: String) -> Unit,
    onDone: (KeyboardActionScope.() -> Unit),
) {
    val textState = rememberSaveable(saver = saver) { mutableStateOf(TextFieldValue()) }

    BasicTextField(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12F),
                shape = CircleShape,
            )
            .padding(12.dp)
            .fillMaxWidth(),
        value = textState.value,
        onValueChange = {
            textState.value = it
            onTextChanged(it.text)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = onDone),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = MaterialTheme.typography.bodyLarge,
        decorationBox = { innerTextField ->
            SearchDecorationBox(
                contentDescription = contentDescription,
                textState = textState.value,
                hint = hint,
                innerTextField = innerTextField
            )
        }
    )
}

@Composable
private fun SearchDecorationBox(
    contentDescription: String,
    textState: TextFieldValue,
    hint: String,
    innerTextField: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = contentDescription,
            tint = Color(0xFF3D4B6F)
        )

        if (textState.text.isEmpty()) {
            Box(Modifier.weight(1f)) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF3D4B6F)
                )
                innerTextField()
            }
        } else {
            innerTextField()
        }
    }
}
