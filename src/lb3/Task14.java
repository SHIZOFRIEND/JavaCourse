package lb3;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

class LruCache<K, V> {
    private final int capacity;
    private final LinkedHashMap<K, V> cache;

    public LruCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Ёмкость должна быть положительной");
        }

        this.capacity = capacity;

        cache = new LinkedHashMap<K, V>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(
                    Map.Entry<K, V> eldest) {
                return size() > LruCache.this.capacity;
            }
        };
    }

    public void put(K key, V value) {
        Objects.requireNonNull(key, "Ключ не может быть null");
        Objects.requireNonNull(value, "Значение не может быть null");
        cache.put(key, value);
    }

    public V get(K key) {
        Objects.requireNonNull(key, "Ключ не может быть null");
        return cache.get(key);
    }

    public boolean containsKey(K key) {
        Objects.requireNonNull(key, "Ключ не может быть null");
        return cache.containsKey(key);
    }

    public int size() {
        return cache.size();
    }

    public void printCache() {
        System.out.println(cache);
    }
}

public class Task14 {
    public static void main(String[] args) {
        LruCache<Integer, String> cache = new LruCache<>(3);

        cache.put(1, "Java");
        cache.put(2, "C#");
        cache.put(3, "Python");

        System.out.println("Начальный кэш:");
        cache.printCache();

        cache.get(1);

        System.out.println("После обращения к ключу 1:");
        cache.printCache();

        cache.put(4, "JavaScript");

        System.out.println("После добавления ключа 4:");
        cache.printCache();

        System.out.println("Ключ 2 существует: "
                + cache.containsKey(2));

        System.out.println("Ключ 1 существует: "
                + cache.containsKey(1));

        cache.get(3);
        cache.put(5, "C++");

        System.out.println("После обращения к 3 и добавления 5:");
        cache.printCache();

        System.out.println("Размер кэша: " + cache.size());

        try {
            cache.put(6, null);
        } catch (NullPointerException e) {
            System.out.println("null запрещён: " + e.getMessage());
        }
    }
}