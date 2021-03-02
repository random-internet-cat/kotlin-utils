package org.randomcat.util

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlin.reflect.KClass

/**
 * Returns an [Array] containing the enum constants of [E].
 *
 * @param E the enum for which to retrieve the constants
 * @param enumClass the reified [Class] of [E]
 */
fun <E : Enum<E>> enumValuesOf(enumClass: KClass<E>): Array<E> {
    val result = enumClass.java.enumConstants ?: null

    check(result != null)
    check(result.none { it == null })

    return result
}

/**
 * Returns `true` if this collection contains all enum constants of [E], and false otherwise.
 *
 * @param E the enum type
 * @param enumClass the reified [Class] of [E]
 */
fun <E : Enum<E>> Collection<E>.isExhaustive(enumClass: KClass<E>): Boolean {
    val enumConstants = enumValuesOf(enumClass).asList()

    val collection = this
    return collection.containsAll(enumConstants)
}

/**
 * Returns `true` if this collection contains all enum constants of [E], and false otherwise.
 *
 * @param E the enum type
 */
inline fun <reified E : Enum<E>> Collection<E>.isExhaustive(): Boolean = isExhaustive(E::class)

/**
 * Throws [IllegalArgumentException] if this [Collection] does not contain all enum constants of [E].
 *
 * @param E the enum type
 * @param enumClass the reified [Class] of [E]
 */
fun <E : Enum<E>> Collection<E>.requireExhaustive(enumClass: KClass<E>) {
    val enumConstants = enumValuesOf(enumClass)

    for (value in enumConstants) {
        require(this.contains(value)) { "Collection was required to be exhaustive, but did not contain $value" }
    }
}

/**
 * Throws [IllegalArgumentException] if this [Collection] does not contain all enum constants of [E].
 *
 * @param E the enum type
 */
inline fun <reified E : Enum<E>> Collection<E>.requireExhaustive() = requireExhaustive(E::class)

/**
 * A map that, as an invariant, has one entry for each enumerator of [K].
 */
interface ExhaustiveEnumMap<K : Enum<K>, V> : Map<K, V> {
    /**
     * Returns the value corresponding to the given [key]. Does not return `null` as a default value, because this map
     * has a value for each key as an invariant.
     */
    override fun get(key: K): V
}

private class ExhaustiveEnumMapImpl<K : Enum<K>, V>
private constructor(private val impl: ImmutableMap<K, V>) : ExhaustiveEnumMap<K, V>, Map<K, V> by impl {
    companion object {
        fun <K : Enum<K>, V> from(enumClass: KClass<K>, map: Map<K, V>): ExhaustiveEnumMapImpl<K, V> {
            map.keys.requireExhaustive(enumClass)
            return ExhaustiveEnumMapImpl(map.toImmutableMap())
        }

        inline fun <reified K : Enum<K>, V> from(map: Map<K, V>) = from(K::class, map)
    }

    override fun get(key: K): V {
        check(impl.containsKey(key))

        @Suppress("UNCHECKED_CAST")
        return impl[key] as V
    }

    override fun toString(): String {
        return impl.toString()
    }

    override fun equals(other: Any?): Boolean {
        // Because of the Map contract, we only care if the other is a map, not an ExhaustiveEnumMap. This means that we
        // can just trust the implementation. If the use passes us a bad map, they deserve what they get.

        @Suppress("ReplaceCallWithBinaryOperator") // using an explicit equals is more clear about forwarding
        return impl.equals(other)
    }

    override fun hashCode(): Int {
        return impl.hashCode()
    }
}

/**
 * Returns an [ExhaustiveEnumMap] that contains the same keys and values as this map. Throws [IllegalArgumentException]
 * if this map's keys do not include all of the enumerators of [K].
 *
 * @param K the key type of the map, which must be an enum type
 * @param V the value type of the map
 * @param enumClass the reified type of [K]
 */
fun <K : Enum<K>, V> Map<K, V>.toExhaustiveEnumMap(enumClass: KClass<K>): ExhaustiveEnumMap<K, V> {
    return ExhaustiveEnumMapImpl.from(enumClass, this)
}

/**
 * Returns an [ExhaustiveEnumMap] that contains the same keys and values as this map. Throws [IllegalArgumentException]
 * if this map's keys do not include all of the enumerators of [K].
 *
 * @param K the key type of the map, which must be an enum type
 * @param V the value type of the map
 */
inline fun <reified K : Enum<K>, V> Map<K, V>.toExhaustiveEnumMap() = toExhaustiveEnumMap(K::class)

/**
 * Returns an [ExhaustiveEnumMap] that contains the key-value pairs from this iterable. Throws
 * [IllegalArgumentException] if the keys do not include all of the enumerators of [K] exactly once.
 *
 * @param K the key type of the map, which must be an enum type
 * @param V the value type of the map
 */
inline fun <reified K : Enum<K>, V> Iterable<Pair<K, V>>.toExhaustiveEnumMap(): ExhaustiveEnumMap<K, V> {
    val list = this.toList()
    list.requireDistinctBy { it.first }
    return list.toMap().toExhaustiveEnumMap()
}

/**
 * Returns an [ExhaustiveEnumMap] that contains the key-value pairs from the array [pairs]. Throws
 * [IllegalArgumentException] if the keys do not include all of the enumerators of [K] exactly once.
 *
 * @param K the key type of the map, which must be an enum type
 * @param V the value type of the map
 */
inline fun <reified K : Enum<K>, V> exhaustiveEnumMapOf(vararg pairs: Pair<K, V>) = pairs.asList().toExhaustiveEnumMap()
