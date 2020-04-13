import io.github.random_internet_cat.util.allAreEqual
import io.github.random_internet_cat.util.requireAllAreEqual
import test_util.assertSucceeds
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.Test

class AllAreEqualTests {
    @Test
    fun `allAreEqual returns true for empty collection`() {
        assertTrue(emptyList<String>().allAreEqual())
    }

    @Test
    fun `allAreEqual returns true when all elements are equal`() {
        assertTrue(listOf(1).allAreEqual())
        assertTrue(listOf(2, 2, 2, 2).allAreEqual())
    }

    @Test
    fun `allAreEqual returns false when not all elements are equal`() {
        assertFalse(listOf(1, 1, 3).allAreEqual())
        assertFalse(listOf(1, 2, 3).allAreEqual())
    }

    @Test
    fun `requireAllAreEqual does not throw for empty collection`() {
        assertSucceeds {
            emptyList<String>().requireAllAreEqual()
        }
    }

    @Test
    fun `requireAllAreEqual does not throw when all elements are equal`() {
        assertSucceeds {
            listOf(1).requireAllAreEqual()
            listOf(2, 2, 2).requireAllAreEqual()
        }
    }

    @Test
    fun `requireAllAreEqual throws when all elements are not equal`() {
        assertFailsWith<IllegalArgumentException> {
            listOf(1, 1, 3).requireAllAreEqual()
        }

        assertFailsWith<IllegalArgumentException> {
            listOf(1, 2, 3).requireAllAreEqual()
        }
    }
}
