package org.scoredroid.teams.domain.usecase

import org.score.droid.utils.mappers.toMatchResponse
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.error.TeamOperationError
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class MoveTeam(
    private val repository: MatchRepository
) : MoveTeamUseCase {
    override suspend fun invoke(matchId: Long, teamAt: Int, moveTo: Int): Result<MatchResponse> {
        val result = repository.moveTeam(matchId, teamAt, moveTo)
        val exception = result.exceptionOrNull()
            when (exception) {
                TeamOperationError.MatchNotFound -> UpdateTeamError.MatchNotFound
                TeamOperationError.TeamNotFound -> UpdateTeamError.TeamNotFound
                else -> null
            }?.let {
                return Result.failure(it)
            }
        return result.map { it.toMatchResponse() }
    }
}