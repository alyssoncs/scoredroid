package org.scoredroid.domain.entities

import org.scoredroid.domain.entities.Score.Companion.toScore
import java.lang.Integer.max

@JvmInline
value class Score private constructor(val intValue: Int) {
    init {
        require(intValue >= 0) { "A ${this::class.simpleName} cannot be negative, but was $intValue" }
    }

    operator fun plus(that: Int) = this + that.toScore()
    operator fun plus(that: Score) = (this.intValue + that.intValue).toScore()
    operator fun minus(that: Int) = this - that.toScore()
    operator fun minus(that: Score) = (this.intValue - that.intValue).toScore()
    operator fun times(that: Int) = this * that.toScore()
    operator fun times(that: Score) = (this.intValue * that.intValue).toScore()

    companion object {
        fun Int.toScore(): Score {
            return Score(max(this, 0))
        }
    }
}

fun Score?.orZero(): Score {
    return this ?: 0.toScore()
}
