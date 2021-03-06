package org.scoredroid.infra.dataaccess.requestmodel

data class CreateMatchRepositoryRequest(
    val name: String = "",
    val teams: List<TeamRequest> = emptyList(),
)

data class TeamRequest(val name: String)
