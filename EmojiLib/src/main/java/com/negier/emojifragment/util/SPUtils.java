package com.negier.emojifragment.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;


/**
 * collection 必须要implements Serializable，因为ObjectOutputStream的缘故
 */

public class SPUtils {
    public static final String SP_NAME="needYourName";//SharedPreferences的名字

    public static void setSPCollection(Context context, Collection collection, String collectionName) throws IOException {
        SharedPreferences.Editor edit=getSharedPreferences(context, SP_NAME).edit();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(collection);
        String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));//利用Base64防止乱码
        edit.putString(collectionName,string);
        edit.apply();
        objectOutputStream.close();
    }
    public static Collection getSPCollection(Context context, String collectionName) throws IOException, ClassNotFoundException {
        String string = getSharedPreferences(context, SP_NAME).getString(collectionName, "");
        if (TextUtils.isEmpty(string)||TextUtils.isEmpty(string.trim())){
            return null;
        }
        byte[] decodeBytes = Base64.decode(string.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodeBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Collection collection = (Collection) objectInputStream.readObject();
        objectInputStream.close();
        return collection;
    }
    public static SharedPreferences getSharedPreferences(Context context,String spName){
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }
}
