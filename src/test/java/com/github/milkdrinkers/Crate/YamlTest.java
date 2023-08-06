package com.github.milkdrinkers.Crate;

import com.github.milkdrinkers.Crate.internal.exceptions.CrateValidationException;
import com.github.milkdrinkers.Crate.internal.settings.DataType;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class YamlTest {


    static Yaml yaml;

    @BeforeEach
    @Test
    void setUp() {
        yaml = new Yaml("Example", "");
        Assertions.assertEquals("Example.yml", yaml.getName());
        Assertions.assertEquals(new ArrayList<>(), yaml.getHeader());
    }

    @Test
    void testGetDataType() {
        Assertions.assertEquals(DataType.UNSORTED, yaml.getDataType());
    }

    @Test
    void testGetters() {
        Object anObject = yaml.get("Key"); // Default: null
        String aString = yaml.getString("Key"); // Default: ""
        int anInt = yaml.getInt("Key"); // Default: 0
        double aDouble = yaml.getDouble("Key"); // Default: 0.0
        float aFloat = yaml.getFloat("Key"); // Default: 0.0
        long aLong = yaml.getLong("Key"); // Default: 0.0

        Optional<String> optionalString = yaml.find(
            "Key",
            String.class); // If a key is not present an empty optional will be returned
        String getOrDefault = yaml.getOrDefault("Key", "Default-Value");
        String getOrSetDefault = yaml.getOrSetDefault(
            "Key",
            "Default-Value-To-Be-Set-If-Not-Yet-Present");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> yaml.getEnum("Key", TimeUnit.class));
        Assertions.assertThrows(
            CrateValidationException.class,
            () -> yaml.getEnum("Key-1", TimeUnit.class));

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
        yaml.setHeader("Example-1", "Example-2");
        Assertions.assertEquals(Arrays.asList("#Example-1", "#Example-2"), yaml.getHeader());
    }

    @Test
    void testSet() {
        yaml.set("Test-Key-1", true);
        Assertions.assertTrue(yaml.getData().containsKey("Test-Key-1"));
        Assertions.assertTrue(yaml.contains("Test-Key-1"));
        Assertions.assertTrue(yaml.getBoolean("Test-Key-1"));
    }

    @AfterAll
    @Test
    void tearDown() {
        yaml.clear();
        Assertions.assertTrue(yaml.getFile().delete());
    }
}
