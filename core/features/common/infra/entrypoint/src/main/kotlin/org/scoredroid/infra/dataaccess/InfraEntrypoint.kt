package org.scoredroid.infra.dataaccess

import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.infra.dataaccess.di.DaggerInfraComponent
import org.scoredroid.infra.dataaccess.repository.MatchRepository

interface InfraEntrypoint {
    companion object {
        fun create(persistentMatchDataSource: PersistentMatchDataSource): InfraEntrypoint {
            return DaggerInfraComponent.factory().create(persistentMatchDataSource)
        }
    }

    val matchRepository: MatchRepository
}
