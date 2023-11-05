package org.scoredroid.usecase.doubles

import org.scoredroid.data.response.MatchResponse
import org.scoredroid.usecase.CreateMatchRequestOptions
import org.scoredroid.usecase.CreateMatchUseCase

class CreateMatchSpy : CreateMatchUseCase {
    private val stub = CreateMatchStub()

    var request: CreateMatchRequestOptions? = null
        private set

    var response: MatchResponse
        get() = stub.response
        set(value) {
            stub.response = value
        }

    override suspend fun invoke(createMatchOptions: CreateMatchRequestOptions): MatchResponse {
        request = createMatchOptions
        return stub.invoke(createMatchOptions)
    }
}
