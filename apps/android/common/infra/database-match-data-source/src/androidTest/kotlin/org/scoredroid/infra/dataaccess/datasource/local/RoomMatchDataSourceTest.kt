package org.scoredroid.infra.dataaccess.datasource.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.database.MatchDatabase
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.TeamRequest

@RunWith(AndroidJUnit4::class)
class RoomMatchDataSourceTest {
    private lateinit var db: MatchDatabase
    private lateinit var dataSource: RoomMatchDataSource

    @Before
    fun setUp() {
        fun getContext() = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(getContext(), MatchDatabase::class.java).build()
        dataSource = RoomMatchDataSource(db.matchDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun createMatch_createsMatchCorrectly() = runTest {
        val request = createMatchRequest().build()

        val match = dataSource.createMatch(request)

        assertMatchWasMappedCorrectly(match, request)
    }

    @Test
    fun getMatch_matchNotFound() = runTest {
        val match = dataSource.getMatch(1)

        match.shouldBeNull()
    }

    @Test
    fun getMatch_matchFound() = runTest {
        val request = createMatchRequest().build()
        dataSource.createMatch(request)

        val match = dataSource.getMatch(1)

        match.shouldNotBeNull()
        assertMatchWasMappedCorrectly(match, request)
    }

    @Test
    fun getAllMatches_noMatches() = runTest {
        val matches = dataSource.getAllMatches()

        matches.shouldBeEmpty()
    }

    @Test
    fun getAllMatches_returnAllMatches() = runTest {
        val match01Request = createMatchRequest()
            .withMatchName("match 01")
            .build()
        dataSource.createMatch(match01Request)
        val match02Request = createMatchRequest()
            .withMatchName("match 02")
            .build()
        dataSource.createMatch(match02Request)

        val matches = dataSource.getAllMatches()

        matches shouldHaveSize 2
        matches.find { it.name == "match 01" }.shouldNotBeNull()
        matches.find { it.name == "match 02" }.shouldNotBeNull()
    }

    @Test
    fun save_updateNonExistingMatch() = runTest {
        val match = Match(
            id = 5,
            name = "a brand new match",
            teams = listOf(
                Team(name = "team a", score = 5.toScore()),
            ),
        )

        val result = dataSource.save(match)

        result.isFailure.shouldBeTrue()
        result.exceptionOrNull()!!.message.shouldNotBeEmpty()
    }

    @Test
    fun save_noRealUpdate() = runTest {
        val oldMatch = dataSource.createMatch(createMatchRequest().build())

        val result = dataSource.save(oldMatch.copy())

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            newMatch shouldBe oldMatch
        }
    }

    @Test
    fun save_updateMatchName() = runTest {
        val request = createMatchRequest()
            .withMatchName("old match name")
            .build()
        val oldMatch = dataSource.createMatch(request)

        val result = dataSource.save(oldMatch.copy(name = "new match name"))

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            newMatch.name shouldNotBe oldMatch.name
        }
    }

