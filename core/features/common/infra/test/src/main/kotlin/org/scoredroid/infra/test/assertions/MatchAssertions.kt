package org.scoredroid.infra.test.assertions

import com.google.common.truth.Truth.assertThat
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixture
import org.scoredroid.utils.mappers.toMatchResponse

suspend fun assertMatchResponse(
    fixture: MatchRepositoryFixture,
    match: MatchResponse,
    assertion: (MatchResponse) -> Unit
) {
    val persistedMatch = fixture.getPersistedMatch(match.id).toMatchResponse()
    println(persistedMatch)
    assertion(persistedMatch)
    assertion(match)
}

suspend fun assertMatchResponse(
    fixture: MatchRepositoryFixture,
    matchResult: Result<MatchResponse>,
    assertion: (MatchResponse) -> Unit
) {
    assertMatchResponse(fixture, matchResult.getOrThrow(), assertion)
}


private suspend fun MatchRepositoryFixture.getPersistedMatch(matchId: Long): Match {
    val match = this.repository.getMatch(matchId)
    assertThat(match).isNotNull()
    return match!!
}
