package org.scoredroid.score.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.scoredroid.infra.dataaccess.repository.MatchRepository

@ExperimentalCoroutinesApi
class IncrementScoreTest : UpdateScoreTest() {
    override fun updateStrategy(currentScore: Int, updateAmount: Int) = currentScore + updateAmount

    override fun createUpdateScoreUseCase(repository: MatchRepository): UpdateScore {
        return IncrementScore(ScoreUpdater(repository))::invoke
    }
}