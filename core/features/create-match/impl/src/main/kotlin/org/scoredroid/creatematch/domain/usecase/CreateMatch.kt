package org.scoredroid.creatematch.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class CreateMatch(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(): MatchResponse {
        val emptyMatch = CreateMatchRepositoryRequest(teams = emptyList())
        return matchRepository.createMatch(emptyMatch)
    }
}
