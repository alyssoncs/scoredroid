package org.scoredroid.creatematch.domain.usecases

import org.scoredroid.creatematch.data.repository.MatchRepository
import org.scoredroid.creatematch.data.request.CreateMatchRequest
import org.scoredroid.creatematch.domain.response.MatchResponse


class CreateMatch(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(): MatchResponse {
        val emptyMatch = CreateMatchRequest(teams = emptyList())
        return matchRepository.createMatch(emptyMatch)
    }
}
