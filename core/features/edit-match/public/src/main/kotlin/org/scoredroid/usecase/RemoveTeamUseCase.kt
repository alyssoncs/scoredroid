package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse

interface RemoveTeamUseCase {
    suspend operator fun invoke(matchId: Long, teamAt: Int): Result<MatchResponse>
}
