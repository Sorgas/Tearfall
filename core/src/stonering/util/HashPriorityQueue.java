package stonering.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Alexander Kuzyakov on 07.02.2018.
 */
public class HashPriorityQueue<K, V> {
    public final HashMap<K, V> hashMap;
    public final TreeMap<V, K> treeMap;

    public HashPriorityQueue(Comparator<V> comp) {
        hashMap = new HashMap<>();
        treeMap = new TreeMap<>(comp);
    }

    public int size() {
        return this.treeMap.size();
    }

    public boolean isEmpty() {
        return this.treeMap.isEmpty();
    }

    public boolean contains(K key) {
        return this.hashMap.containsKey(key);
    }

    public V get(K key) {
        return this.hashMap.get(key);
    }

    public boolean add(K key, V value) {
        this.hashMap.put(key, value);
        this.treeMap.put(value, key);
        return true;
    }
    
    public boolean remove(K key, V value) {
        if (value == null) {
            value = this.hashMap.get(key);
        }
        this.hashMap.remove(key);
        this.treeMap.remove(value);
        return true;
    }

    public V poll() {
        Map.Entry<V, K> entry = this.treeMap.pollFirstEntry();
        return entry.getKey();
    }
}
