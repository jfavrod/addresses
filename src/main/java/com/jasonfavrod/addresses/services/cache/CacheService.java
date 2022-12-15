package com.jasonfavrod.addresses.services.cache;

/**
 * A simple key value store.
 */
public interface CacheService {
    /** Get a value from the CacheService. */
    String get(String key);

    /** Set a value for the given key. Implementations may set a default TTL. */
    void set(String key, String value);

    /** Set a value for the given key, and the number of seconds until the value expires. */
    void set(String key, String value, int secondsToExpire);
}
