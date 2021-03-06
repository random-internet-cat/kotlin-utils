package org.randomcat.util

private inline fun <T, K> Iterable<T>.checkDistinctByImpl(
    selector: (T) -> K,
    onRepeat: (item: T, key: K) -> Unit
): Set<K> {
    val alreadySeenKeys = mutableSetOf<K>()

    for (item in this) {
        val key = selector(item)

        if (alreadySeenKeys.contains(key)) {
            onRepeat(item, key)
        } else {
            alreadySeenKeys += key
        }
    }

    return alreadySeenKeys
}

private inline fun <T> Iterable<T>.checkDistinctImpl(onRepeat: (item: T) -> Unit): Set<T> {
    return checkDistinctByImpl(
        selector = { it },
        onRepeat = { item, _ -> onRepeat(item) }
    )
}

/**
 * Returns a [Set] containing all elements that appear more than once in this [Iterable].
 *
 * @param T the element type of this [Iterable]
 */
fun <T> Iterable<T>.repeatingElements(): Set<T> {
    val duplicates = mutableSetOf<T>()
    checkDistinctImpl(onRepeat = { duplicates += it })

    return duplicates
}

/**
 * Returns a [Set] containing the same elements as this [Collection], if this [Collection] contains no repeating
 * elements, otherwise throws [IllegalArgumentException].
 */
@Suppress("NOTHING_TO_INLINE")
@Deprecated("use requireDistinct instead", ReplaceWith("this.requireDistinct()"))
inline fun <T> Iterable<T>.toSetCheckingDistinct(): Set<T> = requireDistinct()

/**
 * Returns `true` if this [Iterable] does not contain any repeating elements, and `false` otherwise.
 *
 * @param T the element type of this [Iterable]
 */
fun <T> Iterable<T>.isDistinct(): Boolean {
    checkDistinctImpl(onRepeat = { return false })

    return true
}

@Suppress("NOTHING_TO_INLINE")
@Deprecated("use isDistinct instead", ReplaceWith("isDistinct()"))
inline fun <T> Iterable<T>.allAreDistinct() = isDistinct()

/**
 * Returns a [Set] containing the same elements as this [Collection], if this [Collection] contains no repeating
 * elements, otherwise throws [IllegalArgumentException].
 */
fun <T> Iterable<T>.requireDistinct(): Set<T> {
    return checkDistinctImpl(onRepeat = {
        throw IllegalArgumentException("Expected all elements to be distinct, but found repeating element: $it")
    })
}

@Suppress("NOTHING_TO_INLINE")
@Deprecated("use requireDistinct instead", ReplaceWith("requireDistinct()"))
inline fun <T> Iterable<T>.requireAllAreDistinct() = requireDistinct()

/**
 * Returns `true` if this [Iterable] has no elements such that results of [selector] are the same, and `false`
 * otherwise.
 *
 * @param T the element type of this [Iterable]
 * @param K the result type of [selector]
 * @param selector the function to map elements to keys
 */
fun <T, K> Iterable<T>.isDistinctBy(selector: (T) -> K): Boolean {
    checkDistinctByImpl(
        selector = selector,
        onRepeat = { _, _ -> return false }
    )

    return true
}

@Suppress("NOTHING_TO_INLINE")
@Deprecated("use isDistinctBy instead", ReplaceWith("isDistinctBy(selector)"))
inline fun <T, K> Iterable<T>.allAreDistinctBy(noinline selector: (T) -> K): Boolean = isDistinctBy(selector)

/**
 * Throws an [IllegalArgumentException] if this [Iterable] has two elements such that results of [selector] are the
 * same.
 *
 * @param T the element type of this [Iterable]
 * @param K the result type of [selector]
 * @param selector the function to map elements to keys
 */
fun <T, K> Iterable<T>.requireDistinctBy(selector: (T) -> K) {
    checkDistinctByImpl(
        selector = selector,
        onRepeat = { item, key ->
            throw IllegalArgumentException(
                "Expected all elements to be distinct, but found repeating key: $key (from element $item)"
            )
        }
    )
}

@Suppress("NOTHING_TO_INLINE")
@Deprecated("use requireDistinctBy instead", ReplaceWith("requireDistinctBy(selector)"))
inline fun <T, K> Iterable<T>.requireAllAreDistinctBy(noinline selector: (T) -> K) = requireDistinctBy(selector)

/**
 * Returns `true` if all elements in this [Iterable] are equal, and `false` otherwise.
 *
 * In particular, returns `true` for an empty collection.
 *
 * @param T the element type of this iterable
 */
fun <T> Iterable<T>.allAreEqual(): Boolean {
    val iter = iterator()
    if (!iter.hasNext()) return true

    val value = iter.next()

    while (iter.hasNext()) {
        if (value != iter.next()) return false
    }

    return true
}

/**
 * Throws [IllegalArgumentException] if all elements in this [Iterable] are not equal.
 *
 * In particular, does not throw for an empty collection.
 *
 * @param T the element type of this iterable
 */
fun <T> Iterable<T>.requireAllAreEqual() {
    require(allAreEqual()) {
        "Expected all elements in collection $this to be equal"
    }
}

/**
 * Equivalent to `this.getValue(key)`
 */
@Deprecated("Use getValue instead", ReplaceWith("this.getValue(key)", "kotlin.collections.getValue"))
fun <K, V : Any> Map<K, V>.getOrFail(key: K): V = getValue(key)
