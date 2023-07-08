package org.scoredroid.teams.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class RemoveTeam(
    private val matchRepository: MatchRepository,
) : RemoveTeamUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int): Result<MatchResponse> {
        return matchRepository.removeTeam(matchId, teamAt).map { it.toMatchResponse() }
    }
}
