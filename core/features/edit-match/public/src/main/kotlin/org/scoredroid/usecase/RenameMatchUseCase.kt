package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse

interface RenameMatchUseCase {
    suspend operator fun invoke(matchId: Long, name: String): Result<MatchResponse>
}
