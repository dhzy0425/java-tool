package org.osgl.util;

/*-
 * #%L
 * Java Tool
 * %%
 * Copyright (C) 2014 - 2018 OSGL (Open Source General Library)
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import org.osgl.$;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * **Note** the source code is copied from `com.alibaba.fastjson.TypeReference`
 *
 * Represents a generic type `T`. Java doesn't yet provide a way to
 * represent generic types, so this class does. Forces clients to create a
 * subclass of this class which enables retrieval the type information even at
 * runtime.
 *
 * For example, to create a type literal for `List<String>`, you can
 * create an empty anonymous inner class:
 *
 * ```java
 * TypeReference<List<String>> list = new TypeReference<List<String>>() {};
 * ```
 *
 * To create a generic list of type with given class:
 *
 * ```java
 * Type type = TypeReference.listOf(clazz);
 * ```
 *
 * This syntax cannot be used to create type literals that have wildcard
 * parameters, such as `Class<?>` or `List<? extends CharSequence>`.
 */
public class TypeReference<T> {
    static ConcurrentMap<Type, Type> classTypeCache
            = new ConcurrentHashMap<Type, Type>(16, 0.75f, 1);

    protected final Type type;

    /**
     * Constructs a new type literal. Derives represented class from type
     * parameter.
     *
     * <p>Clients create an empty anonymous subclass. Doing so embeds the type
     * parameter in the anonymous class's type hierarchy so we can reconstitute it
     * at runtime despite erasure.
     */
    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();

        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];

        Type cachedType = classTypeCache.get(type);
        if (cachedType == null) {
            classTypeCache.putIfAbsent(type, type);
            cachedType = classTypeCache.get(type);
        }

        this.type = cachedType;
    }

    /**
     * Gets underlying {@code Type} instance.
     */
    public Type getType() {
        return type;
    }

    public static Type listOf(Class<?> elementType) {
        return create(new Type[]{elementType}, List.class);
    }

    public static Type setOf(Class<?> elementType) {
        return create(new Type[]{elementType}, Set.class);
    }

    public static Type collectionOf(Class<?> elementType) {
        return create(new Type[]{elementType}, Collection.class);
    }

    public static Type mapOf(Class<?> keyType, Class<?> valType) {
        return create(new Type[]{keyType, valType}, Map.class);
    }

    public static Type IterableOf(Class<?> elementType) {
        return create(new Type[]{elementType}, Iterable.class);
    }

    private static Type create(Type[] actualTypeArguments, Type rawType) {
        return new ParameterizedTypeImpl(actualTypeArguments, null, rawType);
    }

    public final static Type LIST_STRING = new TypeReference<List<String>>() {}.getType();

}
