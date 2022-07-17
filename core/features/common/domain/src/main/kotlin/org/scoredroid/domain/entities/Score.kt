package org.scoredroid.domain.entities

import java.lang.Integer.max

@JvmInline
value class Score private constructor(val intValue: Int) {
    init {
        require(intValue >= 0) { "A ${this::class.simpleName} cannot be negative, but was $intValue"}
    }

    companion object {
        fun Int.toScore(): Score {
            return Score(max(this, 0))
        }
    }
}