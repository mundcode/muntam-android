package com.mundcode.domain.usecase

import com.mundcode.domain.model.Question
import com.mundcode.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetQuestionByQuestionNumberUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(examId: Int, questionNumber: Int): Flow<Question> =
        questionRepository.getQuestionByQuestionNumberFlow(
            examId = examId,
            questionNumber = questionNumber
        )
}
