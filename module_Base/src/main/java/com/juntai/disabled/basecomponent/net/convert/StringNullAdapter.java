package com.juntai.disabled.basecomponent.net.convert;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * string空数据null处理
 * @aouther Ma
 * @date 2019/3/5
 */
public class StringNullAdapter extends TypeAdapter<String> {
    @Override
    public String read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return "";//原先是返回Null，这里改为返回空字符串
        }

        String jsonStr = reader.nextString();
        if(jsonStr == null) {
            return "";
        }else {
            return jsonStr;
        }
    }

    @Override
    public void write(JsonWriter writer, String value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }
}