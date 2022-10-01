package org.scoredroid.infra.dataaccess.datasource.local

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class InMemoryMatchDataSourceTest {
    @Test
    fun `newInstance should create a new instance`() {
        assertThat(InMemoryMatchDataSource.newInstance())
            .isNotSameInstanceAs(InMemoryMatchDataSource.newInstance())
    }

    @Test
    fun `instance should always get the same instance`() {
        assertThat(InMemoryMatchDataSource.instance)
            .isSameInstanceAs(InMemoryMatchDataSource.instance)
    }
}
