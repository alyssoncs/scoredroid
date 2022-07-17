package org.scoredroid.teams.domain.usecase

import org.score.droid.utils.mappers.toMatchResponse
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.teams.domain.request.AddTeamRequest

class AddTeam(
    private val matchRepository: MatchRepository
) : AddTeamUseCase {
    override suspend fun invoke(matchId: Long, team: AddTeamRequest): Result<MatchResponse> {
        return matchRepository.addTeam(matchId, AddTeamRepositoryRequest(name = team.name))
            .map { it.toMatchResponse() }
    }
}