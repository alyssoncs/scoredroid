package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class RemoveTeam(
    private val repository: MatchRepository,
) : RemoveTeamUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int): Result<MatchResponse> {
        val result = repository.getMatch(matchId)
        if (result.isFailure)
            return result.map(Match::toMatchResponse)

        return result
            .map { match ->
                runCatching {
                    match.removeTeam(teamAt)
                }.getOrNull() ?: match
            }
            .mapCatching { match ->
                repository.updateMatch(match).getOrThrow()
            }
            .map(Match::toMatchResponse)
    }
}
