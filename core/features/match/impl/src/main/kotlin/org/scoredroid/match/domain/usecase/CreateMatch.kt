package org.scoredroid.match.domain.usecase

import org.score.droid.utils.mappers.toMatchResponse
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class CreateMatch(
    private val matchRepository: MatchRepository
) : CreateMatchUseCase {
    override suspend fun invoke(): MatchResponse {
        val emptyMatch = CreateMatchRepositoryRequest(teams = emptyList())
        return matchRepository.createMatch(emptyMatch).toMatchResponse()
    }
}
