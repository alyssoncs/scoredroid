package org.scoredroid.score

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.score.domain.usecase.DecrementScore
import org.scoredroid.score.domain.usecase.IncrementScore
import org.scoredroid.score.domain.usecase.ResetScore

class ScoreEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = ScoreEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `decrementScoreUseCase has the correct instance`() {
        assertCorrectInstance<DecrementScore> { decrementScoreUseCase }
    }

    @Test
    fun `incrementScoreUseCase has the correct instance`() {
        assertCorrectInstance<IncrementScore> { incrementScoreUseCase }
    }

    @Test
    fun `resetScoreUseCase has the correct instance`() {
        assertCorrectInstance<ResetScore> { resetScoreUseCase }
    }

    private inline fun <reified T>assertCorrectInstance(
        dependency: ScoreEntrypoint.() -> Any,
    ) {
        assertThat(entrypoint.dependency()).isInstanceOf(T::class.java)
    }
}
