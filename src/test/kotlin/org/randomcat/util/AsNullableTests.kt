package org.randomcat.util

import kotlin.test.Test
import kotlin.test.assertSame

class AsNullableTests {
    @Test
    fun `returns same object`() {
        val str = "Hi!"
        assertSame(str, str.asNullable())
    }

    @Test
    fun `returns same null object`() {
        val obj = null as String?
        assertSame(obj, obj.asNullable())
    }
}
