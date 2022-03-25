package com.juntai.disabled.basecomponent.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * @aouther Ma
 * @date 2019/3/23
 */
public class GsonTools {

    public GsonTools() {

    }

    public static Gson buildGson() {
        return new Gson();
    }
    public static String createGsonString(Object object) {
        Gson gson = buildGson();
        String gsonString = gson.toJson(object);
        return gsonString;
    }

    public static <T> T changeGsonToBean(String gsonString, Class<T> cls) {
        try {
            Gson gson = buildGson();
            T t = gson.fromJson(gsonString, cls);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//
//    public static <T> List<T> changeGsonToList(String gsonString, Class<T> cls) {
//        Gson gson = new GsonBuilder()
//                /**
//                 * 重写map的反序列化
//                 */
//                .registerTypeAdapter(new TypeToken<List<T>>() {
//                }.getType(), new MapTypeAdapter()).create();
//        List<T> list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
//        }.getType());
//        return list;
//    }

    /**
     * 这种好使  直接指定class
     * @param gsonString
     * @param cls
     * @param <T>
     * @return
     */
//    public List<MessageBodyBean> changeGsonToList(String gsonString) {
//        Gson gson = new Gson();
//        List<MessageBodyBean> list = gson.fromJson(gsonString, new TypeToken<List<MessageBodyBean>>() {
//        }.getType());
//        return list;
//    }
//    public static <T> List<T> changeGsonToList(String gsonString,Class<T> cls) {
//        Gson gson = new Gson();
//        List<T> list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
//        }.getType());
//        return list;
//    }
    public static <T> List<Map<String, T>> changeGsonToListMaps(
            String gsonString) {
        List<Map<String, T>> list = null;
        Gson gson = buildGson();
        list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }

    public static <T> Map<String, T> changeGsonToMaps(String gsonString) {
        Map<String, T> map = null;
        Gson gson = buildGson();
        map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
        return map;
    }

    /**
     * 将ModelA的属性值赋值给modleB
     *
     * @param modelA
     * @param bClass
     * @param <T>
     * @return
     */
    public static <T> T modelA2B(Object modelA, Class<T> bClass) {
        try {
            Gson gson = buildGson();
            String gsonA = gson.toJson(modelA);
            T instanceB = gson.fromJson(gsonA, bClass);
            return instanceB;
        } catch (Exception e) {
            return null;
        }
    }


    //通过Gson将Bean转化为Json字符串形式
    public static RequestBody getJsonRequest(Object object) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), createGsonString(object));
    }
    public static class MapTypeAdapter extends TypeAdapter<Object> {

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();

                case NUMBER:
                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    double dbNum = in.nextDouble();

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }

                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        return lngNum;
                    } else {
                        return dbNum;
                    }

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            // 序列化无需实现
        }

    }
}