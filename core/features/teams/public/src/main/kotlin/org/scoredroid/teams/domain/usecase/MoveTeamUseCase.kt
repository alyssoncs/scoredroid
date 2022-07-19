package org.scoredroid.teams.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.teams.domain.request.AddTeamRequest

interface MoveTeamUseCase {
    suspend operator fun invoke(matchId: Long, teamAt: Int, moveTo: Int): Result<MatchResponse>
}