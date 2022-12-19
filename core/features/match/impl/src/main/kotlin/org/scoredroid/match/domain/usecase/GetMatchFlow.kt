package org.scoredroid.match.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class GetMatchFlow(private val repository: MatchRepository) : GetMatchFlowUseCase {
    override suspend fun invoke(matchId: Long): Flow<MatchResponse?> {
        return repository.getMatchFlow(matchId)
            .map { it?.toMatchResponse() }
    }
}
