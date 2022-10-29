package org.scoredroid.infra.dataaccess.datasource.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.dao.MatchDao
import org.scoredroid.infra.dataaccess.database.MatchDatabase
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.TeamRequest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MatchDaoToPersistentMatchDataSourceAdapterTest {
    private lateinit var dao: MatchDao
    private lateinit var db: MatchDatabase
    private lateinit var dataSourceAdapter: MatchDaoToPersistentMatchDataSourceAdapter

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MatchDatabase::class.java).build()
        dao = db.matchDao()
        dataSourceAdapter = MatchDaoToPersistentMatchDataSourceAdapter(dao)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun createMatch_createsMatchCorrectly() = runBlocking {
        val request = createMatchRequest()
            .withMatchName("match name")
            .withTeams("team a", "team b")
            .build()


        val match = dataSourceAdapter.createMatch(request)

        assertMatchWasMappedCorrectly(match, request)
    }

    @Test
    fun getMatch_matchNotFound() = runBlocking {
        val match = dataSourceAdapter.getMatch(1)

        assertThat(match).isNull()
    }

    @Test
    fun getMatch_matchFound() = runBlocking {
        val request = createMatchRequest().build()
        dataSourceAdapter.createMatch(request)

        val match = dataSourceAdapter.getMatch(1)

        assertThat(match).isNotNull()
        assertMatchWasMappedCorrectly(match!!, request)
    }

    @Test
    fun save_updateNonExistingMatch() = runBlocking {
        val match = Match(
            id = 5,
            name = "a brand new match",
            teams = listOf(
                Team(name = "team a", score = 5.toScore())
            ),
        )

        val result = dataSourceAdapter.save(match)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()!!.message).isNotEmpty()
    }

    @Test
    fun save_noRealUpdate() = runBlocking {
        val request = createMatchRequest().build()
        val oldMatch = dataSourceAdapter.createMatch(request)

        val result = dataSourceAdapter.save(oldMatch.copy())

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            assertThat(newMatch).isEqualTo(oldMatch)
        }
    }

    @Test
    fun save_updateMatchName() = runBlocking {
        val request = createMatchRequest()
            .withMatchName("old match name")
            .build()
        val oldMatch = dataSourceAdapter.createMatch(request)

        val result = dataSourceAdapter.save(oldMatch.copy(name = "new match name"))

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            assertThat(newMatch.name).isNotEqualTo(oldMatch.name)
        }
    }

    @Test
    fun save_updateTeamNames() = runBlocking {
        val request = createMatchRequest()
            .withTeams("team a", "team b")
            .build()
        val oldMatch = dataSourceAdapter.createMatch(request)

        val result = dataSourceAdapter.save(
            oldMatch.copy(
                teams = oldMatch.teams.mapIndexed { idx, team ->
                    team.copy(name = "team $idx")
                }
            )
        )

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            assertThat(newMatch.teams).hasSize(oldMatch.teams.size)
            assertThat(newMatch.teams[0].name).isEqualTo("team 0")
            assertThat(newMatch.teams[1].name).isEqualTo("team 1")
        }
    }

    @Test
    fun save_updateTeamScore() = runBlocking {
        val request = createMatchRequest()
            .withTeams("team a", "team b")
            .build()
        val oldMatch = dataSourceAdapter.createMatch(request)

        val result = dataSourceAdapter.save(
            oldMatch.copy(
                teams = oldMatch.teams.mapIndexed { idx, team ->
                    team.copy(score = idx.inc().toScore())
                }
            )
        )

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            assertThat(newMatch.teams).hasSize(oldMatch.teams.size)
            assertThat(newMatch.teams[0].score).isEqualTo(1.toScore())
            assertThat(newMatch.teams[1].score).isEqualTo(2.toScore())
        }
    }

    @Test
    fun save_updateTeamOrder() = runBlocking {
        val request = createMatchRequest()
            .withTeams("team a", "team b")
            .build()
        val oldMatch = dataSourceAdapter.createMatch(request)

        val result = dataSourceAdapter.save(
            oldMatch.copy(
                teams = oldMatch.teams.reversed()
            )
        )

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            assertThat(newMatch.teams).hasSize(oldMatch.teams.size)
            assertThat(newMatch.teams[0]).isEqualTo(oldMatch.teams[1])
            assertThat(newMatch.teams[1]).isEqualTo(oldMatch.teams[0])
        }
    }

    @Test
    fun save_additionalTeams() = runBlocking {
        val request = createMatchRequest()
            .withTeams("team 1", "team 2")
            .build()
        val oldMatch = dataSourceAdapter.createMatch(request)

        val additionalTeam = Team(name = "team 1.5", score = 2.toScore())
        val result = dataSourceAdapter.save(
            oldMatch.copy(
                teams = listOf(
                    oldMatch.teams.first() ,
                    additionalTeam,
                    oldMatch.teams.last(),
                )
            )
        )

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            assertThat(newMatch.teams).hasSize(oldMatch.teams.size.inc())
            assertThat(newMatch.teams[0]).isEqualTo(oldMatch.teams[0])
            assertThat(newMatch.teams[1]).isEqualTo(additionalTeam)
            assertThat(newMatch.teams[2]).isEqualTo(oldMatch.teams[1])
        }
    }

    @Test
    fun save_missingTeams() = runBlocking {
        val request = createMatchRequest()
            .withTeams("team 1", "team to be deleted", "team 2")
            .build()
        val oldMatch = dataSourceAdapter.createMatch(request)

        val result = dataSourceAdapter.save(
            oldMatch.copy(
                teams = listOf(oldMatch.teams.first(), oldMatch.teams.last())
            )
        )

        assertMatchWasUpdatedCorrectly(oldMatch, result) { newMatch ->
            assertThat(newMatch.teams).hasSize(oldMatch.teams.size.dec())
            assertThat(newMatch.teams[0]).isEqualTo(oldMatch.teams[0])
            assertThat(newMatch.teams[1]).isEqualTo(oldMatch.teams[2])
        }
    }

    private fun assertMatchWasMappedCorrectly(
        match: Match,
        request: CreateMatchRepositoryRequest
    ) {
        assertThat(match.id).isNotNull()
        assertThat(match.name).isEqualTo(request.name)
        assertThat(match.teams).hasSize(request.teams.size)
        request.teams.forEachIndexed { idx, team ->
            assertThat(match.teams[idx].name).isEqualTo(team.name)
            assertThat(match.teams[idx].score.intValue).isEqualTo(0)
        }
    }

    private suspend fun assertMatchWasUpdatedCorrectly(
        oldMatch: Match,
        result: Result<Unit>,
        assert: (Match) -> Unit
    ) {
        val newMatch = dataSourceAdapter.getMatch(oldMatch.id)!!
        assertThat(result.isSuccess).isTrue()
        assert(newMatch)
    }

    class CreateMatchRepositoryRequestBuilder() {
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

