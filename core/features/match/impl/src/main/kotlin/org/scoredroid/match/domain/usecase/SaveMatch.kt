package org.scoredroid.match.domain.usecase

import org.scoredroid.infra.dataaccess.repository.MatchRepository

class SaveMatch(
    private val matchRepository: MatchRepository,
) : SaveMatchUseCase {
    override suspend fun invoke(matchId: Long): Result<Unit> {
        return matchRepository.persist(matchId)
    }
}
