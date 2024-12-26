package io.github.milkdrinkers.crate;

import io.github.milkdrinkers.crate.internal.exceptions.CrateValidationException;
import io.github.milkdrinkers.crate.internal.settings.DataType;
import org.junit.jupiter.api.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JsonTest {

    static Json json;

    @BeforeEach
    @Test
    void setUp() {
        json = new Json("Example", "");
        Assertions.assertEquals("Example.json", json.getName());
    }

    @Test
    void testGetDataType() {
        Assertions.assertEquals(json.getDataType(), DataType.UNSORTED);
    }

    @Test
    void testGetters() {
        Object anObject = json.get("Key"); // Default: null
        String aString = json.getString("Key"); // Default: ""
        int anInt = json.getInt("Key"); // Default: 0
        double aDouble = json.getDouble("Key"); // Default: 0.0
        float aFloat = json.getFloat("Key"); // Default: 0.0
        long aLong = json.getLong("Key"); // Default: 0.0

        Optional<String> optionalString = json.find(
            "Key",
            String.class); // If a key is not present an empty optional will be returned
        String getOrDefault = json.getOrDefault("Key", "Default-Value");
        String getOrSetDefault = json.getOrSetDefault(
            "Key",
            "Default-Value-To-Be-Set-If-Not-Yet-Present");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> json.getEnum("Key", TimeUnit.class));
        Assertions.assertThrows(
            CrateValidationException.class,
            () -> json.getEnum("Key-1", TimeUnit.class));

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
    void testSet() {
        json.set("Test-Key-1", true);
        Assertions.assertTrue(json.getData().containsKey("Test-Key-1"));
        Assertions.assertTrue(json.contains("Test-Key-1"));
        Assertions.assertTrue(json.getBoolean("Test-Key-1"));
    }

    @AfterAll
    static void tearDown() {
        json.clear();
        Assertions.assertTrue(json.getFile().delete());
    }
}
