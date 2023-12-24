package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class RenameTeam(
    private val repository: MatchRepository,
) : RenameTeamUseCase {
    override suspend fun invoke(
        matchId: Long,
        teamAt: Int,
        newName: String,
    ): Result<MatchResponse> {
        val result = repository.getMatch(matchId)
        if (result.isFailure) return result.map(Match::toMatchResponse)

        return result
            .map { match ->
                runCatching {
                    match.renameTeam(teamAt, newName)
                }.getOrNull() ?: match
            }
            .mapCatching { match ->
                repository.updateMatch(match).getOrThrow()
            }
            .map(Match::toMatchResponse)
    }
}
