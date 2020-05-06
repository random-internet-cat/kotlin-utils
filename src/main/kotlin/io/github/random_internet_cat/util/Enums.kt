package io.github.random_internet_cat.util

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

private class ExhaustiveEnumMapImpl<K : Enum<K>, V> private constructor(private val impl: ImmutableMap<K, V>) : ExhaustiveEnumMap<K, V> {
    companion object {
        fun <K : Enum<K>, V> from(enumClass: KClass<K>, map: Map<K, V>): ExhaustiveEnumMapImpl<K, V> {
            map.keys.requireExhaustive(enumClass)
            return ExhaustiveEnumMapImpl(map.toImmutableMap())
        }

        inline fun <reified K : Enum<K>, V> from(map: Map<K, V>) = from(K::class, map)

        // Just defer equality/hashCode to default map implementation, which is basically guaranteed to be correct.
        private fun <K, V> Map<K, V>.selectEquality() = this.toMap()
    }

    override val entries: Set<Map.Entry<K, V>>
        get() = impl.entries

    override val keys: Set<K>
        get() = impl.keys

    override val size: Int
        get() = impl.size

    override val values: Collection<V>
        get() = impl.values

    override fun get(key: K): V {
        check(impl.containsKey(key))
        return impl[key] as V
    }

    override fun containsKey(key: K): Boolean = impl.containsKey(key)

    override fun containsValue(value: V): Boolean = impl.containsValue(value)

    override fun isEmpty(): Boolean = impl.isEmpty()

    override fun equals(other: Any?): Boolean {
        // Because of the Map contract, we only care if the other is a map, not an ExhaustiveEnumMap.
        if (other !is Map<*, *>) return false

        return this.impl.selectEquality() == other.selectEquality()
    }

    override fun hashCode(): Int {
        return this.impl.selectEquality().hashCode()
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
 * Returns an [ExhaustiveEnumMap] that contains the key-value pairs from the array [pairs]. Throws
 * [IllegalArgumentException] if the keys do not include all of the enumerators of [K] exactly once.
 *
 * @param K the key type of the map, which must be an enum type
 * @param V the value type of the map
 */
inline fun <reified K : Enum<K>, V> Iterable<Pair<K, V>>.toExhaustiveEnumMap(): ExhaustiveEnumMap<K, V> {
    val list = this.toList()
    list.map { it.first }.requireAllAreDistinct()
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
