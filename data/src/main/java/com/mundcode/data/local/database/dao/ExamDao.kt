package com.mundcode.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mundcode.data.local.database.model.ExamEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.Instant

@Dao
abstract class ExamDao : BaseDao<ExamEntity> {
    @Query(
        value = """
            SELECT * FROM exams WHERE deleted_at IS NULL AND subject_id = :subjectId
        """
    )
    abstract fun getExams(subjectId: Int): Flow<List<ExamEntity>>

    @Query(
        value = """
            SELECT * FROM exams WHERE deleted_at IS NULL AND is_favorite
        """
    )
    abstract fun getFavoriteExams(): Flow<List<ExamEntity>>

    @Query(
        value = """
            SELECT * FROM exams WHERE deleted_at IS NULL AND id = :examId 
        """
    )
    abstract suspend fun getExamById(examId: Int): ExamEntity

    @Query(
        value = """
            SELECT * FROM exams WHERE deleted_at IS NULL AND id = :id
        """
    )
    abstract fun getExamByIdFlow(id: Int): Flow<ExamEntity>

    fun getExamByIdDistinctUntilChanged(id: Int) = getExamByIdFlow(id).distinctUntilChanged()

    @Query(
        value = """
            UPDATE exams SET deleted_at = :deletedAt WHERE id = :id
        """
    )
    abstract suspend fun deleteExam(id: Int, deletedAt: Instant)

    @Query(
        value = """
            UPDATE questions SET deleted_at = :deletedAt WHERE exam_id = :id
        """
    )
    abstract suspend fun deletedQuestionsByExamId(id: Int, deletedAt: Instant) // todo test

    @Update
    abstract suspend fun updateExam(exam: ExamEntity)

    @Transaction
    open suspend fun deleteExamWithCasacade(id: Int, deletedAt: Instant) {
        deleteExam(id, deletedAt)
        deletedQuestionsByExamId(id, deletedAt)
    }
}
