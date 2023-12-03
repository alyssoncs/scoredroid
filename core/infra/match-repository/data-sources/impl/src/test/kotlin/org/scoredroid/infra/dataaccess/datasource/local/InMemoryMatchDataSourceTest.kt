package org.scoredroid.infra.dataaccess.datasource.local

import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.Test

class InMemoryMatchDataSourceTest {
    @Test
    fun `newInstance should create a new instance`() {
        InMemoryMatchDataSource.newInstance() shouldNotBeSameInstanceAs InMemoryMatchDataSource.newInstance()
    }

    @Test
    fun `instance should always get the same instance`() {
        InMemoryMatchDataSource.instance shouldBeSameInstanceAs InMemoryMatchDataSource.instance
    }
}
