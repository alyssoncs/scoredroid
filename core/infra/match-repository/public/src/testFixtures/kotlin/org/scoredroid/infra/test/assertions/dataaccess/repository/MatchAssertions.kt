package org.scoredroid.infra.test.assertions.dataaccess.repository

import org.junit.jupiter.api.Assertions.assertTrue
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixture
import org.scoredroid.utils.mappers.toMatchResponse

suspend fun assertMatchResponse(
    fixture: MatchRepositoryFixture,
    match: MatchResponse,
    assertion: (MatchResponse) -> Unit,
) {
    val persistedMatch = fixture.getPersistedMatch(match.id).toMatchResponse()
    assertion(persistedMatch)
    assertion(match)
}

suspend fun assertMatchResponse(
    fixture: MatchRepositoryFixture,
    matchResult: Result<MatchResponse>,
    assertion: (MatchResponse) -> Unit,
) {
    assertMatchResponse(fixture, matchResult.getOrThrow(), assertion)
}

private suspend fun MatchRepositoryFixture.getPersistedMatch(matchId: Long): Match {
    this.rebootApplication()
    val result = this.repository.getMatch(matchId)
    assertTrue(result.isSuccess, "the match with id $matchId was not persisted correctly")
    return result.getOrThrow()
}
