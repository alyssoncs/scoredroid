package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse

interface AddTeamUseCase {
    suspend operator fun invoke(matchId: Long, team: AddTeamRequest): Result<MatchResponse>
}

data class AddTeamRequest(
    val name: String,
)
