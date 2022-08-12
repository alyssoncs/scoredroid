package org.scoredroid.teams.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.teams.domain.request.AddTeamRequest
import org.scoredroid.utils.mappers.toMatchResponse

class AddTeam(
    private val matchRepository: MatchRepository
) : AddTeamUseCase {
    override suspend fun invoke(matchId: Long, team: AddTeamRequest): Result<MatchResponse> {
        return matchRepository.addTeam(matchId, AddTeamRepositoryRequest(name = team.name))
            .map { it.toMatchResponse() }
    }
}
