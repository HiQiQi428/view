package org.luncert.view.util;

public class JSONBuilder {

    StringBuilder builder;

    boolean empty = true;

    public JSONBuilder() {
        builder = new StringBuilder();
        builder.append("{");
    }

    public JSONBuilder put(String key, Object value) {
        putKey(key);
        if (value instanceof String)
            builder.append('"').append(value).append('"');
        else
            builder.append(value);
        return this;
    }

    private void putKey(String key) {
        if (empty) {
            empty = false;
        }
        else {
            builder.append(',');
        }
        builder.append('"').append(key).append("\":");
    }

    @Override
    public String toString() {
        return builder.append('}').toString();
    }

}