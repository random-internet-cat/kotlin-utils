import io.github.random_internet_cat.util.asNullable
import kotlin.test.assertSame
import kotlin.test.Test

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
