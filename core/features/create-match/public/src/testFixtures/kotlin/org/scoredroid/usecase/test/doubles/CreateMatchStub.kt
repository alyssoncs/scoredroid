package org.scoredroid.usecase.test.doubles

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.usecase.CreateMatchRequestOptions
import org.scoredroid.usecase.CreateMatchUseCase

class CreateMatchStub : CreateMatchUseCase {
    var response: MatchResponse = MatchResponse(
        id = 0,
        name = "",
        teams = listOf(
            TeamResponse(name = "first team", score = 0),
            TeamResponse(name = "second team", score = 0),
        ),
    )

    override suspend fun invoke(createMatchOptions: CreateMatchRequestOptions): MatchResponse {
        return response
    }
}