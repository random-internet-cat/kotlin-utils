import io.github.random_internet_cat.util.getOrFail
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetOrFailTests {
    @Test
    fun `returns correct value`() {
        val map = mapOf(1 to "hi", 2 to "bye")
        assertEquals("hi", map.getOrFail(1))
    }

    @Test
    fun `throws on missing value`() {
        val map = mapOf(1 to "hi", 2 to "bye")
        assertFailsWith<IllegalStateException> { map.getOrFail(3) }
    }
}
