package com.alibaba.json.bvt.issue_2200.issue2224;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

abstract class KeyedCollection<TKey, TItem> implements CollectionEx<TItem>, Cloneable {
    private transient Map<TKey, TItem> items = new LinkedHashMap<TKey, TItem>();

    protected abstract TKey getKeyForItem(TItem item);

    public TItem get(TKey key) {
        return this.items.get(key);
    }

    //region override

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public boolean contains(Object key) {
        return this.items.containsKey(key);
    }

    public Iterator<TItem> iterator() {
        return this.items.values().iterator();
    }

    public Object[] toArray() {
        return this.items.values().toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.items.values().toArray(a);
    }

    public boolean add(TItem item) {
        if (item == null)
            throw new IllegalArgumentException("item can not be null.");
        TKey key = this.getKeyForItem(item);
        this.items.put(key, item);
        return true;
    }

    public boolean remove(Object key) {
        return this.items.remove(key) != null;
    }

    public boolean containsAll(Collection<?> keys) {
        return this.items.keySet().containsAll(keys);
    }

    public boolean addAll(Collection<? extends TItem> items) {
        boolean modified = false;
        for (TItem item : items)
            modified |= this.add(item);
        return modified;
    }

    public boolean removeAll(Collection<?> keys) {
        boolean modified = false;
        for (Object key : keys)
            modified |= this.remove(key);
        return modified;
    }

    public boolean retainAll(Collection<?> keys) {
        boolean modified = false;
        for (TKey key : this.items.keySet()) {
            if (!keys.contains(key))
                modified |= this.remove(key);
        }
        return modified;
    }

    public void clear() {
        this.items.clear();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        TItem item;
        Iterator<TItem> iterator = this.iterator();
        if (iterator.hasNext()) {
            item = iterator.next();
            builder.append(item == this ? "(this Collection)" : item);
        }
        while (iterator.hasNext()) {
            item = iterator.next();
            builder.append(", ").append(item == this ? "(this Collection)" : item);
        }
        builder.append(']');
        return builder.toString();
    }

    //endregion
}
