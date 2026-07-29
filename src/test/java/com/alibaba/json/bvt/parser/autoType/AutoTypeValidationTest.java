package com.alibaba.json.bvt.parser.autoType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Regression tests for the autoType hardening ported from fastjson2 #7703:
 * 1. type names with URL-special characters (':' or '!') never reach the class loader
 * 2. a whitelist hash match is verified against the accept name text
 * 3. an accept prefix does not cover ClassLoader/DataSource/RowSet gadget base types
 */
public class AutoTypeValidationTest extends TestCase {
    public static class Model {
        public int value;
    }

    public static class EvilClassLoader extends ClassLoader {
    }

    public static class EvilClassLoader2 extends ClassLoader {
    }

    private static void assertAutoTypeRejected(ParserConfig config, String typeName) {
        try {
            config.checkAutoType(typeName, null, 0);
            fail("expected JSONException for " + typeName);
        } catch (JSONException expected) {
            // rejected, same as any unresolvable @type in 1.2.83
        }
    }

    // 1a. checkAutoType rejects URL-special type names before any class loader sees them
    public void test_illegal_type_name_chars_checkAutoType() {
        ParserConfig config = new ParserConfig();
        assertAutoTypeRejected(config, "jar:http://evil/x.jar!/Payload");
        assertAutoTypeRejected(config, "http://example.com/Type");
    }

    // 1b. same under SupportAutoType
    public void test_illegal_type_name_chars_autoTypeSupport() {
        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);
        assertAutoTypeRejected(config, "jar:http://evil/x.jar!/Payload");
    }

    // 1c. TypeUtils.loadClass rejects the name before consulting any class loader
    public void test_loadClass_never_sees_url_names() {
        final List<String> requested = new ArrayList<String>();
        ClassLoader recording = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                requested.add(name);
                throw new ClassNotFoundException(name);
            }
        };

        assertNull(TypeUtils.loadClass("jar:http://evil/x.jar!/Payload", recording, true));
        assertNull(TypeUtils.loadClass("http://example.com/Type", recording, false));
        assertEquals(0, requested.size());
    }

    // 1d. a JSON-LD style IRI @type is rejected the same way 1.2.83 rejects any
    // unresolvable @type; the name never reaches a class loader
    public void test_jsonld_type_iri_rejected() {
        try {
            JSON.parseObject("{\"@type\":\"http://example.com/Type\",\"value\":1}");
            fail("expected JSONException");
        } catch (JSONException expected) {
            // same behavior as 1.2.83 for unresolvable @type
        }
    }

    // 2. an accept hash alone (simulated FNV collision) does not whitelist a type name
    public void test_accept_hash_collision_rejected() throws Exception {
        ParserConfig config = new ParserConfig();

        Field field = ParserConfig.class.getDeclaredField("acceptHashCodes");
        field.setAccessible(true);
        long[] hashes = (long[]) field.get(config);
        long[] injected = Arrays.copyOf(hashes, hashes.length + 1);
        injected[hashes.length] = TypeUtils.fnv1a_64("com.evil.");
        Arrays.sort(injected);
        field.set(config, injected);

        // hash matches at prefix "com.evil." but the name was never added to the accept list
        assertAutoTypeRejected(config, "com.evil.Payload");

        config.setAutoTypeSupport(true);
        assertNull(config.checkAutoType("com.evil.Payload", null, 0));
    }

    // 2b. a genuine accept entry still resolves, including the nested-class spellings
    public void test_accept_entry_resolves() {
        ParserConfig config = new ParserConfig();
        config.addAccept("com.alibaba.json.bvt.parser.autoType.AutoTypeValidationTest$Model");
        Class<?> clazz = config.checkAutoType(
                "com.alibaba.json.bvt.parser.autoType.AutoTypeValidationTest$Model", null, 0);
        assertSame(Model.class, clazz);

        // the canonical spelling hashes identically after '$' -> '.' normalization
        ParserConfig config2 = new ParserConfig();
        config2.addAccept("com.alibaba.json.bvt.parser.autoType.AutoTypeValidationTest.Model");
        assertSame(Model.class, config2.checkAutoType(
                "com.alibaba.json.bvt.parser.autoType.AutoTypeValidationTest$Model", null, 0));
    }

    // 3a. an accept prefix does not cover ClassLoader/DataSource/RowSet gadget base types
    public void test_accept_prefix_does_not_cover_deny_class() {
        ParserConfig config = new ParserConfig();
        config.addAccept("com.alibaba.json.bvt.parser.autoType.");
        assertAutoTypeRejected(config,
                "com.alibaba.json.bvt.parser.autoType.AutoTypeValidationTest$EvilClassLoader");

        ParserConfig config2 = new ParserConfig();
        config2.setAutoTypeSupport(true);
        config2.addAccept("com.alibaba.json.bvt.parser.autoType.");
        assertAutoTypeRejected(config2,
                "com.alibaba.json.bvt.parser.autoType.AutoTypeValidationTest$EvilClassLoader");
    }

    // 3b. an accept entry naming the type in full is an explicit opt-in
    public void test_accept_full_name_opt_in() {
        ParserConfig config = new ParserConfig();
        config.addAccept("com.alibaba.json.bvt.parser.autoType.AutoTypeValidationTest$EvilClassLoader");
        assertSame(EvilClassLoader.class, config.checkAutoType(
                "com.alibaba.json.bvt.parser.autoType.AutoTypeValidationTest$EvilClassLoader", null, 0));
    }

    // 4. a deny class rejected under SupportAutoType must stay rejected on a later call:
    // the failed attempt must not be served back from the class mapping cache
    public void test_deny_class_not_cached_after_rejection() {
        String typeName = "com.alibaba.json.bvt.parser.autoType.AutoTypeValidationTest$EvilClassLoader2";
        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);
        assertAutoTypeRejected(config, typeName);
        assertAutoTypeRejected(config, typeName);
        assertNull(TypeUtils.getClassFromMapping(typeName));
    }
}
