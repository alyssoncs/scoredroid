package org.scoredroid.match

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.match.domain.usecase.ClearTransientMatchData
import org.scoredroid.match.domain.usecase.CreateMatch
import org.scoredroid.match.domain.usecase.GetMatchFlow
import org.scoredroid.match.domain.usecase.GetMatches
import org.scoredroid.match.domain.usecase.RemoveMatch
import org.scoredroid.match.domain.usecase.RenameMatch
import org.scoredroid.match.domain.usecase.SaveMatch

class MatchEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = MatchEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `createMatchUseCase has the correct instance`() {
        assertCorrectInstance<CreateMatch> { createMatchUseCase }
    }

    @Test
    fun `getMatchFlowUseCase has the correct instance`() {
        assertCorrectInstance<GetMatchFlow> { getMatchFlowUseCase }
    }

    @Test
    fun `getMatchesCase has the correct instance`() {
        assertCorrectInstance<GetMatches> { getMatchesUseCase }
    }

    @Test
    fun `removeMatchUseCase has the correct instance`() {
        assertCorrectInstance<RemoveMatch> { removeMatchUseCase }
    }

    @Test
    fun `renameMatchUseCase has the correct instance`() {
        assertCorrectInstance<RenameMatch> { renameMatchUseCase }
    }

    @Test
    fun `saveMatchUseCase has the correct instance`() {
        assertCorrectInstance<SaveMatch> { saveMatchUseCase }
    }

    @Test
    fun `clearTransientMatchDataUseCase has the correct instance`() {
        assertCorrectInstance<ClearTransientMatchData> { clearTransientMatchDataUseCase }
    }

    private inline fun <reified T>assertCorrectInstance(
        dependency: MatchEntrypoint.() -> Any
    ) {
        assertThat(entrypoint.dependency()).isInstanceOf(T::class.java)
    }
}
