package org.luncert.view.util;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Result {

    private int statusCode;

    private String description;

    private Object data;

    public static final int OK = 0;

    // pig not found
    public static final int PIG_NOT_FOUND = 1;

    // invalid sid
    public static final int INVALID_SID = 2;

    // invalid strain identifier
    public static final int INVALID_STRAIN_IDENTIFIER = 3;

    // field is null
    public static final int FIELD_IS_NULL = 4;

    // exception occured
    public static final int EXCEPTOIN_OCCUR = 5;

    // found nothing
    public static final int FOUND_NOTHING = 6;

    public Result(int statusCode) { this(statusCode, null, null); }

    public Result(int statusCode, String description) { this(statusCode, description, null); }

    public Result(int statusCode, String description, Object data) {
        this.statusCode = statusCode;
        this.description = description;
        this.data = data;
    }

    public String toJSONString() {
        JSONObject json = new JSONObject();
        json.put("statusCode", statusCode);
        json.put("description", description);
        if (data instanceof Map) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.putAll((Map<?, ?>)data);
            json.put("data", jsonObj);
        }
        else if (data instanceof List) {
            JSONArray jsonArray = JSONArray.fromObject(data);
            json.put("data", jsonArray);
        }
        else json.put("data", data);
        return json.toString();
    }

}