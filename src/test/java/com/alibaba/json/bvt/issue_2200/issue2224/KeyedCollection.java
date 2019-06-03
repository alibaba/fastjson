package com.alibaba.json.bvt.issue_2200.issue2224;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class KeyedCollection<TKey, TItem> implements CollectionEx<TItem>, Cloneable {
    private transient Map<TKey, TItem> items = new LinkedHashMap<TKey, TItem>();

    protected abstract TKey getKeyForItem(TItem item);

    public TItem get(TKey key) {
        return this.items.get(key);
    }

    //region override

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public boolean contains(Object key) {
        return this.items.containsKey(key);
    }

    @Override
    public Iterator<TItem> iterator() {
        return this.items.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return this.items.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.items.values().toArray(a);
    }

    @Override
    public boolean add(TItem item) {
        if (item == null)
            throw new IllegalArgumentException("item can not be null.");
        TKey key = this.getKeyForItem(item);
        this.items.put(key, item);
        return true;
    }

    @Override
    public boolean remove(Object key) {
        return this.items.remove(key) != null;
    }

    @Override
    public boolean containsAll(Collection<?> keys) {
        return this.items.keySet().containsAll(keys);
    }

    @Override
    public boolean addAll(Collection<? extends TItem> items) {
        boolean modified = false;
        for (TItem item : items)
            modified |= this.add(item);
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> keys) {
        boolean modified = false;
        for (Object key : keys)
            modified |= this.remove(key);
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> keys) {
        boolean modified = false;
        for (TKey key : this.items.keySet()) {
            if (!keys.contains(key))
                modified |= this.remove(key);
        }
        return modified;
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
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
