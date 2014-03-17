package com.android.flash.sync;

import java.lang.reflect.Type;

import com.android.flash.SibOne;
import com.android.flash.SibTwo;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * User: johnwright
 * Date: 1/30/14
 * Time: 10:46 PM
 */
public class SibOneJsonSerializer implements JsonSerializer<SibOne>, JsonDeserializer<SibOne> {
    @Override
    public JsonElement serialize(SibOne sibOne, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sibone", sibOne.getName());
        jsonObject.addProperty("sibtwo", sibOne.getPair().getName());
        return jsonObject;
    }

    @Override
    public SibOne deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) json;
        final int serverId = jsonObject.get("id").getAsInt();
        final int version = jsonObject.get("version").getAsInt();
        final String siboneName = jsonObject.get("sibone").getAsString();
        final String sibtwoName = jsonObject.get("sibtwo").getAsString();
        final SibOne sibOne = new SibOne(siboneName, new SibTwo(sibtwoName), 0, serverId);
        sibOne.setVersion(version);
        return sibOne;
    }
}
