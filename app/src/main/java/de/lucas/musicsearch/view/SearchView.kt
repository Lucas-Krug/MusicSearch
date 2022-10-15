package de.lucas.musicsearch.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import de.lucas.musicsearch.R
import de.lucas.musicsearch.view.theme.Gray500

@Composable
fun SearchView(onClickSearch: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var showTextField by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { showTextField = true }, modifier = Modifier.testTag("searchIcon")) {
            Icon(
                imageVector = Icons.Rounded.Search,
                tint = MaterialTheme.colors.onBackground,
                contentDescription = ""
            )
        }
        AnimatedVisibility(showTextField) {
            TextField(
                value = searchText,
                onValueChange = { onSearchTextChanged ->
                    searchText = onSearchTextChanged
                },
                trailingIcon = {
                    IconButton(onClick = {
                        searchText = ""
                        showTextField = false
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            tint = MaterialTheme.colors.onBackground,
                            contentDescription = ""
                        )
                    }
                },
                placeholder = { Text(stringResource(id = R.string.searchSong)) },
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Gray500,
                    unfocusedIndicatorColor = Gray500,
                    disabledIndicatorColor = Gray500
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchText.isNotEmpty()) {
                            onClickSearch(searchText)
                            focusManager.clearFocus()
                        }
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.background, shape = RectangleShape)
                    .focusRequester(focusRequester)
                    .testTag("textField")
            )
        }
    }
    DisposableEffect(showTextField) {
        if (showTextField) {
            focusRequester.requestFocus()
        }
        onDispose {}
    }
}