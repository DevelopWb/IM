package com.juntai.disabled.basecomponent.net.convert;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * File descripition:double=>
 * @aouther Ma
 * @date 2019/3/5
 */

public class IntegerNullAdapter extends TypeAdapter<Integer> {
    @Override
    public Integer read(JsonReader reader) throws IOException {
        //  Auto-generated method stub
        if (reader.peek() == JsonToken.STRING) {
            reader.skipValue(); //跳过当前
            return 0;
        }
        BigDecimal bigDecimal = new BigDecimal(reader.nextString());
        return bigDecimal.intValue();
    }

    @Override
    public void write(JsonWriter writer, Integer value) throws IOException {
        //  Auto-generated method stub
        writer.value(value);
    }
}