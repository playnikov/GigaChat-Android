package ru.matveev.gigachat.ui.auth.login

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface GoogleAuthEntryPoint {
    fun googleTokenProvider(): GoogleTokenProvider
}

@Composable
fun rememberGoogleTokenProvider(): GoogleTokenProvider {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    return remember(activity) {
        EntryPointAccessors.fromActivity(
            activity,
            GoogleAuthEntryPoint::class.java,
        ).googleTokenProvider()
    }
}

fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    error("Activity not found in context chain")
}