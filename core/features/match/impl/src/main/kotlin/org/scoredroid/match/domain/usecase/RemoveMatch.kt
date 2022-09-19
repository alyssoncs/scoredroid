package org.scoredroid.match.domain.usecase

import org.scoredroid.infra.dataaccess.repository.MatchRepository

class RemoveMatch(
    private val matchRepository: MatchRepository
) : RemoveMatchUseCase {
    override suspend fun invoke(matchId: Long): Result<Unit> {
        return matchRepository.removeMatch(matchId)
    }
}
