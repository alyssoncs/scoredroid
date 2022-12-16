package org.scoredroid.teams.domain.usecase

import org.scoredroid.data.response.MatchResponse

interface RenameTeamUseCase {
    suspend operator fun invoke(matchId: Long, teamAt: Int, newName: String): Result<MatchResponse>
}
