package com.mundcode.domain.usecase

import com.mundcode.domain.model.Question
import com.mundcode.domain.model.enums.QuestionSort
import com.mundcode.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetQuestionsByExamIdWithSortFlowUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(examId: Int, sort: QuestionSort): Flow<List<Question>> =
        when (sort) {
            QuestionSort.LAPS_DESC -> {
                questionRepository.getQuestionsByExamIdDescFlow(examId)
            }
            QuestionSort.WRONG_FIRST -> {
                questionRepository.getQuestionByExamIdWrongFirst(examId)
            }
            QuestionSort.DEFAULT -> {
                questionRepository.getQuestionsByExamIdFlow(examId)
            }
        }
}
