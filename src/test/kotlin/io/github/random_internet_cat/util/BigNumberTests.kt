package io.github.random_internet_cat.util

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BigDecimalMathTests {
    @Test
    fun `addition works`() {
        val bd = BigDecimal.valueOf(5)
        val i = 2

        assertEquals(BigDecimal.valueOf(7), bd + i)
        assertEquals(BigDecimal.valueOf(7), i + bd)
    }

    @Test
    fun `subtraction works`() {
        val bd = BigDecimal.valueOf(5)
        val i = 2

        assertEquals(BigDecimal.valueOf(3), bd - i)
        assertEquals(BigDecimal.valueOf(-3), i - bd)
    }

    @Test
    fun `multiplication works`() {
        val bd = BigDecimal.valueOf(5)
        val i = 2

        assertEquals(BigDecimal.TEN, bd * i)
        assertEquals(BigDecimal.TEN, i * bd)
    }

    @Test
    fun `comparison works`() {
        val i = 1
        val bd = BigDecimal.valueOf(5)

        assertTrue(i < bd)
        assertTrue(i <= bd)
        assertFalse(i > bd)
        assertFalse(i >= bd)

        assertFalse(bd < i)
        assertFalse(bd <= i)
        assertTrue(bd > i)
        assertTrue(bd >= i)
    }
}

class BigDecimalCeilTests {
    @Test
    fun `ceil(BigDecimal) works`() {
        class CeilTestCase(val input: String, val result: String)

        val testCases = listOf(
            CeilTestCase(input = "0", result = "0"),
            CeilTestCase(input = "0.5", result = "1"),
            CeilTestCase(input = "1.2", result = "2"),
            CeilTestCase(input = "1.7", result = "2"),
            CeilTestCase(input = "2.00000", result = "2"),
            CeilTestCase(input = "-2", result = "-2"),
            CeilTestCase(input = "-2.1", result = "-2"),
            CeilTestCase(input = "-2.5", result = "-2"),
            CeilTestCase(input = "-2.9", result = "-2"),
            CeilTestCase(input = "-3", result = "-3"),
            CeilTestCase(input = "-3.1", result = "-3")
        )

        for (testCase in testCases) {
            assertEquals(BigInteger(testCase.result), ceil(BigDecimal(testCase.input)))
        }
    }
}
