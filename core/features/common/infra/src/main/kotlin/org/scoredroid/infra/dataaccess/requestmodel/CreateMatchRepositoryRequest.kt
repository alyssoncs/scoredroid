package org.scoredroid.infra.dataaccess.requestmodel

data class CreateMatchRepositoryRequest(
    val teams: List<TeamRequest>
)

class TeamRequest
