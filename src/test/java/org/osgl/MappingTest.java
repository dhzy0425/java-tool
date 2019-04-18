package org.osgl;

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

import static org.osgl.Lang.requireNotNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.joda.time.DateTime;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.osgl.exception.MappingException;
import org.osgl.exception.UnexpectedClassNotFoundException;
import org.osgl.util.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(Enclosed.class)
public class MappingTest extends TestBase {

    enum Color {R, G, B}

    static class Foo {
        public int id = N.randInt();
        public int[] ia = {1, 2, 3};
        public List<String> l1 = C.list("a", "b");
        public String color = $.random("R", "G", "B");
        public String name = S.random();
        public Date createDate = new Date();
        public Set<Integer> si = C.newSet(1, 2);
        private String __enhanced_field = S.random();
        private String fixxld = S.random();
        private String field_with__ = S.random();
        private String __some_super_field = S.random();
        private int __a_super_value = N.randInt();
        private String excludeMe = S.random();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Foo foo = (Foo) o;
            return id == foo.id &&
                    Arrays.equals(ia, foo.ia) &&
                    Objects.equals(name, foo.name) &&
                    Objects.equals(createDate, foo.createDate);
        }

        @Override
        public int hashCode() {

            int result = Objects.hash(id, name, createDate);
            result = 31 * result + Arrays.hashCode(ia);
            return result;
        }
    }

    static class Bar {
        public DateTime create_date = DateTime.now();
        public Color color = $.random(Color.values());
        public int id = N.randInt();
        public int[] ia = {1, 2};
        public String[] l1 = {"1", "x"};
        public String name = S.random();
        public long value = N.randLong();
        public Set<Integer> si = C.newSet(10, 20);
        private String __enhanced_field = S.random();
        private String fixxld = S.random();
        private String field_with__ = S.random();
        private String __some_super_field = S.random();
        private int __a_super_value = N.randInt();
        private String excludeMe = S.random();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Bar bar = (Bar) o;
            return id == bar.id &&
                    value == bar.value &&
                    Arrays.equals(ia, bar.ia) &&
                    Objects.equals(name, bar.name) &&
                    Objects.equals(create_date, bar.create_date);
        }

        @Override
        public int hashCode() {

            int result = Objects.hash(id, name, value, create_date);
            result = 31 * result + Arrays.hashCode(ia);
            return result;
        }
    }

    static class Bean {
        public Foo foo = new Foo();
        public Map<String, Bar> map = C.Map("bar1", new Bar());

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Bean bean = (Bean) o;
            return $.eq(foo, bean.foo) &&
                    $.eq(C.Map(map), C.Map(bean.map));
        }

        @Override
        public int hashCode() {
            return Objects.hash(foo, map);
        }
    }

    static Lang.TypeConverter<Date, DateTime> DATE_TO_DATETIME = new Lang.TypeConverter<Date, DateTime>() {
        @Override
        public DateTime convert(Date date) {
            return new DateTime(date);
        }
    };

    static Lang.TypeConverter<DateTime, Date> DATETIME_TO_DATE = new Lang.TypeConverter<DateTime, Date>() {
        @Override
        public Date convert(DateTime dateTime) {
            return new Date(dateTime.getMillis());
        }
    };

    @Ignore
    static class Base {

        Foo foo1;
        Foo foo2;
        Foo foo3;

        Bar bar1;
        Bar bar2;
        Bar bar3;

        Foo[] foo_1_array;
        Foo[] foo_2_array;
        Foo[] foo_3_array;

        Bar[] bar_1_array;
        Bar[] bar_2_array;
        Bar[] bar_3_array;

        List<Foo> foo_1_list;
        List<Foo> foo_2_list;
        List<Foo> foo_3_list;

        List<Bar> bar_1_list;
        List<Bar> bar_2_list;
        List<Bar> bar_3_list;

        Bean bean;

        int[] int_3_array;


        @Before
        public void init() {
            foo1 = new Foo();
            foo2 = new Foo();
            foo3 = new Foo();

            bar1 = new Bar();
            bar2 = new Bar();
            bar3 = new Bar();

            foo_1_array = new Foo[]{foo1};
            foo_2_array = new Foo[]{foo1, foo2};
            foo_3_array = new Foo[]{foo1, foo2, foo3};

            bar_1_array = new Bar[]{bar1};
            bar_2_array = new Bar[]{bar1, bar2};
            bar_3_array = new Bar[]{bar1, bar2, bar3};

            foo_1_list = C.list(foo1);
            foo_2_list = C.list(foo1, foo2);
            foo_3_list = C.list(foo1, foo2, foo3);

            bar_1_list = C.list(bar1);
            bar_2_list = C.list(bar1, bar2);
            bar_3_list = C.list(bar1, bar2, bar3);

            bean = new Bean();

            int_3_array = new int[]{1, 2, 3};

            OsglConfig.addGlobalMappingFilters("contains:super");
            OsglConfig.addGlobalMappingFilters("reg:.*xx.*");
            OsglConfig.addGlobalMappingFilters("starts:__enhanced");
            OsglConfig.addGlobalMappingFilters("ends:__");
            OsglConfig.addGlobalMappingFilters("excludeMe");
        }

    }


    public static class CopyArrayToArray extends Base {

        @Test
        public void simpleCase() {
            int[] ia = new int[3];
            int[] result = $.copy(int_3_array).to(ia);
            same(result, ia);
            eq("123", S.join(result).get());
        }

        @Test
        public void targetArrayWithNoEnoughSlot() {
            int[] ia = new int[2];
            int[] result = $.copy(int_3_array).to(ia);
            notSame(result, ia);
            eq("123", S.join(result).get());
        }

        @Test
        public void itShallClearExistingArray() {
            int[] ia = {10, 9, 8, 7};
            int[] result = $.copy(int_3_array).to(ia);
            same(result, ia);
            eq("1230", S.join(result).get());
        }

        @Test
        public void testArrayClone() {
            int[] ia = {10, 9, 8, 7};
            int[] result = $.cloneOf(ia);
            eq(ia, result);
            notSame(ia, result);
        }

    }

    public static class MergeArrayToArray extends Base {

        @Test
        public void simpleCase() {
            int[] ia = new int[3];
            int[] result = $.merge(int_3_array).to(ia);
            same(result, ia);
            eq("123", S.join(result).get());
        }

        @Test
        public void itShallNotClearExistingArray() {
            int[] ia = {10, 9, 8, 7};
            int[] result = $.merge(int_3_array).to(ia);
            same(result, ia);
            eq("1237", S.join(result).get());
        }

    }

    public static class MapArrayToArray extends Base {

        @Test
        public void mapToArrayWithConvertibleType() {
            String[] sa = new String[3];
            String[] result = $.map(int_3_array).to(sa);
            same(result, sa);
            eq("2", result[1]);
            eq("123", S.join(result).get());
        }

        @Test(expected = UnexpectedClassNotFoundException.class)
        public void mapToArrayWithNonConvertibleType() {
            Class[] ca = new Class[3];
            $.map(int_3_array).to(ca);
        }

        @Test
        public void arrayOfPojo() {
            Bar bar1_copy = $.cloneOf(bar1);
            notSame(bar1_copy, bar1);
            eq(bar1_copy, bar1);

            Object source = foo_3_array;
            Bar[] ba = bar_3_array;
            Bar[] result = $.mergeMap(source).withConverter(DATE_TO_DATETIME, DATETIME_TO_DATE).to(ba);

            same(result, ba);
            meq(foo1, result[0]);
            meq(foo2, result[1]);
            meq(foo3, result[2]);

            ne(bar1_copy, bar1);
        }

    }

    public static class PojoToPojo extends Base {

        @Test
        public void testStackoverflowCase() {
            Bar source = new Bar();
            Bar target = new Bar();
            $.merge(source).to(target);
        }

        @Test
        public void deepCopySimpleCase() {
            Foo source = new Foo();
            Foo target = new Foo();
            Foo result = $.deepCopy(source).to(target);
            same(result, target);
            notSame(source, target);
            eq(source, target);
            notSame(source.ia, target.ia);
            eq(source.si, target.si);
            notSame(source.si, target.si);
            same(source.color, target.color);
        }

        @Test
        public void deepCopyToDifferentType() throws Exception {
            Foo source = foo1;
            Thread.sleep(10);
            Bar target = new Bar();
            Bar result = $.deepCopy(source).filter("-color").to(target);
            same(result, target);
            eq(source.id, target.id);
            eq(source.name, target.name);
            eq(source.ia, target.ia);
            eq(source.si, target.si);
            notSame(source.ia, target.ia);
            isNull(target.create_date);
        }

        @Test(expected = MappingException.class)
        public void deepCopyWithTypeMismatch() {
            Foo source = new Foo();
            Bar target = new Bar();
            $.deepCopy(source).keywordMatching().to(target);
        }

        @Test
        public void deepCopyIgnoreError() throws Exception {
            Foo source = foo1;
            Thread.sleep(10);
            Bar target = new Bar();
            $.deepCopy(source).keywordMatching().ignoreError().to(target);
            eq(source.id, target.id);
            eq(source.name, target.name);
            eq(source.ia, target.ia);
            eq(source.si, target.si);
            notSame(source.ia, target.ia);
            requireNotNull(target.create_date); // there are initial value
            ne(source.createDate.getTime(), target.create_date.getMillis());
        }


        @Test
        public void testMerge() throws Exception {
            Foo source = foo1;
            Thread.sleep(10);
            Bar target = new Bar();
            $.merge(source).filter("-color").to(target);
            eq(source.id, target.id);
            eq(source.name, target.name);
            eq(source.ia, target.ia);
            ne(source.si, target.si);
            // because merge use strict name matching thus
            // foo.createDate cannot be map into bar.create_date
            // however merge will leave target field unchanged if source field is null
            // in this case it assume foo.create_date (which doesn't exits) is null
            requireNotNull(target.create_date);
            ne(source.createDate.getTime(), target.create_date.getMillis());
            yes(target.si.containsAll(source.si));
        }

        @Test
        public void testMergeMapping() {
            Foo source = foo1;
            Bar target = bar1;
            $.mergeMap(source).withConverter(DATE_TO_DATETIME).to(target);
            eq(source.id, target.id);
            eq(source.name, target.name);
            eq(source.ia, target.ia);
            ne(source.si, target.si);
            yes(target.si.containsAll(source.si));
            requireNotNull(target.create_date);
            eq(source.createDate.getTime(), target.create_date.getMillis());
            yes(target.si.containsAll(source.si));
        }

        @Test
        public void testMapping() {
            Foo source = foo1;
            Bar target = bar1;
            $.map(source).withConverter(DATE_TO_DATETIME).to(target);
            eq(source.id, target.id);
            eq(source.name, target.name);
            eq(source.ia, target.ia);
            eq(source.si, target.si);
            yes(target.si.containsAll(source.si));
            eq(source.color, target.color.name());
            requireNotNull(target.create_date);
            eq(source.createDate.getTime(), target.create_date.getMillis());
        }

        @Test
        public void testGlobalFilter() {
            Foo source = foo1;
            Bar target = new Bar();
            $.deepCopy(source).filter("-color").to(target);
            eq(source.id, target.id);
            eq(source.name, target.name);
            ne(source.__a_super_value, target.__a_super_value);
            ne(source.__enhanced_field, target.__enhanced_field);
            ne(source.__some_super_field, target.__some_super_field);
            ne(source.__a_super_value, target.__a_super_value);
            ne(source.field_with__, target.field_with__);
            ne(source.excludeMe, target.excludeMe);
        }

        @Test
        public void testIgnoreGlobalFilter() {
            Foo source = foo1;
            Bar target = new Bar();
            $.deepCopy(source).filter("-color").ignoreGlobalFilter().to(target);
            eq(source.id, target.id);
            eq(source.name, target.name);
            eq(source.__a_super_value, target.__a_super_value);
            eq(source.__enhanced_field, target.__enhanced_field);
            eq(source.__some_super_field, target.__some_super_field);
            eq(source.__a_super_value, target.__a_super_value);
            eq(source.field_with__, target.field_with__);
            eq(source.excludeMe, target.excludeMe);
        }

        @Test
        public void testComplexDeepCopy() {
            Bean source = new Bean();
            Bean target = new Bean();
            Bean result = $.deepCopy(source).to(target);
            same(target, result);
            eq(target, source);
            notSame(source.foo, target.foo);
            notSame(source.foo.ia, target.foo.ia);
            notSame(source.map.get("bar1"), target.map.get("bar1"));
        }

        @Test
        public void testDeepCopyWithFilter() {
            Bean source = new Bean();
            Bean target = new Bean();
            $.deepCopy(source).filter("-map.bar1.name,-foo.name").to(target);
            ne(target, source);

            Foo sourceFoo = source.foo;
            Foo targetFoo = target.foo;
            ne(sourceFoo, targetFoo);
            eq(sourceFoo.createDate, targetFoo.createDate);
            eq(sourceFoo.id, targetFoo.id);
            eq(sourceFoo.ia, targetFoo.ia);
            notSame(sourceFoo.ia, targetFoo.ia);
            ne(sourceFoo.name, targetFoo.name);

            Bar sourceBar = source.map.get("bar1");
            Bar targetBar = target.map.get("bar1");
            ne(sourceBar, targetBar);
            eq(sourceBar.create_date, targetBar.create_date);
            eq(sourceBar.id, targetBar.id);
            eq(sourceBar.ia, targetBar.ia);
            notSame(sourceBar.ia, targetBar.ia);
            ne(sourceBar.name, targetBar.name);
        }

        @Test
        public void testShallowCopy() {
            Bean source = new Bean();
            Bean target = new Bean();
            Bean result = $.copy(source).to(target);
            same(target, result);
            eq(target, source);
            same(source.foo, target.foo);
            same(source.map.get("bar1"), target.map.get("bar1"));
            same(source.foo.si, target.foo.si);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testShallowCopyToDifferentType() {
            Foo source = new Foo();
            $.copy(source).to(Bar.class);
        }

        @Test
        public void testShallowCopyToDifferentTypeIgnoreError() {
            Foo source = new Foo();
            Bar target = $.copy(source).ignoreError().to(Bar.class);
            same(target.ia, source.ia);
            eq(target.id, source.id);
            same(target.name, source.name);
            same(target.si, source.si);
        }
    }

    public static class CopyListToList extends Base {

        public List<Foo> fooList = C.list(new Foo(), new Foo());
        public List<Bar> barList = C.newList();

        @Test
        public void test() {
            $.map(fooList).targetGenericType(new TypeReference<List<Bar>>() {
            }).to(barList);
            eq(2, barList.size());
            Foo foo = fooList.get(0);
            Bar bar = barList.get(0);
            eq(foo.name, bar.name);
            eq(foo.ia, bar.ia);
        }

    }

    public static class HeaderMapping extends TestBase {

        public static class Foo {
            String no;

            @Override
            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (obj instanceof Foo) {
                    return S.eq(((Foo) obj).no, no);
                }
                return false;
            }

            static Foo of(String no) {
                Foo foo = new Foo();
                foo.no = no;
                return foo;
            }
        }

        public static class FooHost {
            Foo foo = Foo.of("2");
        }

        public static class Bar {
            int id;

            @Override
            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (obj instanceof Bar) {
                    return ((Bar) obj).id == id;
                }
                return false;
            }

            static Bar of(int id) {
                Bar bar = new Bar();
                bar.id = id;
                return bar;
            }
        }

        public static class BarHost {
            Bar bar = Bar.of(1);
        }

        @Test
        public void testSimpleCase() {
            Bar bar = Bar.of(12);
            eq(Foo.of("12"), $.map(bar).mapHead("id").to("no").to(Foo.class));
        }

        @Test
        public void testArrayToArray() {
            Bar[] bars = {Bar.of(1), Bar.of(2)};
            Foo[] target = new Foo[2];
            Foo[] expected = {Foo.of("1"), Foo.of("2")};
            eq(expected, $.map(bars).mapHead("id").to("no").to(target));
        }

        @Test
        public void testMapToPojo() {
            Map<String, String> source = C.Map("key", "123");
            Bar target = new Bar();
            $.map(source).mapHead("key").to("id").to(target);
            eq(123, target.id);
        }

        @Test
        public void testNested() {
            FooHost source = new FooHost();
            BarHost target = new BarHost();
            eq(1, target.bar.id);
            $.map(source).mapHead("foo").to("bar").mapHead("foo.no").to("bar.id").to(target);
            eq(2, target.bar.id);
        }

    }

    static void eq(Foo foo, Bar bar) {
        eq(foo, bar, false);
    }

    static void meq(Foo foo, Bar bar) {
        eq(foo, bar, true);
    }

    static void eq(Foo foo, Bar bar, boolean isMapping) {
        eq(foo.id, bar.id);
        eq(foo.name, bar.name);
        if (isMapping) {
            eq(foo.createDate.getTime(), bar.create_date.getMillis());
        }
        if (null == foo.ia) {
            isNull(bar.ia);
        } else {
            requireNotNull(bar.ia);
            eq(foo.ia, bar.ia);
        }
    }

    public static class Miscs extends TestBase {

        public static class RawData {
            Calendar date;

            public RawData(long currentTimeMillis) {
                date = Calendar.getInstance();
                date.setTimeInMillis(currentTimeMillis);
            }
        }

        public static class ConvertedData {
            DateTime date;
        }

        public static Lang.TypeConverter<Calendar, DateTime> converter = new Lang.TypeConverter<Calendar, DateTime>() {
            @Override
            public DateTime convert(Calendar calendar) {
                return new DateTime(calendar.getTimeInMillis());
            }
        };

        @Test
        public void testWithTypeConverter() {
            RawData src = new RawData($.ms());
            ConvertedData tgt = $.map(src).withConverter(converter).to(ConvertedData.class);
            eq(tgt.date.getMillis(), src.date.getTimeInMillis());
        }

        public static class RawDataV2 {
            String date;

            public RawDataV2(String date) {
                this.date = date;
            }
        }

        public static class ConvertedDataV2 {
            Date date;
        }

        @Test
        public void testTypeConvertWithHint() throws Exception {
            RawDataV2 src = new RawDataV2("20180518");
            ConvertedDataV2 tgt = $.map(src).conversionHint(Date.class, "yyyyMMdd").to(ConvertedDataV2.class);
            Date expected = new SimpleDateFormat("yyyyMMdd").parse("20180518");
            eq(expected, tgt.date);
        }

        @Test
        public void testObjectToMap() {
            Foo foo = new Foo();
            Map map = $.deepCopy(foo).to(Map.class);
            eq(foo.name, map.get("name"));
            eq(foo.si, map.get("si"));
            eq(foo.id, map.get("id"));
            eq(foo.ia, map.get("ia"));
            eq(foo.createDate, map.get("createDate"));
            eq(foo.l1, map.get("l1"));
        }

        @Test
        public void testListObjectToListMap() {
            Foo foo = new Foo();
            List<Foo> fooList = C.list(foo);
            List list = $.map(fooList).targetGenericType(new TypeReference<List<Map>>() {
            }).to(List.class);
            Map map = (Map) list.get(0);
            eq(foo.name, map.get("name"));
            eq(foo.si, map.get("si"));
            eq(foo.id, map.get("id"));
            eq(foo.ia, map.get("ia"));
            eq(foo.createDate, map.get("createDate"));
            eq(foo.l1, map.get("l1"));
        }

        @Test
        public void testFlatMapFromMap() {
            Map<String, Object> source = new HashMap<>();
            source.put("id", S.random());
            Map<String, Object> nest1 = new HashMap<>();
            nest1.put("id", S.random());
            Map<String, Object> nest2 = new HashMap<>();
            nest2.put("id", S.random());
            nest1.put("nest2", nest2);
            source.put("nest1", nest1);
            Map<String, Object> target = $.flatCopy(source).to(Map.class);
            eq(source.get("id"), target.get("id"));
            eq(nest1.get("id"), target.get("nest1.id"));
            eq(nest2.get("id"), target.get("nest1.nest2.id"));
        }

    }


}
