package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class RenameMatch(private val repository: MatchRepository) : RenameMatchUseCase {
    override suspend fun invoke(matchId: Long, name: String): Result<MatchResponse> {
        val match = repository.getMatch(matchId) ?: return Result.failure(Throwable("Match not found"))

        return repository.updateMatch(match.rename(name))
            .map(Match::toMatchResponse)
    }
}
