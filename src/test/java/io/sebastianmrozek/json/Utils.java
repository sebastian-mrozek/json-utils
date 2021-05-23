package io.sebastianmrozek.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

    public static final ObjectMapper MAPPER = initMapper();

    public static ObjectMapper initMapper() {
        return new ObjectMapper();
    }

    public static JsonNode readNodeFromResource(String resourcePath) {
        URL resourceUrl = Utils.class.getResource(resourcePath);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource path cannot be null");
        }

        try {
            String resourceContentString = Files.readString(Paths.get(resourceUrl.toURI()));
            return readNodeFromString(resourceContentString);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to read json string from a resource: " + resourcePath, e);
        }
    }

    public static JsonNode readNodeFromString(String resourceContentString) {
        try {
            return MAPPER.readTree(resourceContentString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse json from a string", e);
        }
    }

}
