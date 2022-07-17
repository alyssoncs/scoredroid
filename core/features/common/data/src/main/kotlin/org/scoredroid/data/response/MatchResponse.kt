package org.scoredroid.data.response

data class MatchResponse(
    val id: Long,
    val name: String,
    val teams: List<TeamResponse>,
)

data class TeamResponse(
    val name: String,
    val score: Int,
)
