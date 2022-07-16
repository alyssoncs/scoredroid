package org.scoredroid.creatematch.domain.response

//TODO: move this class to common module
data class MatchResponse(
    val id: Long,
    val teams: List<TeamResponse>,
)

data class TeamResponse(val name: String)
