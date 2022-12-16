package org.scoredroid.infra.test.assertions

import org.junit.jupiter.api.Assertions.assertNotNull
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
    println("deep: $persistedMatch")
    assertion(persistedMatch)
    println("deep2: $match")
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
    this.rebootApplication()
    val match = this.repository.getMatch(matchId)
    assertNotNull(match, "the match with id $matchId was not persisted correctly")
    return match!!
}
