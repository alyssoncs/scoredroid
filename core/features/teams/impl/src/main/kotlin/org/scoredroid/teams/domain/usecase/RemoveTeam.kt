package org.scoredroid.teams.domain.usecase

import org.score.droid.utils.mappers.toMatchResponse
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class RemoveTeam(
    private val matchRepository: MatchRepository
) : RemoveTeamUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int): Result<MatchResponse> {
        return matchRepository.removeTeam(matchId, teamAt).map { it.toMatchResponse() }
    }
}
