package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.TeamRequest
import org.scoredroid.utils.mappers.toMatchResponse

class CreateMatch(
    private val matchRepository: MatchRepository,
) : CreateMatchUseCase {
    override suspend fun invoke(createMatchOptions: CreateMatchRequestOptions): MatchResponse {
        val createMatchRequest = CreateMatchRepositoryRequest(
            name = createMatchOptions.matchName,
            teams = createMatchOptions.teams.map { TeamRequest(it.name) },
        )
        return matchRepository.createMatch(createMatchRequest).toMatchResponse()
    }
}
