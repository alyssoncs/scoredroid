package org.scoredroid.domain.entities

import java.lang.Integer.max

@JvmInline
value class Quantity private constructor(private val qty: Int) {
    init {
        require(qty >= 0) { "A ${this::class.simpleName} cannot be negative, but was $qty"}
    }

    companion object {
        fun of(quantity: Int): Quantity {
            return Quantity(max(quantity, 0))
        }
    }
}