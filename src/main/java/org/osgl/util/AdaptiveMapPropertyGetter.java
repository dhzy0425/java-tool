package org.osgl.util;

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

import org.osgl.Lang;

import java.util.Map;

/**
 * Implement {@link PropertyGetter} on a {@link Map} type entity
 */
public class AdaptiveMapPropertyGetter extends MapPropertyHandler implements PropertyGetter {

    public AdaptiveMapPropertyGetter(Class<?> keyType, Class<?> valType) {
        super(keyType, valType);
    }

    public AdaptiveMapPropertyGetter(NullValuePolicy nullValuePolicy, Class<?> keyType, Class<?> valType) {
        super(nullValuePolicy, keyType, valType);
    }

    public AdaptiveMapPropertyGetter(Lang.Function<Class<?>, Object> objectFactory,
                                     Lang.Func2<String, Class<?>, ?> stringValueResolver,
                                     Class<?> keyType,
                                     Class<?> valType) {
        super(objectFactory, stringValueResolver, keyType, valType);
    }

    public AdaptiveMapPropertyGetter(Lang.Function<Class<?>, Object> objectFactory,
                                     Lang.Func2<String, Class<?>, ?> stringValueResolver,
                                     NullValuePolicy nullValuePolicy,
                                     Class<?> keyType,
                                     Class<?> valType) {
        super(objectFactory, stringValueResolver, nullValuePolicy, keyType, valType);
    }

    @Override
    public Object get(Object entity, Object index) {
        AdaptiveMap map = (AdaptiveMap) entity;
        String key = S.string(keyFrom(index));
        Object val = map.getValue(key);
        if (null == val) {
            switch (nullValuePolicy) {
                case NPE:
                    throw new NullPointerException();
                case CREATE_NEW:
                    val = objectFactory.apply(valType);
                    map.putValue(key, val);
                default:
                    // do nothing
            }
        }
        return val;
    }

    @Override
    public PropertySetter setter() {
        return new AdaptiveMapPropertySetter(objectFactory, stringValueResolver, keyType, valType);
    }
}
