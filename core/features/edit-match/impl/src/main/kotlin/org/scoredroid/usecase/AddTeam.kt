package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class AddTeam(
    private val repository: MatchRepository,
) : AddTeamUseCase {
    override suspend fun invoke(matchId: Long, team: AddTeamRequest): Result<MatchResponse> {
        return repository.getMatch(matchId)
            .mapCatching { match ->
                repository.updateMatch(match.addTeam(team.name)).getOrThrow()
            }
            .map(Match::toMatchResponse)
    }
}
