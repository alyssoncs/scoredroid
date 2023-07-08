package org.scoredroid.infra.dataaccess.di

import dagger.BindsInstance
import dagger.Component
import org.scoredroid.infra.dataaccess.InfraEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.infra.dataaccess.di.modules.DataSourceModule
import org.scoredroid.infra.dataaccess.di.modules.RepositoryModule

@Component(
    modules = [
        DataSourceModule::class,
        RepositoryModule::class,
    ],
)
internal interface InfraComponent : InfraEntrypoint {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance persistentMatchDataSource: PersistentMatchDataSource,
        ): InfraComponent
    }
}
