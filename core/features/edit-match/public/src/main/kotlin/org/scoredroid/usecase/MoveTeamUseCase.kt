package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse

interface MoveTeamUseCase {
    suspend operator fun invoke(matchId: Long, teamAt: Int, moveTo: Int): Result<MatchResponse>
}
