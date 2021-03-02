package io.github.random_internet_cat.util

import io.github.random_internet_cat.util.test_util.assertSucceeds
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PrimaryKeyedTests {
    private data class KeyValuePair(val key: Int, val value: String)

    @Test
    fun `isPrimaryKeyedBy and groupByPrimaryKey exception works`() {
        assertTrue(emptyList<KeyValuePair>().isPrimaryKeyedBy { it.key })

        assertPrimaryKeyed(
            KeyValuePair(1, "A"),
        )

        assertPrimaryKeyed(
            KeyValuePair(1, "A"),
            KeyValuePair(1, "A"),
        )

        assertPrimaryKeyed(
            KeyValuePair(1, "A"),
            KeyValuePair(2, "B"),
        )

        assertPrimaryKeyed(
            KeyValuePair(1, "A"),
            KeyValuePair(1, "A"),
            KeyValuePair(2, "B"),
        )

        assertNotPrimaryKeyed(
            KeyValuePair(1, "A"),
            KeyValuePair(1, "B"),
        )

        assertNotPrimaryKeyed(
            KeyValuePair(1, "A"),
            KeyValuePair(1, "B"),
            KeyValuePair(2, "A"),
            KeyValuePair(2, "A"),
        )

        assertNotPrimaryKeyed(
            KeyValuePair(1, "A"),
            KeyValuePair(1, "B"),
            KeyValuePair(2, "A"),
            KeyValuePair(2, "B"),
        )
    }

    private fun assertPrimaryKeyed(entries: List<KeyValuePair>) {
        assertTrue(entries.isPrimaryKeyedBy { it.key })
        assertSucceeds { entries.groupByPrimaryKey { it.key } }
    }

    private fun assertPrimaryKeyed(vararg entries: KeyValuePair) = assertPrimaryKeyed(entries.asList())

    private fun assertNotPrimaryKeyed(entries: List<KeyValuePair>) {
        assertFalse(entries.isPrimaryKeyedBy { it.key })
        assertFails { entries.groupByPrimaryKey { it.key } }
    }

    private fun assertNotPrimaryKeyed(vararg entries: KeyValuePair) = assertNotPrimaryKeyed(entries.asList())
}