    @Test
    fun save_updateTeamNames() = runTest {
        val request = createMatchRequest()
            .withTeams("team a", "team b")
            .build()
        val oldMatch = dataSource.createMatch(request)

        val result = dataSource.save(
            oldMatch.copy(
                teams = oldMatch.teams.mapIndexed { idx, team ->
                    team.copy(name = "team $idx")
                },
            ),
        )

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            newMatch.teams shouldHaveSize oldMatch.teams.size
            newMatch.teams[0].name shouldBe "team 0"
            newMatch.teams[1].name shouldBe "team 1"
        }
    }

    @Test
    fun save_updateTeamScore() = runTest {
        val request = createMatchRequest()
            .withTeams("team a", "team b")
            .build()
        val oldMatch = dataSource.createMatch(request)

        val result = dataSource.save(
            oldMatch.copy(
                teams = oldMatch.teams.mapIndexed { idx, team ->
                    team.copy(score = idx.inc().toScore())
                },
            ),
        )

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            newMatch.teams shouldHaveSize oldMatch.teams.size
            newMatch.teams[0].score shouldBe 1.toScore()
            newMatch.teams[1].score shouldBe 2.toScore()
        }
    }

    @Test
    fun save_updateTeamOrder() = runTest {
        val request = createMatchRequest()
            .withTeams("team a", "team b")
            .build()
        val oldMatch = dataSource.createMatch(request)

        val result = dataSource.save(
            oldMatch.copy(
                teams = oldMatch.teams.reversed(),
            ),
        )

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            newMatch.teams shouldHaveSize oldMatch.teams.size
            newMatch.teams[0] shouldBe oldMatch.teams[1]
            newMatch.teams[1] shouldBe oldMatch.teams[0]
        }
    }

    @Test
    fun save_additionalTeams() = runTest {
        val request = createMatchRequest()
            .withTeams("team 1", "team 2")
            .build()
        val oldMatch = dataSource.createMatch(request)

        val additionalTeam = Team(name = "team 1.5", score = 2.toScore())
        val result = dataSource.save(
            oldMatch.copy(
                teams = listOf(
                    oldMatch.teams.first(),
                    additionalTeam,
                    oldMatch.teams.last(),
                ),
            ),
        )

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            newMatch.teams shouldHaveSize oldMatch.teams.size.inc()
            newMatch.teams[0] shouldBe oldMatch.teams[0]
            newMatch.teams[1] shouldBe additionalTeam
            newMatch.teams[2] shouldBe oldMatch.teams[1]
        }
    }

    @Test
    fun save_missingTeams() = runTest {
        val request = createMatchRequest()
            .withTeams("team 1", "team to be deleted", "team 2")
            .build()
        val oldMatch = dataSource.createMatch(request)

        val result = dataSource.save(
            oldMatch.copy(
                teams = listOf(oldMatch.teams.first(), oldMatch.teams.last()),
            ),
        )

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            newMatch.teams shouldHaveSize oldMatch.teams.size.dec()
            newMatch.teams[0] shouldBe oldMatch.teams[0]
            newMatch.teams[1] shouldBe oldMatch.teams[2]
        }
    }

    @Test
    fun removeMatch_matchNotFound() = runTest {
        val result = dataSource.removeMatch(1)

        result.isFailure.shouldBeTrue()
        result.exceptionOrNull()!!.message.shouldNotBeEmpty()
    }

    @Test
    fun removeMatch_existingMatch() = runTest {
        val match = dataSource.createMatch(createMatchRequest().build())

        val result = dataSource.removeMatch(match.id)

        result.isSuccess.shouldBeTrue()
        dataSource.getMatch(match.id).shouldBeNull()
    }

    private fun assertMatchWasMappedCorrectly(
        match: Match,
        request: CreateMatchRepositoryRequest,
    ) {
        match.id.shouldNotBeNull()
        match.name shouldBe request.name
        match.teams shouldHaveSize request.teams.size
        request.teams.forEachIndexed { idx, team ->
            match.teams[idx].name shouldBe team.name
            match.teams[idx].score.intValue shouldBe 0
        }
    }

    private suspend fun assertMatchWasUpdatedCorrectly(
        oldMatch: Match,
        result: Result<Unit>,
        assert: (Match) -> Unit,
    ) {
        val newMatch = dataSource.getMatch(oldMatch.id)!!
        result.isSuccess.shouldBeTrue()
        assert(newMatch)
    }

    class CreateMatchRepositoryRequestBuilder {
        private var matchName = "match name"
        private var teams = listOf("team 01, team 02")

        fun withMatchName(name: String): CreateMatchRepositoryRequestBuilder {
            matchName = name
            return this
        }

        fun withTeams(vararg teamNames: String): CreateMatchRepositoryRequestBuilder {
            teams = teamNames.toList()
            return this
        }

        fun build(): CreateMatchRepositoryRequest {
            return CreateMatchRepositoryRequest(matchName, teams.map(::TeamRequest))
        }
    }

    private fun createMatchRequest() = CreateMatchRepositoryRequestBuilder()
}
