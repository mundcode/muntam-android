package com.mundcode.muntam.presentation.screen.main.favorites

import androidx.lifecycle.viewModelScope
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.domain.usecase.DeleteExamUseCase
import com.mundcode.domain.usecase.GetFavoriteExamsUseCase
import com.mundcode.domain.usecase.GetQuestionsBySubjectIdUseCase
import com.mundcode.domain.usecase.UpdateExamUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.navigation.ExamRecord
import com.mundcode.muntam.navigation.Questions
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.presentation.model.asStateModel
import com.mundcode.muntam.worker.QuestionNotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteExamViewModel @Inject constructor(
    private val getFavoriteExamsUseCase: GetFavoriteExamsUseCase,
    private val updateExamUseCase: UpdateExamUseCase,
    private val deleteExamUseCase: DeleteExamUseCase,
    private val getQuestionsBySubjectIdUseCase: GetQuestionsBySubjectIdUseCase
) : BaseViewModel<FavoriteExamState>() {

    private val _alarmCancelEvent = MutableSharedFlow<String>()
    val alarmCancelEvent: SharedFlow<String> = _alarmCancelEvent

    init {
        loadExams()
    }

    private fun loadExams() = viewModelScope.launch(Dispatchers.IO) {
        getFavoriteExamsUseCase().collectLatest {
            updateState {
                state.value.copy(
                    exams = it.mapValues { (_, value) -> value.map { it.asStateModel() } }
                )
            }
        }
    }

    fun onClickExam(examModel: ExamModel) = viewModelScope.launch {
        if (examModel.state == ExamState.END && examModel.completeAd) {
            _navigationEvent.emit(Questions.getRouteWithArgs(examId = examModel.id))
        } else {
            _navigationEvent.emit(ExamRecord.getRouteWithArgs(examModel.subjectId, examModel.id))
        }
    }

    fun onClickFavorite(examModel: ExamModel) = viewModelScope.launch(Dispatchers.IO) {
        updateExamUseCase(
            examModel.copy(
                isFavorite = examModel.isFavorite.not()
            ).asExternalModel()
        )
        _toast.emit("즐겨찾기가 해제되었습니다.")
    }

    fun onClickOption(examModel: ExamModel) {
        updateState {
            stateValue.copy(
                focusedExam = examModel
            )
        }
    }

    fun onSelectDeleteFromBottomSheet() {
        updateState {
            stateValue.copy(
                showDeleteConfirmDialog = true
            )
        }
    }

    fun onSelectModifyFromBottomSheet() {
        updateState {
            stateValue.copy(
                showModifyDialog = true
            )
        }
    }

    fun onSelectDeleteExam() = viewModelScope.launch(Dispatchers.IO) {
        onCancelDialog()

        stateValue.focusedExam?.let {
            getQuestionsBySubjectIdUseCase(it.subjectId).forEach { question ->
                if (question.isAlarm) {
                    _alarmCancelEvent.emit(
                        QuestionNotificationWorker.getWorkerIdWithArgs(questionId = question.id)
                    )
                }
            }
            deleteExamUseCase(it.id)
        }
    }

    fun onModifyExamName(name: String) = viewModelScope.launch(Dispatchers.IO) {
        onCancelDialog()
        stateValue.focusedExam?.let {
            updateExamUseCase(it.copy(name = name).asExternalModel())
        }
    }

    fun onCancelDialog() {
        updateState {
            state.value.copy(
                focusedExam = null,
                showModifyDialog = false,
                showDeleteConfirmDialog = false
            )
        }
    }

    override fun createInitialState(): FavoriteExamState {
        return FavoriteExamState()
    }
}

data class FavoriteExamState(
    val exams: Map<String, List<ExamModel>> = mapOf(),
    val focusedExam: ExamModel? = null,
    val showModifyDialog: Boolean = false,
    val showDeleteConfirmDialog: Boolean = false
)
