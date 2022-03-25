package com.juntai.disabled.basecomponent.net.convert;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * File descripition:long=>
 * @aouther Ma
 * @date 2019/3/5
 */

public class LongNullAdapter extends TypeAdapter<Long> {
    @Override
    public Long read(JsonReader reader) throws IOException {
        //  Auto-generated method stub
        if (reader.peek() == JsonToken.STRING) {
            reader.skipValue(); //跳过当前
            return 0l;
        }
        BigDecimal bigDecimal = new BigDecimal(reader.nextString());
        return bigDecimal.longValue();
    }

    @Override
    public void write(JsonWriter writer, Long value) throws IOException {
        //  Auto-generated method stub
        writer.value(value);
    }
}