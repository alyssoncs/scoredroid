package org.scoredroid.usecase

import app.cash.turbine.test
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import kotlin.properties.Delegates

class ClearTransientMatchDataTest {
    private val fixture = MatchRepositoryFixtureFactory.create()
    private val clearTransientData = ClearTransientMatchData(fixture.repository)

    @Nested
    inner class NoMatch {
        @Test
        fun `return failure`() = runTest {
            val result = clearTransientData(0L)

            result.isFailure.shouldBeTrue()
        }
    }

    @Nested
    inner class MatchExists {

        private var matchId by Delegates.notNull<Long>()

        @BeforeEach
        fun setUp() = runTest {
            matchId = fixture.createNamedMatch("old name").id
        }

        @Test
        fun `return success`() = runTest {
            val result = clearTransientData(matchId)

            result.isSuccess.shouldBeTrue()
        }

        @Test
        fun `clean the transient data`() = runTest {
            fixture.rebootApplication()
            fixture.renameMatch(matchId, "new name")
            fixture.repository.getMatch(matchId)!!.name shouldBe "new name"

            clearTransientData(matchId)

            fixture.repository.getMatch(matchId)!!.name shouldBe "old name"
        }

        @Test
        fun `flow is updated to persistent value`() = runTest {
            fixture.getMatchFlow(matchId).test {
                fixture.rebootApplication()
                fixture.renameMatch(matchId, "new name")

                clearTransientData(matchId)

                awaitItem()!!.name shouldBe "old name"
                awaitItem()!!.name shouldBe "new name"
                awaitItem()!!.name shouldBe "old name"
            }
        }
    }
}
