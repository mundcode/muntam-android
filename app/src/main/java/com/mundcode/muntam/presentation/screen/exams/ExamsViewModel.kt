package com.mundcode.muntam.presentation.screen.exams

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mundcode.designsystem.state.ToastState
import com.mundcode.designsystem.state.rememberToastState
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.domain.usecase.DeleteExamUseCase
import com.mundcode.domain.usecase.GetExamsUseCase
import com.mundcode.domain.usecase.GetSubjectByIdUseCase
import com.mundcode.domain.usecase.InsertExamUseCase
import com.mundcode.domain.usecase.InsertQuestionsExamUseCase
import com.mundcode.domain.usecase.UpdateExamUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.navigation.ExamRecord
import com.mundcode.muntam.navigation.Questions
import com.mundcode.muntam.navigation.SubjectModify
import com.mundcode.muntam.presentation.model.ExamModel
import com.mundcode.muntam.presentation.model.QuestionModel
import com.mundcode.muntam.presentation.model.asExternalModel
import com.mundcode.muntam.presentation.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class ExamsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExamsUseCase: GetExamsUseCase,
    private val getSubjectByIdUseCase: GetSubjectByIdUseCase,
    private val insertQuestionsUseCase: InsertQuestionsExamUseCase,
    private val insertExamUseCase: InsertExamUseCase,
    private val deleteExamUseCase: DeleteExamUseCase,
    private val updateExamUseCase: UpdateExamUseCase
) : BaseViewModel<ExamsState>() {
    private val subjectId: Int = checkNotNull(savedStateHandle[SubjectModify.subjectIdArg])

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent.asSharedFlow()

    init {
        loadExams()
        loadSubject()
    }

    private fun loadExams() = viewModelScope.launch(Dispatchers.IO) {
        getExamsUseCase(subjectId).collectLatest {
            updateState { state.value.copy(exams = it.map { it.asStateModel() }) }
        }
    }

    private fun loadSubject() = viewModelScope.launch(Dispatchers.IO) {
        val subject = getSubjectByIdUseCase(subjectId)
        updateState {
            state.value.copy(
                subjectTitle = subject.name
            )
        }
    }

    fun onClickExam(exam: ExamModel) = viewModelScope.launch {
        if (exam.state == ExamState.END) {
            _navigationEvent.emit(Questions.route)
        } else {
            _navigationEvent.emit(ExamRecord.getRouteWithArgs(subjectId, exam.id))
        }
    }

    fun onClickExamSave(exam: ExamModel) = viewModelScope.launch(Dispatchers.IO) {
        updateExamUseCase(exam.copy(isFavorite = exam.isFavorite.not()).asExternalModel())
    }

    fun onClickExamOption() = updateState {
        state.value.copy(showExamOptionDialog = true)
    }

    fun onClickDeleteExam(exam: ExamModel) = viewModelScope.launch(Dispatchers.IO) {
        deleteExamUseCase(exam.id)
    }

    fun onClickModifyExamName() = updateState {
        state.value.copy(showNameEditorDialog = true)
    }

    fun onSelectExamName(exam: ExamModel, name: String) = viewModelScope.launch(Dispatchers.IO) {
        updateExamUseCase(exam.copy(name = name).asExternalModel())
    }

    fun onClickStartExamDialogConfirm() = updateState {
        state.value.copy(showStartExamDialog = true)
    }

    fun onClickStart(name: String) = viewModelScope.launch(Dispatchers.IO) {
        onCancelDialog()
        val subject = getSubjectByIdUseCase(subjectId)
        val exam = ExamModel(
            subjectId = subjectId,
            name = name,
            timeLimit = subject.timeLimit,
            createdAt = Clock.System.now()
        )
        val examId = insertExamUseCase(exam.asExternalModel())

        val questions = (1..subject.totalQuestionNumber).map {
            QuestionModel(
                subjectId = subjectId,
                examId = examId.toInt(),
                questionNumber = it,
                createdAt = Clock.System.now()
            )
        }.map {
            it.asExternalModel()
        }

        insertQuestionsUseCase(questions)

        _navigationEvent.emit(ExamRecord.getRouteWithArgs(subjectId, examId.toInt()))
    }

    fun onCancelDialog() {
        updateState {
            state.value.copy(
                showExamOptionDialog = false,
                showNameEditorDialog = false,
                showStartExamDialog = false
            )
        }
    }

    override fun createInitialState(): ExamsState {
        return ExamsState()
    }
}

data class ExamsState(
    val exams: List<ExamModel> = listOf(),
    val subjectTitle: String = "",
    val showExamOptionDialog: Boolean = false,
    val showNameEditorDialog: Boolean = false,
    val showStartExamDialog: Boolean = false,
    val toastState: ToastState = rememberToastState()
)
