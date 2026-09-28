package ru.matveev.gigachat.ui.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.matveev.gigachat.R
import ru.matveev.gigachat.ui.Effect
import ru.matveev.gigachat.ui.auth.login.SignInEvent
import ru.matveev.gigachat.ui.component.OrDivider
import ru.matveev.gigachat.ui.component.StyledButton

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignIn: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

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
            modifier = modifier
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
                        viewModel.onEvent(SignUpEvent.EmailUpdated(it))
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
                        viewModel.onEvent(SignUpEvent.PasswordUpdated(it))
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
                                viewModel.onEvent(SignUpEvent.VisiblePasswordUpdated)
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

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.confirmPassword,
                    onValueChange = {
                        viewModel.onEvent(SignUpEvent.ConfirmPasswordUpdated(it))
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
                                viewModel.onEvent(SignUpEvent.VisibleConfirmPasswordUpdated)
                            }
                        ) {
                            if (state.isVisibleConfirmPassword) {
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
                    visualTransformation = if (state.isVisibleConfirmPassword) {
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
                        viewModel.onEvent(SignUpEvent.SignUp)
                    },
                    enabled = state.isSubmitEnabled
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_in),
                        fontSize = 19.sp
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            OrDivider()

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.yes_account),
                    fontSize = 16.sp
                )
                TextButton(
                    onClick = onSignIn
                ) {
                    Text(
                        text = stringResource(R.string.sign_in),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}