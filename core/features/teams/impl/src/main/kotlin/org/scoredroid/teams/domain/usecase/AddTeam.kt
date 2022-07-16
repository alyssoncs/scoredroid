package org.scoredroid.teams.domain.usecase

import org.scoredroid.creatematch.data.repository.AddTeamRepositoryRequest
import org.scoredroid.creatematch.data.repository.MatchRepository
import org.scoredroid.creatematch.domain.response.MatchResponse
import org.scoredroid.teams.domain.request.AddTeamRequest

class AddTeam(
    private val matchRepository: MatchRepository
) : AddTeamUseCase {
    override suspend fun invoke(matchId: Long, team: AddTeamRequest): Result<MatchResponse> {
        return matchRepository.addTeam(matchId, AddTeamRepositoryRequest(name = team.name))
    }
}