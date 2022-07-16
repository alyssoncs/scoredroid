package org.scoredroid.teams.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.teams.domain.request.AddTeamRequest

interface AddTeamUseCase {
    suspend operator fun invoke(matchId: Long, team: AddTeamRequest): Result<MatchResponse>
}