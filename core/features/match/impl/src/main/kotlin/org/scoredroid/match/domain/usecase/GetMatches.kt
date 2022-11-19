package org.scoredroid.match.domain.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

class GetMatches(private val repository: MatchRepository) : GetMatchesUseCase {
    override suspend fun invoke(): List<MatchResponse> {
        return repository.getMatches()
            .sortedByDescending(Match::id)
            .map(Match::toMatchResponse)
    }
}
