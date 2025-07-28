package io.github.milkdrinkers.crate;

import io.github.milkdrinkers.crate.internal.exceptions.CrateValidationException;
import io.github.milkdrinkers.crate.internal.settings.DataType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class YamlTest {
    static Yaml yaml;
    @TempDir
    File tempDir;

    @BeforeEach
    @Test
    void setUp() {
        yaml = Yaml.builder().path(tempDir.getPath(), "Example.yml").build();
        Assertions.assertEquals("Example.yml", yaml.getName());
        Assertions.assertEquals(new ArrayList<>(), yaml.getHeader());
    }

    @AfterEach
    void tearDown() {
        yaml.clear();
        Assertions.assertTrue(yaml.getFile().delete());
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

    @Test
    void testDefaults() {
        // Test 1: Resource in root of resources folder
        Yaml yamlWithDefaults1 = Yaml.builder()
            .path(tempDir.getPath(), "ExtractTest1.yml")
            .defaults("testextract1.yml")
            .build();

        // Verify the file was created and populated with default data
        Assertions.assertTrue(yamlWithDefaults1.getFile().exists(),
            "Configuration file should be created when defaults are applied");

        // Verify specific content from the defaults file
        Assertions.assertTrue(yamlWithDefaults1.contains("app.name"));
        Assertions.assertEquals("Test Application", yamlWithDefaults1.getString("app.name"));
        Assertions.assertTrue(yamlWithDefaults1.getBoolean("app.debug"));
        Assertions.assertEquals(5432, yamlWithDefaults1.getInt("database.port"));
        Assertions.assertEquals(30, yamlWithDefaults1.getInt("settings.timeout"));

        // Test that the defaults were actually loaded (file should not be empty)
        Assertions.assertFalse(yamlWithDefaults1.getData().isEmpty(),
            "Configuration should contain data from defaults resource");

        // Test 2: Resource in nested folder
        Yaml yamlWithDefaults2 = Yaml.builder()
            .path(tempDir.getPath(), "ExtractTest2.yml")
            .defaults("nested/testextract2.yml")
            .build();

        // Verify the file was created and populated with default data
        Assertions.assertTrue(yamlWithDefaults2.getFile().exists(),
            "Configuration file should be created when defaults are applied");

        // Verify specific content from the nested defaults file
        Assertions.assertTrue(yamlWithDefaults2.contains("app.name"));
        Assertions.assertEquals("Test Application", yamlWithDefaults2.getString("app.name"));
        Assertions.assertTrue(yamlWithDefaults2.getBoolean("app.debug"));
        Assertions.assertEquals(5432, yamlWithDefaults2.getInt("database.port"));

        // Verify both configurations loaded identical data
        Assertions.assertEquals(yamlWithDefaults1.getString("app.name"), yamlWithDefaults2.getString("app.name"));
        Assertions.assertEquals(yamlWithDefaults1.getInt("database.port"), yamlWithDefaults2.getInt("database.port"));

        // Clean up
        yamlWithDefaults1.clear();
        yamlWithDefaults1.getFile().delete();
        yamlWithDefaults2.clear();
        yamlWithDefaults2.getFile().delete();

        // Test 3: Verify exception is thrown for non-existent resource
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Yaml.builder()
                .path(tempDir.getPath(), "NonExistentDefaultsTest.yml")
                .defaults("non-existent-resource.yml")
                .build();
        }, "Should throw exception when default resource doesn't exist");
    }
}
