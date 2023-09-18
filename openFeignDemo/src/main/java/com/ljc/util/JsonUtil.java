package com.ljc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.util.List;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {

    }

    public static JsonNode batchProcessJsonValueByPath(Object target, List<String> paths, JsonValueProcessor processer) {
        if (paths == null || target == null || processer == null) {
            return null;
        }
        JsonNode jsonNode = toJsonNode(toJsonString(target));
        paths.forEach(path -> processJsonValueByPath(path, path.split("\\."), 0, jsonNode, processer));
        return jsonNode;
    }

    private static void processJsonValueByPath(String path, String[] pathNodes, int idx, JsonNode parent, JsonValueProcessor processer) {
        if (parent instanceof ArrayNode) {
            ((ArrayNode) parent).iterator().forEachRemaining(e -> processJsonValueByPath(path, pathNodes, idx, e, processer));
            return;
        }
        JsonNode jsonNode = parent.get(pathNodes[idx]);
        if (jsonNode == null) {
            return;
        }
        if (jsonNode instanceof ValueNode) {
            processer.process(((ValueNode) jsonNode), parent, path, pathNodes[idx]);
        } else {
            processJsonValueByPath(path, pathNodes, idx + 1, jsonNode, processer);
        }
    }

    @FunctionalInterface
    public interface JsonValueProcessor {
        void process(ValueNode valueNode, JsonNode parent, String path, String currentPathNode);
    }

    public static String toJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("toJsonString failed.");
        }
    }

    public static JsonNode toJsonNode(String jsonStr) {
        try {
            return mapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("toJsonNode failed.");
        }
    }

    public static Object toObject(String jsonStr, Class<?> toObjectType) {
        try {
            return mapper.readValue(jsonStr, toObjectType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
