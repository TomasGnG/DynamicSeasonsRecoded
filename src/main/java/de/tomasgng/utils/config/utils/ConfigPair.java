package de.tomasgng.utils.config.utils;

import java.util.List;

public class ConfigPair {
    private final String path;
    private final Object value;
    private List<String> comments;

    public ConfigPair(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public ConfigPair(String path) {
        this.path = path;
        this.value = null;
    }

    public ConfigPair(String path, Object value, String... comments) {
        this.path = path;
        this.value = value;
        this.comments = List.of(comments);
    }

    public String getPath() {
        return path;
    }

    public Object getValue() {
        return value;
    }

    public String getStringValue() {
        return String.valueOf(value);
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(getStringValue());
    }

    public int getIntegerValue() {
        return Integer.parseInt(getStringValue());
    }

    public double getDoubleValue() {
        return Double.parseDouble(getStringValue());
    }

    public List<String> getStringListValue() {
        return (List<String>) value;
    }

    public List<String> getComments() {
        return comments;
    }

    public boolean hasComments() {
        return comments != null;
    }
}