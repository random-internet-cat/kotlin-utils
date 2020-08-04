import io.github.random_internet_cat.util.exhaustiveEnumMapOf
import io.github.random_internet_cat.util.isExhaustive
import io.github.random_internet_cat.util.requireExhaustive
import io.github.random_internet_cat.util.toExhaustiveEnumMap
import org.junit.jupiter.api.assertThrows
import test_util.assertEqualsAndHashCode
import test_util.assertSucceeds
import kotlin.test.*

class ExhaustiveEnumTests {
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

class ExhahustiveEnumMapTests {
    enum class TestEnum { FIRST, SECOND }

    @Test
    fun `exhaustiveEnumMapOf and toExhaustiveEnumMap provide maps that work`() {
        val testMaps = listOf(
            exhaustiveEnumMapOf(TestEnum.FIRST to 1, TestEnum.SECOND to 2),
            mapOf(TestEnum.FIRST to 1, TestEnum.SECOND to 2).toExhaustiveEnumMap(),
            listOf(TestEnum.FIRST to 1, TestEnum.SECOND to 2).toExhaustiveEnumMap()
        )

        for (testMap in testMaps) {
            assertEquals(TestEnum.values().size, testMap.size)
            assertFalse(testMap.isEmpty())
            assertEquals(setOf(TestEnum.FIRST, TestEnum.SECOND), testMap.keys)
            assertEquals(setOf(1, 2), testMap.values.toSet())
            assertEquals(1, testMap[TestEnum.FIRST])
            assertEquals(2, testMap[TestEnum.SECOND])

            for (key in TestEnum.values()) {
                assertTrue(testMap.containsKey(key))
            }

            for (value in listOf(1, 2)) {
                assertTrue(testMap.containsValue(value))
            }
        }

        for (first in testMaps) {
            for (second in testMaps) {
                assertEqualsAndHashCode(first, second)
            }
        }
    }

    @Test
    fun `ExhaustiveEnumMap compares equal to self`() {
        val map = exhaustiveEnumMapOf(TestEnum.FIRST to 1, TestEnum.SECOND to 1)
        assertEquals(map, map)
    }

    @Test
    fun `ExhaustiveEnumMap compares unequal to different map`() {
        val first = exhaustiveEnumMapOf(TestEnum.FIRST to 1, TestEnum.SECOND to 1)
        val second = exhaustiveEnumMapOf(TestEnum.FIRST to 1, TestEnum.SECOND to 2)

        assertNotEquals(first, second)
    }

    @Test
    fun `ExhaustiveEnumMap compares equal to Map with same values`() {
        val exhaustiveMap = exhaustiveEnumMapOf(TestEnum.FIRST to 1, TestEnum.SECOND to 1)
        val normalMap = mapOf(TestEnum.FIRST to 1, TestEnum.SECOND to 1)

        assertEqualsAndHashCode(normalMap, exhaustiveMap)
    }

    @Test
    fun `exhaustiveEnumMapOf throws IllegalArgumentException if keys are not exhaustive`() {
        assertThrows<IllegalArgumentException> {
            exhaustiveEnumMapOf<TestEnum, Int>()
        }

        assertThrows<IllegalArgumentException> {
            exhaustiveEnumMapOf(TestEnum.FIRST to 1)
        }

        assertThrows<IllegalArgumentException> {
            exhaustiveEnumMapOf(TestEnum.SECOND to 2)
        }
    }

    @Test
    fun `exhaustiveEnumMapOf throws IllegalArgumentException if keys are duplicated`() {
        assertThrows<IllegalArgumentException> {
            exhaustiveEnumMapOf(TestEnum.FIRST to 1, TestEnum.FIRST to 1)
        }

        assertThrows<IllegalArgumentException> {
            exhaustiveEnumMapOf(TestEnum.FIRST to 1, TestEnum.SECOND to 1, TestEnum.FIRST to 1)
        }
    }

    @Test
    fun `toExhaustiveEnumMap throws IllegalArgumentException if keys are duplicated`() {
        assertThrows<IllegalArgumentException> {
            listOf(TestEnum.FIRST to 1, TestEnum.FIRST to 1).toExhaustiveEnumMap()
        }

        assertThrows<IllegalArgumentException> {
            listOf(TestEnum.FIRST to 1, TestEnum.SECOND to 1, TestEnum.FIRST to 1).toExhaustiveEnumMap()
        }
    }

    @Test
    fun `toExhaustiveEnumMap throws IllegalArgumentException if keys are not exhaustive`() {
        assertThrows<IllegalArgumentException> {
            listOf<Pair<TestEnum, Int>>().toExhaustiveEnumMap()
        }

        assertThrows<IllegalArgumentException> {
            mapOf<TestEnum, Int>().toExhaustiveEnumMap()
        }

        assertThrows<IllegalArgumentException> {
            mapOf(TestEnum.FIRST to 1).toExhaustiveEnumMap()
        }

        assertThrows<IllegalArgumentException> {
            listOf(TestEnum.FIRST to 1).toExhaustiveEnumMap()
        }

        assertThrows<IllegalArgumentException> {
            mapOf(TestEnum.SECOND to 2).toExhaustiveEnumMap()
        }

        assertThrows<IllegalArgumentException> {
            listOf(TestEnum.SECOND to 2).toExhaustiveEnumMap()
        }
    }

    enum class EmptyEnum

    @Test
    fun `ExhaustiveEnumMap works with empty enums`() {
        val testMaps = listOf(
            exhaustiveEnumMapOf<EmptyEnum, Int>(),
            mapOf<EmptyEnum, Int>().toExhaustiveEnumMap(),
            listOf<Pair<EmptyEnum, Int>>().toExhaustiveEnumMap()
        )

        for (testMap in testMaps) {
            assertEquals(setOf(), testMap.entries)
            assertEquals(setOf(), testMap.keys)
            assertEquals(setOf(), testMap.values.toSet())
            assertFalse(testMap.containsValue(0))
            assertTrue(testMap.isEmpty())
            assertEqualsAndHashCode(testMap, mapOf<TestEnum, Int>())
        }
    }
}
