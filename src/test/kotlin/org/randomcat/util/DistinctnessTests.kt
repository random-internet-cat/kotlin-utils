package org.randomcat.util

import org.randomcat.util.test_util.assertSucceeds
import kotlin.test.*

class DistinctnessTests {
    @Test
    fun `isDistinct returns true for set with all distinct`() {
        val list = listOf(1, 2, 3, 4)
        assertTrue(list.isDistinct())
    }

    @Test
    fun `isDistinct returns false for set with not all distinct`() {
        val list = listOf(1, 1, 2, 3)
        assertFalse(list.isDistinct())
    }

    @Test
    fun `isDistinctBy returns true for set with all selected keys distinct`() {
        val list = listOf("Alice", "Bob", "Charlie")
        assertTrue(list.isDistinctBy { it[0] })
    }

    @Test
    fun `isDistinctBy returns false for set with not all selected keys distinct`() {
        val list = listOf("Alice", "Adam", "Bob", "Charlie")
        assertFalse(list.isDistinctBy { it[0] })
    }

    @Test
    fun `requireDistinct returns set for list with all distinct`() {
        val list = listOf(1, 2, 3)
        assertEquals(setOf(1, 2, 3), list.requireDistinct())
    }

    @Test
    fun `requireDistinct throws for list with not all distinct`() {
        val list = listOf(1, 1, 2, 3)
        assertFailsWith<IllegalArgumentException> { list.requireDistinct() }
    }

    @Test
    fun `requireDistinctBy does not throw for list with all selected keys distinct`() {
        val list = listOf("Alice", "Bob", "Charlie")

        assertSucceeds {
            list.requireDistinctBy { it[0] }
        }
    }

    @Test
    fun `requireDistinctBy does throws for list with not all selected keys distinct`() {
        val list = listOf("Alice", "Adam", "Bob", "Charlie")
        assertFailsWith<IllegalArgumentException> { list.requireDistinctBy { it[0] } }
    }
}
