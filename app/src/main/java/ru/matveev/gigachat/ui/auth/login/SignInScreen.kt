package ru.matveev.gigachat.ui.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.matveev.gigachat.R
import ru.matveev.gigachat.ui.Effect
import ru.matveev.gigachat.ui.component.GoogleButton
import ru.matveev.gigachat.ui.component.OrDivider
import ru.matveev.gigachat.ui.component.StyledButton
import ru.matveev.gigachat.ui.theme.GigaChatTheme

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit,
    onSignUp: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val googleTokenProvider = rememberGoogleTokenProvider()
    val activity = LocalContext.current.findActivity()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is Effect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is Effect.ShowInfo -> snackbarHostState.showSnackbar(effect.message)
                Effect.Success -> onSuccess()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(top = 100
                        .dp)
            )

            Spacer(Modifier.height(180.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.email,
                    onValueChange = {
                        viewModel.onEvent(SignInEvent.EmailUpdated(it))
                    },
                    leadingIcon = {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.Email),
                            contentDescription = "Input e-mail"
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_email)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.password,
                    onValueChange = {
                        viewModel.onEvent(SignInEvent.PasswordUpdated(it))
                    },
                    leadingIcon = {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.Password),
                            contentDescription = "Input password"
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(SignInEvent.VisiblePasswordUpdated)
                            }
                        ) {
                            if (state.isVisiblePassword) {
                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Outlined.VisibilityOff),
                                    contentDescription = "Hide password"
                                )
                            } else {
                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Outlined.Visibility),
                                    contentDescription = "Show password"
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_password)
                        )
                    },
                    visualTransformation = if (state.isVisiblePassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )

                StyledButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.onEvent(SignInEvent.SignIn)
                    },
                    enabled = state.isSubmitEnabled
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.sign_in),
                            fontSize = 19.sp
                        )
                    }
                }
            }

            Spacer(modifier.height(20.dp))

            GoogleButton(
                modifier = Modifier.fillMaxWidth(),
                onClicked = {
                    scope.launch {
                        googleTokenProvider.getIdToken(activity)
                            .onSuccess { token ->
                                viewModel.onEvent(SignInEvent.GoogleTokenReceived(token))
                            }
                            .onFailure { error ->
                                viewModel.onEvent(SignInEvent.GoogleTokenFailed(error))
                            }
                    }
                },
                text = stringResource(R.string.sign_with_google),
            )

            Spacer(modifier.height(20.dp))

            OrDivider()

            Spacer(modifier.height(20.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.no_account),
                    fontSize = 16.sp
                )
                TextButton(
                    onClick = onSignUp
                ) {
                    Text(
                        text = stringResource(R.string.sign_up),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}