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

        val newMatch = dataSourceAdapter.getMatch(oldMatch.id)
        assertThat(result.isSuccess).isTrue()
        assertThat(newMatch).isEqualTo(oldMatch)
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

    class CreateMatchRepositoryRequestBuilder() {
        private var matchName = "match name"
        private var teams = listOf<String>("team 01, team 02")

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

