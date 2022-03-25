package com.juntai.disabled.basecomponent.net.convert;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * File descripition:   对返回值为空处理
 * @aouther Ma
 * @date 2019/3/5
 */

public class DoubleNullAdapter extends TypeAdapter<Double> {
    @Override
    public Double read(JsonReader reader) throws IOException {
        //  Auto-generated method stub
        if (reader.peek() == JsonToken.STRING) {
            reader.skipValue(); //跳过当前
            return 0.00;
        }
        BigDecimal bigDecimal = new BigDecimal(reader.nextString());
        return bigDecimal.doubleValue();
    }

    @Override
    public void write(JsonWriter writer, Double value) throws IOException {
        //  Auto-generated method stub
        writer.value(value);
    }
}
