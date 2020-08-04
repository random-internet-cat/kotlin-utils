package io.github.random_internet_cat.util

import io.github.random_internet_cat.util.test_util.assertSucceeds
import kotlin.test.*

class DistinctnessTests {
    @Test
    fun `allAreDistinct returns true for set with all distinct`() {
        val list = listOf(1, 2, 3, 4)
        assertTrue(list.allAreDistinct())
    }

    @Test
    fun `allAreDistinct returns false for set with not all distinct`() {
        val list = listOf(1, 1, 2, 3)
        assertFalse(list.allAreDistinct())
    }

    @Test
    fun `allAreDistinctBy returns true for set with all selected keys distinct`() {
        val list = listOf("Alice", "Bob", "Charlie")
        assertTrue(list.allAreDistinctBy { it[0] })
    }

    @Test
    fun `allAreDistinctBy returns false for set with not all selected keys distinct`() {
        val list = listOf("Alice", "Adam", "Bob", "Charlie")
        assertFalse(list.allAreDistinctBy { it[0] })
    }

    @Test
    fun `requireAllAreDistinct does not throw for list with all distinct`() {
        val list = listOf(1, 2, 3)

        assertSucceeds {
            list.requireAllAreDistinct()
        }
    }

    @Test
    fun `requireAllAreDistinct throws for list with not all distinct`() {
        val list = listOf(1, 1, 2, 3)
        assertFailsWith<IllegalArgumentException> { list.requireAllAreDistinct() }
    }

    @Test
    fun `toSetCheckingDistinct returns set for list with all distinct`() {
        val list = listOf(1, 2, 3)
        assertEquals(setOf(1, 2, 3), list.toSetCheckingDistinct())
    }

    @Test
    fun `toSetCheckingDistinct throws for list with not all distinct`() {
        val list = listOf(1, 1, 2, 3)
        assertFailsWith<IllegalArgumentException> { list.toSetCheckingDistinct() }
    }

    @Test
    fun `requireAllAreDistinctBy does not throw for list with all selected keys distinct`() {
        val list = listOf("Alice", "Bob", "Charlie")

        assertSucceeds {
            list.requireAllAreDistinctBy { it[0] }
        }
    }

    @Test
    fun `requireAllAreDistinctBy does throws for list with not all selected keys distinct`() {
        val list = listOf("Alice", "Adam", "Bob", "Charlie")
        assertFailsWith<IllegalArgumentException> { list.requireAllAreDistinctBy { it[0] } }
    }
}
