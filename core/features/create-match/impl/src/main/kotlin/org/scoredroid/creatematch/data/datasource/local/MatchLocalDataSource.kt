package org.scoredroid.creatematch.data.datasource.local

import org.scoredroid.creatematch.data.request.CreateMatchRequest
import org.scoredroid.creatematch.domain.response.MatchResponse

interface MatchLocalDataSource {
    suspend fun createMatch(createMatchRequest: CreateMatchRequest): MatchResponse
}