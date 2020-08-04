import io.github.random_internet_cat.util.repeatingElements
import kotlin.test.Test
import kotlin.test.assertEquals

class RepeatingElementsTests {
    @Test
    fun `returns empty set for list without repeating elements`() {
        assertEquals(emptySet(), listOf(1, 2, 3).repeatingElements())
    }

    @Test
    fun `returns repeating elements`() {
        assertEquals(setOf(1), listOf(1, 1, 2, 3).repeatingElements())
        assertEquals(setOf(1, 2), listOf(1, 1, 2, 2, 3).repeatingElements())
        assertEquals(setOf(1, 3), listOf(1, 1, 2, 3, 3).repeatingElements())
    }
}
