package org.scoredroid.usecase

import app.cash.turbine.test
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.scoredroid.infra.test.assertions.dataaccess.repository.assertMatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import kotlin.properties.Delegates

class RenameMatchTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val renameMatch = RenameMatch(fixture.repository)

    @Nested
    inner class MatchNotFound {

        @Test
        fun `return error`() = runTest {
            val nonExistingMatchId = 0L

            val result = renameMatch(nonExistingMatchId, "irrelevant")

            result.isFailure.shouldBeTrue()
        }
    }

    @Nested
    inner class MatchFound {

        private var matchId by Delegates.notNull<Long>()

        private val oldName = "old name"
        private val newName = "new name"

        @BeforeEach
        fun setUp() = runTest {
            matchId = fixture.createNamedMatch(matchName = oldName).id
        }

        @ParameterizedTest
        @ValueSource(booleans = [true, false])
        fun `return renamed match`(rebootApplication: Boolean) = runTest {
            if (rebootApplication) fixture.rebootApplication()

            val result = renameMatch(matchId, newName)

            assertMatchResponse(fixture, result) { match ->
                match.name shouldBe newName
            }
        }

        @Test
        fun `flow is updated`() = runTest {
            fixture.getMatchFlow(matchId).test {
                renameMatch(matchId, newName)

                awaitItem()!!.name shouldBe oldName
                awaitItem()!!.name shouldBe newName
            }
        }
    }
}
