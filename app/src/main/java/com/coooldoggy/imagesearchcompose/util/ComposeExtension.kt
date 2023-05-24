package com.coooldoggy.imagesearchcompose.util

import androidx.activity.ComponentActivity
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce

interface MultipleEventsCutterManager {
    fun processEvent(event: () -> Unit)
}

@OptIn(FlowPreview::class)
@Composable
fun <T> MultipleEventsCutter(
    delay: Long = 300L,
    content: @Composable (MultipleEventsCutterManager) -> T,
): T {
    val debounceState = remember {
        MutableSharedFlow<() -> Unit>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    }

    val result = content(
        object : MultipleEventsCutterManager {
            override fun processEvent(event: () -> Unit) {
                debounceState.tryEmit(event)
            }
        },
    )

    LaunchedEffect(true) {
        debounceState
            .debounce(delay)
            .collect { onClick ->
                onClick.invoke()
            }
    }

    return result
}

fun Modifier.onClick(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    onClick: () -> Unit,
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    },
) {
    MultipleEventsCutter { manager ->
        Modifier.clickable(
            enabled = enabled,
            onClickLabel = onClickLabel,
            onClick = { manager.processEvent { onClick() } },
            role = role,
            indication = LocalIndication.current,
            interactionSource = remember { interactionSource },
        )
    }
}

@Composable
fun getActivity() = LocalContext.current as ComponentActivity

@Composable
inline fun <reified VM : ViewModel> composableActivityViewModel(
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
): VM = viewModel(
    VM::class.java,
    getActivity(),
    key,
    factory,
)
