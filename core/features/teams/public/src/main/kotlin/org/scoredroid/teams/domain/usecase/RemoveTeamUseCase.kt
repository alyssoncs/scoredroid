package org.scoredroid.teams.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.teams.domain.request.AddTeamRequest

interface RemoveTeamUseCase {
    suspend operator fun invoke(matchId: Long, teamAt: Int): Result<MatchResponse>
}
