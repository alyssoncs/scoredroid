package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class MoveTeam(
    private val repository: MatchRepository,
) : MoveTeamUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int, moveTo: Int): Result<MatchResponse> {
        val result = repository.getMatch(matchId)

        if (result.isFailure)
            return Result.failure(UpdateTeamError.MatchNotFound)

        return result
            .mapCatching { match ->
                match.moveTeam(teamAt, moveTo)
            }
            .recoverCatching {
                throw UpdateTeamError.TeamNotFound
            }
            .mapCatching { match ->
                repository.updateMatch(match).getOrThrow()
            }
            .map(Match::toMatchResponse)
    }
}
