package io.github.milkdrinkers.crate;

import io.github.milkdrinkers.crate.annotation.ConfigPath;
import io.github.milkdrinkers.crate.internal.exceptions.CrateValidationException;
import io.github.milkdrinkers.crate.internal.settings.DataType;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class ConfigTest {
    static Config config;
    @TempDir
    File tempDir;

    @AfterEach
    void tearDown() {
        config.clear();
        Assertions.assertTrue(config.getFile().delete());
    }

    @BeforeEach
    @Test
    void setUp() {
        config = new Config("Example", tempDir.getPath());
        Assertions.assertEquals("Example.yml", config.getName());
        Assertions.assertEquals(new ArrayList<>(), config.getHeader());
    }

    @Test
    void testGetDataType() {
        Assertions.assertEquals(config.getDataType(), DataType.SORTED);
    }

    @Test
    void testGetters() {
        Object anObject = config.get("Key"); // Default: null
        String aString = config.getString("Key"); // Default: ""
        int anInt = config.getInt("Key"); // Default: 0
        double aDouble = config.getDouble("Key"); // Default: 0.0
        float aFloat = config.getFloat("Key"); // Default: 0.0
        long aLong = config.getLong("Key"); // Default: 0.0

        Optional<String> optionalString = config.find(
            "Key",
            String.class); // If a key is not present an empty optional will be returned
        String getOrDefault = config.getOrDefault("Key", "Default-Value");
        String getOrSetDefault = config.getOrSetDefault(
            "Key",
            "Default-Value-To-Be-Set-If-Not-Yet-Present");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> config.getEnum("Key", TimeUnit.class));
        Assertions.assertThrows(CrateValidationException.class,
            () -> config.getEnum("Key-1", TimeUnit.class));

        Assertions.assertNull(anObject);
        Assertions.assertEquals("", aString);
        Assertions.assertEquals(0, anInt);
        Assertions.assertEquals(0.0, aDouble);
        Assertions.assertEquals(0.0f, aFloat);
        Assertions.assertEquals(0.0, aLong);
        Assertions.assertEquals(Optional.empty(), optionalString);
        Assertions.assertEquals("Default-Value", getOrDefault);
        Assertions.assertEquals("Default-Value-To-Be-Set-If-Not-Yet-Present", getOrSetDefault);
    }

    @Test
    void testSetHeader() {
        config.setHeader("Example-1", "Example-2");
        Assertions.assertEquals(Arrays.asList("#Example-1", "#Example-2"), config.getHeader());
    }

    @Test
    void testSet() {
        config.set("Test-Key-1", true);
        Assertions.assertTrue(config.getData().containsKey("Test-Key-1"));
        Assertions.assertTrue(config.contains("Test-Key-1"));
        Assertions.assertTrue(config.getBoolean("Test-Key-1"));
    }

    @Test
    void testAnnotations() {
        config.set("annotation-test", "Annotation Test");
        config.set("section.annotations", 2);

        AnnotationTests test = new AnnotationTests();
        config.annotateClass(test);
        Assertions.assertEquals("Annotation Test", test.getAnnotationTest());
        Assertions.assertEquals(2, test.getAnnotationTest2());
    }

    @Getter
    static class AnnotationTests {
        @ConfigPath("annotation-test")
        public String annotationTest;
        @ConfigPath("section.annotations")
        public Integer annotationTest2;
    }
}
