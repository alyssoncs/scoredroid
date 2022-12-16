package org.scoredroid.match.domain.usecase

import org.scoredroid.infra.dataaccess.repository.MatchRepository

class ClearTransientMatchData(
    private val matchRepository: MatchRepository
) : ClearTransientMatchDataUseCase {
    override suspend fun invoke() {
        return matchRepository.clearTransientData()
    }
}
