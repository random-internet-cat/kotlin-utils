package org.randomcat.util

class PrimaryKeyMismatchException(
    val key: Any?,
    val original: Any?,
    val added: Any?,
) : Exception("Attempt to add conflicting element with key $key; original = $original; added = $added")

inline fun <T : Any, K : Any> Iterable<T>.isPrimaryKeyedBy(keySelector: (T) -> K): Boolean {
    val knownMappings = mutableMapOf<K, T>()

    for (element in this) {
        val key = keySelector(element)
        val old = knownMappings.put(key, element)

        if (old != null && old != element) return false
    }

    return true
}

inline fun <T : Any, K : Any> Iterable<T>.groupByPrimaryKey(keySelector: (T) -> K): Map<K, T> {
    val knownMappings = mutableMapOf<K, T>()

    for (element in this) {
        val key = keySelector(element)
        val old = knownMappings.put(key, element)

        if (old != null && old != element) {
            throw PrimaryKeyMismatchException(
                key = key,
                original = old,
                added = element,
            )
        }
    }

    return knownMappings
}
