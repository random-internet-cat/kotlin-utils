import io.github.random_internet_cat.util.isExhaustive
import io.github.random_internet_cat.util.requireExhaustive
import test_util.assertSucceeds
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.Test

class `Exhaustive Enum tests` {
    private enum class TestEnum {
        First, Second
    }

    @Test
    fun `isExhaustive correctly returns false`() {
        assertFalse(listOf<TestEnum>().isExhaustive())
        assertFalse(listOf(TestEnum.First).isExhaustive())
        assertFalse(listOf(TestEnum.First, TestEnum.First).isExhaustive())
        assertFalse(listOf(TestEnum.Second).isExhaustive())
    }

    @Test
    fun `isExhaustive correctly returns true`() {
        assertTrue(listOf(TestEnum.First, TestEnum.Second).isExhaustive())
        assertTrue(listOf(TestEnum.Second, TestEnum.First).isExhaustive())
        assertTrue(listOf(TestEnum.Second, TestEnum.First, TestEnum.Second).isExhaustive())
    }

    @Test
    fun `requireExhaustive correctly succeeds`() {
        assertSucceeds {
            listOf(TestEnum.First, TestEnum.Second).requireExhaustive()
        }

        assertSucceeds {
            listOf(TestEnum.Second, TestEnum.First).requireExhaustive()
        }

        assertSucceeds {
            listOf(TestEnum.Second, TestEnum.First, TestEnum.Second).requireExhaustive()
        }
    }

    @Test
    fun `requireExhaustive correctly fails`() {
        assertFailsWith<IllegalArgumentException> { listOf<TestEnum>().requireExhaustive() }
        assertFailsWith<IllegalArgumentException> { listOf(TestEnum.First).requireExhaustive() }
        assertFailsWith<IllegalArgumentException> { listOf(TestEnum.First, TestEnum.First).requireExhaustive() }
        assertFailsWith<IllegalArgumentException> { listOf(TestEnum.Second).requireExhaustive() }
    }
}
