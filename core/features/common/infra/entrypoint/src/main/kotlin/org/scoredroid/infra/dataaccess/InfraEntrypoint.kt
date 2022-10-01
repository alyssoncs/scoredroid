package org.scoredroid.infra.dataaccess

import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.infra.dataaccess.di.DaggerInfraComponent
import org.scoredroid.infra.dataaccess.repository.MatchRepository

class InfraEntrypoint private constructor(val matchRepository: MatchRepository) {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): InfraEntrypoint {
            val component = DaggerInfraComponent.factory().create(persistentMatchDataSource)
            return InfraEntrypoint(component.matchRepository())
        }
    }
}