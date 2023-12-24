package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class AddTeam(
    private val repository: MatchRepository,
) : AddTeamUseCase {
    override suspend fun invoke(matchId: Long, team: AddTeamRequest): Result<MatchResponse> {
        val match = repository.getMatch(matchId) ?: return Result.failure(Throwable("Match not found"))

        return repository.updateMatch(match.addTeam(Team(team.name, 0.toScore())))
            .map(Match::toMatchResponse)
    }
}
