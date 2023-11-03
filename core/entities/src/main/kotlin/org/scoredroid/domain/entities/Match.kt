package org.scoredroid.domain.entities

data class Match(
    val id: Long,
    val name: String,
    val teams: List<Team>,
)
