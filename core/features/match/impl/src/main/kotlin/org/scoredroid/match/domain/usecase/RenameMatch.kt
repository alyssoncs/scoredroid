package org.scoredroid.match.domain.usecase

import org.score.droid.utils.mappers.toMatchResponse
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class RenameMatch(private val repository: MatchRepository) : RenameMatchUseCase {
    override suspend fun invoke(matchId: Long, name: String): Result<MatchResponse> {
        return repository.renameMatch(matchId, name).map { it.toMatchResponse() }
    }
}
