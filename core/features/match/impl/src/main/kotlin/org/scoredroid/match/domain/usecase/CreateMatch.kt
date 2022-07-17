package org.scoredroid.match.domain.usecase

import org.score.droid.utils.mappers.toMatchResponse
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.match.domain.request.CreateMatchRequestOptions

class CreateMatch(
    private val matchRepository: MatchRepository
) : CreateMatchUseCase {
    override suspend fun invoke(createMatchOptions: CreateMatchRequestOptions): MatchResponse {
        val emptyMatch = CreateMatchRepositoryRequest(teams = emptyList())
        return matchRepository.createMatch(emptyMatch).toMatchResponse()
    }
}
