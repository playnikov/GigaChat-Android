package ru.matveev.gigachat.feature.auth.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.matveev.gigachat.R
import ru.matveev.gigachat.core.error.AppResult
import ru.matveev.gigachat.core.ui.Effect
import ru.matveev.gigachat.core.ui.component.GoogleButton
import ru.matveev.gigachat.core.ui.component.OrDivider
import ru.matveev.gigachat.core.ui.component.StyledButton
import ru.matveev.gigachat.feature.auth.component.EmailTextField
import ru.matveev.gigachat.feature.auth.component.PasswordTextField

@Composable
fun SignInScreen(
    onSuccess: () -> Unit,
    onSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val googleTokenProvider = rememberGoogleTokenProvider()
    val activity = LocalContext.current.findActivity()

    LaunchedEffect(viewModel) {
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
                modifier = Modifier.padding(top = 100.dp)
            )

            Spacer(Modifier.height(180.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EmailTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEvent(SignInEvent.EmailUpdated(it)) }
                )

                PasswordTextField(
                    value = state.password,
                    onValueChange = { viewModel.onEvent(SignInEvent.PasswordUpdated(it)) },
                    isVisible = state.isVisiblePassword,
                    onToggleVisibility = { viewModel.onEvent(SignInEvent.VisiblePasswordUpdated) },
                    placeholder = stringResource(R.string.enter_password)
                )

                StyledButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.onEvent(SignInEvent.SignIn) },
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

            Spacer(Modifier.height(20.dp))

            GoogleButton(
                modifier = Modifier.fillMaxWidth(),
                onClicked = {
                    scope.launch {
                        when (val result = googleTokenProvider.getIdToken(activity)) {
                            is AppResult.Success -> viewModel.onEvent(
                                SignInEvent.GoogleTokenReceived(result.data)
                            )
                            is AppResult.Failure -> viewModel.onEvent(
                                SignInEvent.GoogleTokenFailed(result.error)
                            )
                        }
                    }
                },
                text = stringResource(R.string.sign_with_google),
                loading = state.isLoading
            )

            Spacer(Modifier.height(20.dp))

            OrDivider()

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.no_account),
                    fontSize = 16.sp
                )
                TextButton(onClick = onSignUp) {
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
