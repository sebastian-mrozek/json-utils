package io.sebastianmrozek.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


public class JsonUtilsTest {

  @Test
  public void assertContains_itself() {
    JsonNode original = Utils.readNodeFromResource("/contains/original.json");
    JsonUtils.assertContains(original, original);
  }


  @Test
  public void assertContains_subset() {
    JsonNode original = Utils.readNodeFromResource("/contains/original.json");
    JsonNode expected = Utils.readNodeFromResource("/contains/original-subset.json");
    JsonUtils.assertContains(original, expected);
  }


  @Test
  public void testContainsFails() {
    JsonNode original = Utils.readNodeFromResource("/contains/original.json");
    JsonNode expected = Utils.readNodeFromResource("/contains/original-subset-modified.json");
    try {
      JsonUtils.assertContains(original, expected);
    } catch (AssertionError e) {
      String exceptionMessage = e.getMessage();
      System.out.println(exceptionMessage);
      Stream.of("Expected field 'someString1' to be equal to '\"aaaa\"' but was '\"string1\"",
        "Expected field 'someValue1' to be equal to '99' but was '1'",
        "Unable to match expected element 'someArray1[0]' in the actual array",
        "Expected field 'someObject1.value1' to be of type 'ARRAY' but was 'NUMBER'",
        "Expected field 'someObject1.value2' to be of type 'OBJECT' but was 'STRING'",
        "Unable to match expected element 'someObject1.array1[0]' in the actual array",
        "Expected field 'someObject1.object1.val5' to be present",
        "Expected field 'someObject1.object1.val6' to be present",
        "Expected field 'someObject1.object2' to be of type 'NULL' but was 'OBJECT'",
        "Expected field 'someObject1.objectNull' to be of type 'OBJECT' but was 'NULL'")
        .forEach(assertionError -> assertThat(exceptionMessage).contains(assertionError));
      return;
    }
    Assertions.fail("Expected an exception to be thrown");
  }


  @Test
  public void assertContains_checkNull() {
    JsonNode original = Utils.readNodeFromResource("/contains/check-null-actual.json");
    JsonNode expected = Utils.readNodeFromResource("/contains/check-null-expected.json");
    try {
      JsonUtils.assertContains(original, expected);
    } catch (AssertionError e) {
      String exceptionMessage = e.getMessage();
      Stream.of("Expected field 'someNull' to be of type 'NULL' but was 'STRING'",
        "Expected field 'extra' to be present")
        .forEach(assertionError -> assertThat(exceptionMessage).contains(assertionError));
      return;
    }
    Assertions.fail("Expected an exception to be thrown");
  }

  @Test
  public void assertContains_checkType() {
    JsonNode original = Utils.readNodeFromResource("/contains/check-type-actual.json");
    JsonNode expected = Utils.readNodeFromResource("/contains/check-type-expected.json");
    try {
      JsonUtils.assertContains(original, expected);
    } catch (AssertionError e) {
      String exceptionMessage = e.getMessage();
      Stream.of("Expected field 'some' to be of type 'NUMBER' but was 'STRING'")
        .forEach(assertionError -> assertThat(exceptionMessage).contains(assertionError));
      return;
    }
    Assertions.fail("Expected an exception to be thrown");
  }

  @Test
  public void path_when_empty() {
    JsonUtils contains = new JsonUtils();

    assertThat(contains.path()).isEqualTo("");
    assertThat(contains.path("a")).isEqualTo("a");
    assertThat(contains.path("b")).isEqualTo("b");
  }


  @Test
  public void assertContainsNumbersArrayShuffled() {
    JsonNode array = Utils.readNodeFromString("[2, 54, 13, 10]");
    JsonNode arrayShuffled = Utils.readNodeFromString("[2, 13, 10, 54]");

    JsonUtils.assertContains(arrayShuffled, array);
  }

  @Test
  public void assertContainsObjectsArrayShuffled() {
    JsonNode array = Utils.readNodeFromResource("/contains/array-objects.json");
    JsonNode arrayShuffled = Utils.readNodeFromResource("/contains/array-objects-shuffled.json");

    JsonUtils.assertContains(arrayShuffled, array);
  }

  @Test
  public void assertArrayElementsNotFound() {
    JsonNode original = Utils.readNodeFromResource("/contains/array-multi-match.json");
    JsonNode actual = Utils.readNodeFromResource("/contains/array-multi-match-duplicate-props.json");

    try {
      JsonUtils.assertContains(actual, original);
    } catch (AssertionError e) {
      String exceptionMessage = e.getMessage();
      Stream.of("Unable to match expected element '[5]' in the actual array",
          "Unable to match expected element '[4]' in the actual array")
          .forEach(assertionError -> assertThat(exceptionMessage).contains(assertionError));
      return;
    }

    Assertions.fail("Expected an exception to be thrown");
  }
}
