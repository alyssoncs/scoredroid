package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class RenameMatch(private val repository: MatchRepository) : RenameMatchUseCase {
    override suspend fun invoke(matchId: Long, name: String): Result<MatchResponse> {
        return repository.renameMatch(matchId, name).map { it.toMatchResponse() }
    }
}
