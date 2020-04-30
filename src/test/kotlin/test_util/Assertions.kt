package test_util

import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun assertSucceeds(block: () -> Unit) {
    var succeeded: Boolean

    try {
        block()
        succeeded = true
    } catch (caught: Exception) {
        succeeded = false
    }

    assertTrue(succeeded, "Expected block to succeed, but it instead failed with an exception.")
}

/**
 * Given that [first] and [second] are supposed to be equal, assert contract of equals and hashCode.
 *
 * Namely:
 * - Assert that `first == second` is true (testing the assertion).
 * - Assert that `second == first` is true (testing equals contract: symmetric).
 * - Assert that `first.hashCode() == second.hashCode()` (testing equals and hashCode contract consistency).
 */
fun <T> assertEqualsAndHashCode(first: T, second: T) {
    assertEquals(first, second)
    assertEquals(second, first)
    assertEquals(first.hashCode(), second.hashCode())
}
