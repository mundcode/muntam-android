package com.mundcode.domain.repository

import com.mundcode.domain.model.Exam
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface ExamRepository {
    suspend fun insertExams(exmas: List<Exam>)

    fun getExams(): Flow<List<Exam>>

    suspend fun deleteExams(ids: List<Int>)
}