/*
 * Copyright (C) 2013 The OSGL cache project
 * Gelin Luo <greenlaw110(at)gmail.com>
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package org.osgl.cache;

/*-
 * #%L
 * Java Tool
 * %%
 * Copyright (C) 2014 - 2017 OSGL (Open Source General Library)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Defines a cache service
 */
public interface CacheService {

    enum State {
        INITIALIZED, STARTED, SHUTDOWN;

        public boolean isInitialized() {
            return INITIALIZED == this;
        }

        public boolean isStarted() {
            return STARTED == this;
        }

        public boolean isShutdown() {
            return SHUTDOWN == this;
        }
    }

    String DEF_CACHE_NAME = "osgl-cache";

    /**
     * Store an item into the cache service by key and set ttl value
     *
     * @param key the cache key
     * @param value the object to be cached
     * @param ttl   time to live of the cached item. Time unit is second.
     *              If set to negative number, then it will never expire.
     *              If set to zero then the default ttl value will be used
     */
    void put(String key, Object value, int ttl);

    /**
     * Store an item into the cache by key and use default ttl
     *
     * @param key the cache key
     * @param value the object to be cached
     */
    void put(String key, Object value);

    /**
     * Remove an item from the cache service by key
     * @param key the cache key
     */
    void evict(String key);

    /**
     * Return an item from the cache service by key
     *
     * @param key the cache key
     * @param <T> the generic type of the return value
     * @return the value associated with the key
     */
    <T> T get(String key);

    /**
     * Increase an number type value associated with key `key` by `1`
     *
     * If no value is associated with the key, then it will associate
     * number `1` to `key` and return `0`.
     *
     * @param key
     *      the key index the numeric value
     * @return
     *      the int value before increment operation happening.
     */
    int incr(String key);

    /**
     * Increase an number type value associated with key `key` with expires specified
     *
     * If no value is associated with the key, then it will associate
     * number `1` to `key` and return `0`.
     *
     * @param key
     *      the key index the numeric value
     * @param ttl
     *      the number of seconds the key to expire
     * @return
     *      the int value before increment operation happening.
     */
    int incr(String key, int ttl);

    /**
     * Decrease an number type value associated with key `key` by `1`
     *
     * If no value is associated with the key, then it will associate
     * number `-1` to `key` and return `0`.
     *
     * @param key
     *      the key index the numeric value
     * @return
     *      the int value before decrement operation happening.
     */
    int decr(String key);

    /**
     * Decrease an number type value associated with key `key` by `1`
     * with expires specified
     *
     * If no value is associated with the key, then it will associate
     * number `-1` to `key` and return `0`.
     *
     * @param key
     *      the key index the numeric value
     * @param ttl
     *      the number of seconds the key to expire
     * @return
     *      the int value before decrement operation happening.
     */
    int decr(String key, int ttl);

    /**
     * Remove all cached items
     */
    void clear();

    /**
     * Set default ttl value which will be used if user pass 0 as ttl or not specified ttl
     * Note some service might not favor this method after the internal cache service
     * is initialized. E.g. memcached and ehcaches
     *
     * @param ttl the default ttl value in seconds
     */
    void setDefaultTTL(int ttl);

    /**
     * Shutdown the cache service
     */
    void shutdown();

    /**
     * Restart the cache service after shutdown
     *
     * <p>Note, by default the cache service
     * should be started after initialized</p>
     */
    void startup();

    /**
     * Return state of this cache service.
     * @return {@link State} of this cache service.
     */
    State state();

}
