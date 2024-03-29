package org.scoredroid.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class GetMatchesFlow(private val repository: MatchRepository) : GetMatchesFlowUseCase {
    override suspend fun invoke(): Flow<List<MatchResponse>> {
        return repository.getMatchesFlow()
            .map {
                it.sortedByDescending(Match::id)
                    .map(Match::toMatchResponse)
            }
    }
}
