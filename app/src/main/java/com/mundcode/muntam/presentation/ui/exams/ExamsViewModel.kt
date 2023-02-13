package com.mundcode.muntam.presentation.ui.exams

import androidx.lifecycle.viewModelScope
import com.mundcode.domain.usecase.DeleteExamUseCase
import com.mundcode.domain.usecase.GetExamsUseCase
import com.mundcode.domain.usecase.InsertExamUseCase
import com.mundcode.domain.usecase.UpdateExamUseCase
import com.mundcode.muntam.base.BaseViewModel
import com.mundcode.muntam.presentation.ui.model.ExamState
import com.mundcode.muntam.presentation.ui.model.asExternalModel
import com.mundcode.muntam.presentation.ui.model.asStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@HiltViewModel
class ExamsViewModel @Inject constructor(
    private val getExamsUseCase: GetExamsUseCase,
    private val insertExamUseCase: InsertExamUseCase,
    private val deleteExamUseCase: DeleteExamUseCase,
    private val updateExamUseCase: UpdateExamUseCase
) : BaseViewModel() {
    private val _exams = MutableSharedFlow<List<ExamState>>()
    val exams: SharedFlow<List<ExamState>> = _exams

    fun getExams(subjectId: Int) = viewModelScope.launch {
        getExamsUseCase(subjectId).collectLatest { list ->
            _exams.emit(list.map { it.asStateModel() })
        }
    }

    // todo https://stackoverflow.com/questions/29341380/sqlite-foreign-key-constraint-failed-code-787
    fun insertExam(subjectId: Int) = viewModelScope.launch {
        insertExamUseCase(
            ExamState(
                subjectId = subjectId,
                name = "신참이다",
                createdAt = Clock.System.now()
            ).asExternalModel()
        )
    }

    fun updateExam(examState: ExamState) = viewModelScope.launch {
        updateExamUseCase(examState.copy(name = "눌러짐").asExternalModel())
    }

    fun deleteExam(examState: ExamState) = viewModelScope.launch {
        deleteExamUseCase(examState.id)
    }
}
