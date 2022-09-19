package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import kotlin.properties.Delegates

@OptIn(ExperimentalCoroutinesApi::class)
class SaveMatchTest {
    private val fixture = MatchRepositoryFixtureFactory.create()
    private val saveMatch = SaveMatch(fixture.repository)

    @Nested
    inner class NoMatch {
        @Test
        fun `return failure`() = runTest {
            val result = saveMatch(0L)

            assertThat(result.isFailure).isTrue()
        }
    }

    @Nested
    inner class MatchExists {
        private var matchId by Delegates.notNull<Long>()

        @BeforeEach
        internal fun setUp() = runTest {
            matchId = fixture.createEmptyMatch().id
        }

        @Test
        fun `return success`() = runTest {
            val result = saveMatch(matchId)

            assertThat(result.isSuccess).isTrue()
        }

        @Test
        fun `saves the match`() = runTest {
            fixture.repository.renameMatch(matchId, "new name")
            saveMatch(matchId)
            fixture.clearInMemoryData()

            assertThat(fixture.repository.getMatch(matchId)!!.name).isEqualTo("new name")
        }
    }
}