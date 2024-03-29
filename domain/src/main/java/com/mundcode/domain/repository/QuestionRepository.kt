package com.mundcode.domain.repository

import com.mundcode.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun insertQuestion(question: Question): Long

    suspend fun insertQuestions(questions: List<Question>)

    fun getQuestionsBySubjectId(subjectId: Int): List<Question>

    fun getQuestionsByExamIdFlow(examId: Int): Flow<List<Question>>

    fun getQuestionsByExamIdDescFlow(examId: Int): Flow<List<Question>>

    fun getQuestionByExamIdWrongFirst(examId: Int): Flow<List<Question>>

    fun getQuestionByExamId(examId: Int): List<Question>

    fun getQuestionByQuestionNumberFlow(examId: Int, questionNumber: Int): Flow<Question>

    suspend fun getQuestionByQuestionId(questionId: Int): Question

    fun getQuestionById(id: Int): Flow<Question>

    suspend fun updateQuestion(question: Question)
}
