package com.alibaba.fastjson.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @deprecated
 */
public class AntiCollisionHashMap<K, V> extends AbstractMap<K, V> implements
        Map<K, V>, Cloneable, Serializable {

    transient volatile Set<K> keySet = null;
    transient volatile Collection<V> values = null;

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * The maximum capacity, used if a higher value is implicitly specified by
     * either of the constructors with arguments. MUST be a power of two <=
     * 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * The table, resized as necessary. Length MUST Always be a power of two.
     */
    transient Entry<K, V>[] table;

    /**
     * The number of key-value mappings contained in this map.
     */
    transient int size;

    /**
     * The next size value at which to resize (capacity * load factor).
     *
     * @serial
     */
    int threshold;

    /**
     * The load factor for the hash table.
     *
     * @serial
     */
    final float loadFactor;

    /**
     * The number of times this SafelyHashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the SafelyHashMap or otherwise modify its internal structure (e.g.,
     * rehash). This field is used to make iterators on Collection-views of the
     * SafelyHashMap fail-fast. (See ConcurrentModificationException).
     */
    transient volatile int modCount;

    /**
     * Constructs an empty <tt>SafelyHashMap</tt> with the specified initial
     * capacity and load factor.
     *
     * @param initialCapacity
     *            the initial capacity
     * @param loadFactor
     *            the load factor
     * @throws IllegalArgumentException
     *             if the initial capacity is negative or the load factor is
     *             nonpositive
     */

    final static int M_MASK = 0x8765fed3;
    final static int SEED = -2128831035;
    final static int KEY = 16777619;

    final int random = new Random().nextInt(99999); // a fixed value in an instance
    private int hashString(String key) {

        int hash = SEED * random;
        for (int i = 0; i < key.length(); i++)
            hash = (hash * KEY) ^ key.charAt(i);
        return (hash ^ (hash >> 1)) & M_MASK;
    }

    public AntiCollisionHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: "
                    + initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: "
                    + loadFactor);

        // Find a power of 2 >= initialCapacity
        int capacity = 1;
        while (capacity < initialCapacity)
            capacity <<= 1;

        this.loadFactor = loadFactor;
        threshold = (int) (capacity * loadFactor);
        table = new Entry[capacity];
        init();
    }

    /**
     * Constructs an empty <tt>SafelyHashMap</tt> with the specified initial
     * capacity and the default load factor (0.75).
     *
     * @param initialCapacity
     *            the initial capacity.
     * @throws IllegalArgumentException
     *             if the initial capacity is negative.
     */
    public AntiCollisionHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs an empty <tt>SafelyHashMap</tt> with the default initial
     * capacity (16) and the default load factor (0.75).
     */
    public AntiCollisionHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
        init();
    }

    /**
     * Constructs a new <tt>SafelyHashMap</tt> with the same mappings as the
     * specified <tt>Map</tt>. The <tt>SafelyHashMap</tt> is created with
     * default load factor (0.75) and an initial capacity sufficient to hold the
     * mappings in the specified <tt>Map</tt>.
     *
     * @param m
     *            the map whose mappings are to be placed in this map
     * @throws NullPointerException
     *             if the specified map is null
     */
    public AntiCollisionHashMap(Map<? extends K, ? extends V> m) {
        this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,
                DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
        putAllForCreate(m);
    }

    // internal utilities

    /**
     * Initialization hook for subclasses. This method is called in all
     * constructors and pseudo-constructors (clone, readObject) after
     * SafelyHashMap has been initialized but before any entries have been
     * inserted. (In the absence of this method, readObject would require
     * explicit knowledge of subclasses.)
     */
    void init() {
    }

    /**
     * Applies a supplemental hash function to a given hashCode, which defends
     * against poor quality hash functions. This is critical because
     * SafelyHashMap uses power-of-two length hash tables, that otherwise
     * encounter collisions for hashCodes that do not differ in lower bits.
     * Note: Null keys always map to hash 0, thus index 0.
     */
    static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h = h * h;
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * Returns index for hash code h.
     */
    static int indexFor(int h, int length) {
        return h & (length - 1);
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return size;
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null}
     * if this map contains no mapping for the key.
     *
     * <p>
     * More formally, if this map contains a mapping from a key {@code k} to a
     * value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise it returns
     * {@code null}. (There can be at most one such mapping.)
     *
     * <p>
     * A return value of {@code null} does not <i>necessarily</i> indicate that
     * the map contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}. The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @see #put(Object, Object)
     */
    public V get(Object key) {
        if (key == null)
            return getForNullKey();
        int hash = 0;
        if (key instanceof String)
            hash = hash(hashString((String) key));
        else
            hash = hash(key.hashCode());
        for (Entry<K, V> e = table[indexFor(hash, table.length)]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
                return e.value;
        }
        return null;
    }

    /**
     * Offloaded version of get() to look up null keys. Null keys map to index
     * 0. This null case is split out into separate methods for the sake of
     * performance in the two most commonly used operations (get and put), but
     * incorporated with conditionals in others.
     */
    private V getForNullKey() {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null)
                return e.value;
        }
        return null;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.
     *
     * @param key
     *            The key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     *         key.
     */
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    /**
     * Returns the entry associated with the specified key in the SafelyHashMap.
     * Returns null if the SafelyHashMap contains no mapping for the key.
     */
    final Entry<K, V> getEntry(Object key) {
        int hash = (key == null) ? 0
                : (key instanceof String) ? hash(hashString((String) key))
                : hash(key.hashCode());
        for (Entry<K, V> e = table[indexFor(hash, table.length)]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash
                    && ((k = e.key) == key || (key != null && key.equals(k))))
                return e;
        }
        return null;
    }

    /**
     * Associates the specified value with the specified key in this map. If the
     * map previously contained a mapping for the key, the old value is
     * replaced.
     *
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
     *         if there was no mapping for <tt>key</tt>. (A <tt>null</tt> return
     *         can also indicate that the map previously associated
     *         <tt>null</tt> with <tt>key</tt>.)
     */
    public V put(K key, V value) {
        if (key == null)
            return putForNullKey(value);
        int hash = 0;
        if (key instanceof String)
            hash = hash(hashString((String) key));
        else
            hash = hash(key.hashCode());
        int i = indexFor(hash, table.length);
        for (Entry<K, V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, key, value, i);
        return null;
    }

    /**
     * Offloaded version of put for null keys
     */
    private V putForNullKey(V value) {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }
        modCount++;
        addEntry(0, null, value, 0);
        return null;
    }

    /**
     * This method is used instead of put by constructors and pseudoconstructors
     * (clone, readObject). It does not resize the table, check for
     * comodification, etc. It calls createEntry rather than addEntry.
     */
    private void putForCreate(K key, V value) {
        int hash = (key == null) ? 0
                : (key instanceof String) ? hash(hashString((String) key))
                : hash(key.hashCode());
        int i = indexFor(hash, table.length);

        /**
         * Look for preexisting entry for key. This will never happen for clone
         * or deserialize. It will only happen for construction if the input Map
         * is a sorted map whose ordering is inconsistent w/ equals.
         */
        for (Entry<K, V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash
                    && ((k = e.key) == key || (key != null && key.equals(k)))) {
                e.value = value;
                return;
            }
        }

        createEntry(hash, key, value, i);
    }

    private void putAllForCreate(Map<? extends K, ? extends V> m) {
        for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m
                .entrySet().iterator(); i.hasNext();) {
            Map.Entry<? extends K, ? extends V> e = i.next();
            putForCreate(e.getKey(), e.getValue());
        }
    }

    /**
     * Rehashes the contents of this map into a new array with a larger
     * capacity. This method is called automatically when the number of keys in
     * this map reaches its threshold.
     *
     * If current capacity is MAXIMUM_CAPACITY, this method does not resize the
     * map, but sets threshold to Integer.MAX_VALUE. This has the effect of
     * preventing future calls.
     *
     * @param newCapacity
     *            the new capacity, MUST be a power of two; must be greater than
     *            current capacity unless current capacity is MAXIMUM_CAPACITY
     *            (in which case value is irrelevant).
     */
    void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    /**
     * Transfers all entries from current table to newTable.
     */
    void transfer(Entry[] newTable) {
        Entry[] src = table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry<K, V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry<K, V> next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

    /**
     * Copies all of the mappings from the specified map to this map. These
     * mappings will replace any mappings that this map had for any of the keys
     * currently in the specified map.
     *
     * @param m
     *            mappings to be stored in this map
     * @throws NullPointerException
     *             if the specified map is null
     */
    public void putAll(Map<? extends K, ? extends V> m) {
        int numKeysToBeAdded = m.size();
        if (numKeysToBeAdded == 0)
            return;

		/*
		 * Expand the map if the map if the number of mappings to be added is
		 * greater than or equal to threshold. This is conservative; the obvious
		 * condition is (m.size() + size) >= threshold, but this condition could
		 * result in a map with twice the appropriate capacity, if the keys to
		 * be added overlap with the keys already in this map. By using the
		 * conservative calculation, we subject ourself to at most one extra
		 * resize.
		 */
        if (numKeysToBeAdded > threshold) {
            int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);
            if (targetCapacity > MAXIMUM_CAPACITY)
                targetCapacity = MAXIMUM_CAPACITY;
            int newCapacity = table.length;
            while (newCapacity < targetCapacity)
                newCapacity <<= 1;
            if (newCapacity > table.length)
                resize(newCapacity);
        }

        for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m
                .entrySet().iterator(); i.hasNext();) {
            Map.Entry<? extends K, ? extends V> e = i.next();
            put(e.getKey(), e.getValue());
        }
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @param key
     *            key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
     *         if there was no mapping for <tt>key</tt>. (A <tt>null</tt> return
     *         can also indicate that the map previously associated
     *         <tt>null</tt> with <tt>key</tt>.)
     */
    public V remove(Object key) {
        Entry<K, V> e = removeEntryForKey(key);
        return (e == null ? null : e.value);
    }

    /**
     * Removes and returns the entry associated with the specified key in the
     * SafelyHashMap. Returns null if the SafelyHashMap contains no mapping for
     * this key.
     */
    final Entry<K, V> removeEntryForKey(Object key) {
        int hash = (key == null) ? 0
                : (key instanceof String) ? hash(hashString((String) key))
                : hash(key.hashCode());
        int i = indexFor(hash, table.length);
        Entry<K, V> prev = table[i];
        Entry<K, V> e = prev;

        while (e != null) {
            Entry<K, V> next = e.next;
            Object k;
            if (e.hash == hash
                    && ((k = e.key) == key || (key != null && key.equals(k)))) {
                modCount++;
                size--;
                if (prev == e)
                    table[i] = next;
                else
                    prev.next = next;
                e.recordRemoval(this);
                return e;
            }
            prev = e;
            e = next;
        }

        return e;
    }

    /**
     * Special version of remove for EntrySet.
     */
    final Entry<K, V> removeMapping(Object o) {
        if (!(o instanceof Map.Entry))
            return null;

        Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
        Object key = entry.getKey();
        int hash = (key == null) ? 0
                : (key instanceof String) ? hash(hashString((String) key))
                : hash(key.hashCode());
        int i = indexFor(hash, table.length);
        Entry<K, V> prev = table[i];
        Entry<K, V> e = prev;

        while (e != null) {
            Entry<K, V> next = e.next;
            if (e.hash == hash && e.equals(entry)) {
                modCount++;
                size--;
                if (prev == e)
                    table[i] = next;
                else
                    prev.next = next;
                e.recordRemoval(this);
                return e;
            }
            prev = e;
            e = next;
        }

        return e;
    }

    /**
     * Removes all of the mappings from this map. The map will be empty after
     * this call returns.
     */
    public void clear() {
        modCount++;
        Entry[] tab = table;
        for (int i = 0; i < tab.length; i++)
            tab[i] = null;
        size = 0;
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the specified
     * value.
     *
     * @param value
     *            value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the specified
     *         value
     */
    public boolean containsValue(Object value) {
        if (value == null)
            return containsNullValue();

        Entry[] tab = table;
        for (int i = 0; i < tab.length; i++)
            for (Entry e = tab[i]; e != null; e = e.next)
                if (value.equals(e.value))
                    return true;
        return false;
    }

    /**
     * Special-case code for containsValue with null argument
     */
    private boolean containsNullValue() {
        Entry[] tab = table;
        for (int i = 0; i < tab.length; i++)
            for (Entry e = tab[i]; e != null; e = e.next)
                if (e.value == null)
                    return true;
        return false;
    }

    /**
     * Returns a shallow copy of this <tt>SafelyHashMap</tt> instance: the keys
     * and values themselves are not cloned.
     *
     * @return a shallow copy of this map
     */
    public Object clone() {
        AntiCollisionHashMap<K, V> result = null;
        try {
            result = (AntiCollisionHashMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            // assert false;
        }
        result.table = new Entry[table.length];
        result.entrySet = null;
        result.modCount = 0;
        result.size = 0;
        result.init();
        result.putAllForCreate(this);

        return result;
    }

    static class Entry<K, V> implements Map.Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;
        final int hash;

        /**
         * Creates new entry.
         */
        Entry(int h, K k, V v, Entry<K, V> n) {
            value = v;
            next = n;
            key = k;
            hash = h;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry e = (Map.Entry) o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2)))
                    return true;
            }
            return false;
        }

        public final int hashCode() {
            return (key == null ? 0 : key.hashCode())
                    ^ (value == null ? 0 : value.hashCode());
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }

        /**
         * This method is invoked whenever the value in an entry is overwritten
         * by an invocation of put(k,v) for a key k that's already in the
         * SafelyHashMap.
         */
        void recordAccess(AntiCollisionHashMap<K, V> m) {
        }

        /**
         * This method is invoked whenever the entry is removed from the table.
         */
        void recordRemoval(AntiCollisionHashMap<K, V> m) {
        }
    }

    /**
     * Adds a new entry with the specified key, value and hash code to the
     * specified bucket. It is the responsibility of this method to resize the
     * table if appropriate.
     *
     * Subclass overrides this to alter the behavior of put method.
     */
    void addEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K, V> e = table[bucketIndex];
        table[bucketIndex] = new Entry<K, V>(hash, key, value, e);
        if (size++ >= threshold)
            resize(2 * table.length);
    }

    /**
     * Like addEntry except that this version is used when creating entries as
     * part of Map construction or "pseudo-construction" (cloning,
     * deserialization). This version needn't worry about resizing the table.
     *
     * Subclass overrides this to alter the behavior of SafelyHashMap(Map),
     * clone, and readObject.
     */
    void createEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K, V> e = table[bucketIndex];
        table[bucketIndex] = new Entry<K, V>(hash, key, value, e);
        size++;
    }

    private abstract class HashIterator<E> implements Iterator<E> {
        Entry<K, V> next; // next entry to return
        int expectedModCount; // For fast-fail
        int index; // current slot
        Entry<K, V> current; // current entry

        HashIterator() {
            expectedModCount = modCount;
            if (size > 0) { // advance to first entry
                Entry[] t = table;
                while (index < t.length && (next = t[index++]) == null)
                    ;
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Entry<K, V> nextEntry() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            Entry<K, V> e = next;
            if (e == null)
                throw new NoSuchElementException();

            if ((next = e.next) == null) {
                Entry[] t = table;
                while (index < t.length && (next = t[index++]) == null)
                    ;
            }
            current = e;
            return e;
        }

        public void remove() {
            if (current == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            Object k = current.key;
            current = null;
            AntiCollisionHashMap.this.removeEntryForKey(k);
            expectedModCount = modCount;
        }

    }

    private final class ValueIterator extends HashIterator<V> {
        public V next() {
            return nextEntry().value;
        }
    }

    private final class KeyIterator extends HashIterator<K> {
        public K next() {
            return nextEntry().getKey();
        }
    }

    private final class EntryIterator extends HashIterator<Map.Entry<K, V>> {
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    // Subclass overrides these to alter behavior of views' iterator() method
    Iterator<K> newKeyIterator() {
        return new KeyIterator();
    }

    Iterator<V> newValueIterator() {
        return new ValueIterator();
    }

    Iterator<Map.Entry<K, V>> newEntryIterator() {
        return new EntryIterator();
    }

    // Views

    private transient Set<Map.Entry<K, V>> entrySet = null;

    /**
     * Returns a {@link Set} view of the keys contained in this map. The set is
     * backed by the map, so changes to the map are reflected in the set, and
     * vice-versa. If the map is modified while an iteration over the set is in
     * progress (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined. The set supports element
     * removal, which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt>, and <tt>clear</tt> operations. It does not support
     * the <tt>add</tt> or <tt>addAll</tt> operations.
     */
    public Set<K> keySet() {

        Set<K> ks = keySet;
        return (ks != null ? ks : (keySet = new KeySet()));
    }

    private final class KeySet extends AbstractSet<K> {
        public Iterator<K> iterator() {
            return newKeyIterator();
        }

        public int size() {
            return size;
        }

        public boolean contains(Object o) {
            return containsKey(o);
        }

        public boolean remove(Object o) {
            return AntiCollisionHashMap.this.removeEntryForKey(o) != null;
        }

        public void clear() {
            AntiCollisionHashMap.this.clear();
        }
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are reflected
     * in the collection, and vice-versa. If the map is modified while an
     * iteration over the collection is in progress (except through the
     * iterator's own <tt>remove</tt> operation), the results of the iteration
     * are undefined. The collection supports element removal, which removes the
     * corresponding mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     * <tt>clear</tt> operations. It does not support the <tt>add</tt> or
     * <tt>addAll</tt> operations.
     */
    public Collection<V> values() {
        Collection<V> vs = values;
        return (vs != null ? vs : (values = new Values()));
    }

    private final class Values extends AbstractCollection<V> {
        public Iterator<V> iterator() {
            return newValueIterator();
        }

        public int size() {
            return size;
        }

        public boolean contains(Object o) {
            return containsValue(o);
        }

        public void clear() {
            AntiCollisionHashMap.this.clear();
        }
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map. The set
     * is backed by the map, so changes to the map are reflected in the set, and
     * vice-versa. If the map is modified while an iteration over the set is in
     * progress (except through the iterator's own <tt>remove</tt> operation, or
     * through the <tt>setValue</tt> operation on a map entry returned by the
     * iterator) the results of the iteration are undefined. The set supports
     * element removal, which removes the corresponding mapping from the map,
     * via the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>
     * , <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support
     * the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet0();
    }

    private Set<Map.Entry<K, V>> entrySet0() {
        Set<Map.Entry<K, V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        public Iterator<Map.Entry<K, V>> iterator() {
            return newEntryIterator();
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<K, V> e = (Map.Entry<K, V>) o;
            Entry<K, V> candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }

        public boolean remove(Object o) {
            return removeMapping(o) != null;
        }

        public int size() {
            return size;
        }

        public void clear() {
            AntiCollisionHashMap.this.clear();
        }
    }

    /**
     * Save the state of the <tt>SafelyHashMap</tt> instance to a stream (i.e.,
     * serialize it).
     *
     * @serialData The <i>capacity</i> of the SafelyHashMap (the length of the
     *             bucket array) is emitted (int), followed by the <i>size</i>
     *             (an int, the number of key-value mappings), followed by the
     *             key (Object) and value (Object) for each key-value mapping.
     *             The key-value mappings are emitted in no particular order.
     */
    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        Iterator<Map.Entry<K, V>> i = (size > 0) ? entrySet0().iterator()
                : null;

        // Write out the threshold, loadfactor, and any hidden stuff
        s.defaultWriteObject();

        // Write out number of buckets
        s.writeInt(table.length);

        // Write out size (number of Mappings)
        s.writeInt(size);

        // Write out keys and values (alternating)
        if (i != null) {
            while (i.hasNext()) {
                Map.Entry<K, V> e = i.next();
                s.writeObject(e.getKey());
                s.writeObject(e.getValue());
            }
        }
    }

    private static final long serialVersionUID = 362498820763181265L;

    /**
     * Reconstitute the <tt>SafelyHashMap</tt> instance from a stream (i.e.,
     * deserialize it).
     */
    private void readObject(java.io.ObjectInputStream s) throws IOException,
            ClassNotFoundException {
        // Read in the threshold, loadfactor, and any hidden stuff
        s.defaultReadObject();

        // Read in number of buckets and allocate the bucket array;
        int numBuckets = s.readInt();
        table = new Entry[numBuckets];

        init(); // Give subclass a chance to do its thing.

        // Read in size (number of Mappings)
        int size = s.readInt();

        // Read the keys and values, and put the mappings in the SafelyHashMap
        for (int i = 0; i < size; i++) {
            K key = (K) s.readObject();
            V value = (V) s.readObject();
            putForCreate(key, value);
        }
    }

    // These methods are used when serializing HashSets
    int capacity() {
        return table.length;
    }

    float loadFactor() {
        return loadFactor;
    }

}