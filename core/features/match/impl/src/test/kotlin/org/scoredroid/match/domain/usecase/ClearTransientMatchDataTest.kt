package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import kotlin.properties.Delegates

@OptIn(ExperimentalCoroutinesApi::class)
class ClearTransientMatchDataTest {
    private val fixture = MatchRepositoryFixtureFactory.create()
    private val cleanTransientData = ClearTransientMatchData(fixture.repository)

    private var matchId by Delegates.notNull<Long>()

    @BeforeEach
    internal fun setUp() = runTest {
        matchId = fixture.createNamedMatch("old name").id
        fixture.rebootApplication()
    }

    @Test
    fun `clean the transient data`() = runTest {
        fixture.repository.renameMatch(matchId, "new name")
        assertThat(fixture.repository.getMatch(matchId)!!.name).isEqualTo("new name")

        cleanTransientData()

        assertThat(fixture.repository.getMatch(matchId)!!.name).isEqualTo("old name")
    }
}