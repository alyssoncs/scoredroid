package org.scoredroid.creatematch.domain.response

data class MatchResponse(
    val id: Long,
    val teams: List<TeamResponse>,
)

class TeamResponse
