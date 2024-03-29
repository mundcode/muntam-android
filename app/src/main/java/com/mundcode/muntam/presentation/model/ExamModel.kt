package com.mundcode.muntam.presentation.model

import androidx.compose.ui.graphics.Color
import com.mundcode.designsystem.theme.Gray500
import com.mundcode.designsystem.theme.MTRed
import com.mundcode.domain.model.Exam
import com.mundcode.domain.model.enums.ExamState
import com.mundcode.muntam.util.asCurrentTimerText
import com.mundcode.muntam.util.asMTDateText
import kotlin.math.absoluteValue
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class ExamModel( // todo 수정
    var id: Int = 0,
    val subjectId: Int = 0,
    val name: String = "",
    val isFavorite: Boolean = false,
    val timeLimit: Long = 0,
    val completeAd: Boolean = false,
    val createdAt: Instant = Clock.System.now(),
    val lastQuestionNumber: Int? = null, // 시험 기록 중 마지막으로 푼 문제
    val deletedAt: Instant? = null, // 소프트 딜리트 용도
    val lastAt: Long? = null, // (시험 종료이든 또는 중간에 나갔든) 마지막으로 기록된 시험진행 시간
    val endAt: Instant? = null, // 시험이 끝났을 때 시간
    val state: ExamState = ExamState.READY
) : Comparable<ExamModel> {
    val createdAtText: String = createdAt.asMTDateText()

    val expiredTimeText: String? = obtainExpiredTime()
    val expiredTimeTextColor: Color = obtainExpiredTimeTextColor()
    val lastLapsTimeText: String = lastAt?.asCurrentTimerText() ?: "00:00:00"

    private fun obtainExpiredTime(): String? {
        lastAt ?: return null
        val diff = timeLimit - lastAt
        if (diff >= 0) return null
        val absDiff = diff.absoluteValue
        val sec = (absDiff) % 60
        val min = (absDiff / 60) % 60
        val hour = (absDiff / 60 / 60) % 24

        return "%02d:%02d:%02d".format(hour, min, sec)
    }

    private fun obtainExpiredTimeTextColor(): Color {
        return when {
            lastAt == null -> Gray500
            timeLimit - lastAt < 0 -> MTRed
            else -> Gray500
        }
    }

    // todo sqlite 쿼리로 대체
    override fun compareTo(other: ExamModel): Int {
        return if (this.isFavorite != other.isFavorite) {
            if (this.isFavorite && other.isFavorite.not()) {
                -1
            } else if (this.isFavorite.not() && other.isFavorite) {
                1
            } else {
                0
            }
        } else {
            if (this.createdAt > other.createdAt) {
                -1
            } else if (this.createdAt < other.createdAt) {
                1
            } else {
                0
            }
        }
    }
}

fun Exam.asStateModel() = ExamModel( // todo 수정
    id = id,
    subjectId = subjectId,
    name = name,
    isFavorite = isFavorite,
    timeLimit = timeLimit,
    completeAd = completeAd,
    createdAt = createdAt,
    endAt = endAt,
    lastAt = lastAt,
    lastQuestionNumber = lastQuestionNumber,
    deletedAt = deletedAt,
    state = state
)

fun ExamModel.asExternalModel(): Exam = Exam( // todo 수정
    id = id,
    subjectId = subjectId,
    name = name,
    isFavorite = isFavorite,
    timeLimit = timeLimit,
    completeAd = completeAd,
    createdAt = createdAt,
    endAt = endAt,
    lastAt = lastAt,
    lastQuestionNumber = lastQuestionNumber,
    deletedAt = deletedAt,
    state = state
)

fun createExamModels(
    size: Int,
    subjectId: Int = 0
) = (1..size).map {
    createExamModel(
        id = it,
        subjectId = subjectId,
    )
}

fun createExamModel(
    id: Int,
    subjectId: Int,
    completeAd: Boolean = false
) = ExamModel(
    id = id,
    subjectId = subjectId,
    name = "테스트 시험 이름 : $id",
    createdAt = Clock.System.now(),
    timeLimit = id * 100000L,
    completeAd = completeAd
)
