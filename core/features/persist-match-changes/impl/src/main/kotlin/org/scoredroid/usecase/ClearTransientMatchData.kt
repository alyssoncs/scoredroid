package org.scoredroid.usecase

import org.scoredroid.infra.dataaccess.repository.MatchRepository

class ClearTransientMatchData(
    private val matchRepository: MatchRepository,
) : ClearTransientMatchDataUseCase {
    override suspend fun invoke(matchId: Long): Result<Unit> {
        return matchRepository.clearTransientData(matchId)
    }
}
