package org.scoredroid.domain.entities

import java.lang.Integer.max

@JvmInline
value class Score private constructor(private val qty: Int) {
    init {
        require(qty >= 0) { "A ${this::class.simpleName} cannot be negative, but was $qty"}
    }

    companion object {
        fun of(quantity: Int): Score {
            return Score(max(quantity, 0))
        }
    }
}