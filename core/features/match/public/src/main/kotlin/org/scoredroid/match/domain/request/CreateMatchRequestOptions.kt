package org.scoredroid.match.domain.request

data class CreateMatchRequestOptions(
    val matchName: String = "",
    val teams: List<InitialTeamRequest> = emptyList(),
) {
    data class InitialTeamRequest(val name: String)
}
