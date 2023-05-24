package com.coooldoggy.imagesearchcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coooldoggy.imagesearchcompose.ui.common.BaseUiContract
import com.coooldoggy.imagesearchcompose.ui.common.ErrorData
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMVIViewModel : ViewModel() {
    protected abstract fun handleEvents(event: BaseUiEvent)

    private val _event: MutableSharedFlow<BaseUiEvent> = MutableSharedFlow()

    private val _effect: Channel<BaseUiEffect> = Channel()
    private val effect: Flow<BaseUiEffect> = _effect.receiveAsFlow()

    private val _commonEvent: MutableSharedFlow<BaseUiContract.BaseUiLoadingEvent> =
        MutableSharedFlow()

    private val _commonState: MutableStateFlow<BaseUiContract.BaseUiLoadingState> =
        MutableStateFlow(
            BaseUiContract.BaseUiLoadingState(
                uiState = BaseUiContract.UiState.PROGRESS,
                apiError = ErrorData(""),
            ),
        )
    val commonState = _commonState.asStateFlow()

    open fun enableMoreToLoad(enableMoreToLoad: Boolean) {
        loadDataComplete(enableMoreToLoad)
    }

    protected var activeFlowJob: Job = SupervisorJob(viewModelScope.coroutineContext[Job])

    protected fun cancelActiveFlowJob() {
        activeFlowJob.cancel()
        activeFlowJob = SupervisorJob(viewModelScope.coroutineContext[Job])
    }

    open fun loadMore() {
        sendUiEvent(BaseUiContract.BaseUiLoadingEvent.LoadMore)
    }

    private fun loadDataComplete(enableMoreToLoad: Boolean = false) {
        if (enableMoreToLoad) {
            sendUiEvent(BaseUiContract.BaseUiLoadingEvent.Progress)
        } else {
            sendUiEvent(BaseUiContract.BaseUiLoadingEvent.Complete)
        }
    }

    fun setErrorState(errorMsg: String) {
        sendUiEvent(BaseUiContract.BaseUiLoadingEvent.Error(errorMsg))
    }

    protected abstract fun loadData()

    fun <T : BaseUiEffect> effect(): Flow<T> {
        return effect as Flow<T>
    }

    init {
        subscribeToEvents()
        subscribeToCommonEvents()
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect { _event ->
                handleEvents(_event)
            }
        }
    }

    fun sendUiEvent(event: BaseUiContract.BaseUiLoadingEvent) {
        viewModelScope.launch {
            _commonEvent.emit(event)
        }
    }

    private fun subscribeToCommonEvents() {
        viewModelScope.launch {
            _commonEvent.collect { _event ->
                handleCommonEvents(_event)
            }
        }
    }

    private fun handleCommonEvents(event: BaseUiContract.BaseUiLoadingEvent) {
        when (event) {
            is BaseUiContract.BaseUiLoadingEvent.LoadMore -> {
                loadData()
            }
            is BaseUiContract.BaseUiLoadingEvent.Progress -> {
                _commonState.update {
                    it.copy(
                        uiState = BaseUiContract.UiState.PROGRESS,
                    )
                }
            }
            is BaseUiContract.BaseUiLoadingEvent.Complete -> {
                _commonState.update {
                    it.copy(
                        uiState = BaseUiContract.UiState.COMPLETE,
                    )
                }
            }
            is BaseUiContract.BaseUiLoadingEvent.Error -> {
                _commonState.update {
                    it.copy(
                        uiState = BaseUiContract.UiState.ERROR,
                    )
                }
            }
        }
    }

    /**
     * View -> ViewModel로 Event 전달
     */
    fun sendEvent(event: BaseUiEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    /**
     * ViewModel -> View Intent
     */
    protected fun sendEffect(effect: () -> BaseUiEffect) {
        val effectValue = effect()
        viewModelScope.launch {
            _effect.send(effectValue)
        }
    }

    protected fun sendEffect(effect: BaseUiEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    open fun isEmpty(totalItemsCount: Int): Boolean {
        return totalItemsCount == 0
    }

    override fun onCleared() {
        activeFlowJob.cancel()
        super.onCleared()
    }
}

interface BaseUiState // data class
interface BaseUiEvent
interface BaseUiEffect
