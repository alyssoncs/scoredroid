package org.scoredroid.creatematch.data.repository

import org.scoredroid.creatematch.data.datasource.local.MatchLocalDataSource
import org.scoredroid.creatematch.data.request.CreateMatchRequest
import org.scoredroid.creatematch.domain.response.MatchResponse

class MatchRepository(
    private val matchLocalDataSource: MatchLocalDataSource
) {
    suspend fun createMatch(createMatchRequest: CreateMatchRequest): MatchResponse {
        return matchLocalDataSource.createMatch(createMatchRequest)
    }
}