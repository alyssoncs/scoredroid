package org.scoredroid.usecase

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.utils.mappers.toMatchResponse

internal class ScoreUpdater(
    private val matchRepository: MatchRepository,
) {
    suspend fun updateForTeam(
        matchId: Long,
        teamAt: Int,
        updateScore: (currentScore: Score) -> Score,
    ): Result<MatchResponse> {
        val result = matchRepository.getMatch(matchId)
        if (result.isFailure) return Result.failure(UpdateScoreError.MatchNotFound)

        return result
            .mapCatching { match ->
                matchRepository.updateMatch(match.updateScore(teamAt, updateScore)).getOrThrow()
            }
            .recoverCatching { throw UpdateScoreError.TeamNotFound }
            .map(Match::toMatchResponse)
    }
}
