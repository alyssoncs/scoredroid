package org.score.droid.utils.mappers

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.domain.entities.Match

fun Match.toMatchResponse() = MatchResponse(
    id = id,
    teams = teams.map { TeamResponse(it.name, it.score.intValue) }
)
