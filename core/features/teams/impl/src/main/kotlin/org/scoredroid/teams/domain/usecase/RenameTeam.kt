package org.scoredroid.teams.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class RenameTeam(
    private val matchRepository: MatchRepository
) : RenameTeamUseCase {
    override suspend fun invoke(
        matchId: Long,
        teamAt: Int,
        newName: String,
    ): Result<MatchResponse> {
        return matchRepository.renameTeam(matchId, teamAt, newName)
            .map { it.toMatchResponse() }
    }
}
