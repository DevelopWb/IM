package com.juntai.wisdom.im.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


import com.juntai.wisdom.im.bean.PhoneContactsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * Create by zhangzhenlong
 * 2021-3-15
 * email:954101549@qq.com
 */
public class ContactUtil {
    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

    //上下文对象
    private Context context;
    //联系人提供者的uri
    private Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    public ContactUtil(Context context){
        this.context = context;
    }

    //获取所有联系人
    public List<PhoneContactsBean> getAllContactInfo(){
        List<PhoneContactsBean> phoneDtos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri,new String[]{NUM,NAME},null,null,null);
        while (cursor.moveToNext()){
            PhoneContactsBean contactBean = new PhoneContactsBean(cursor.getString(cursor.getColumnIndex(NAME)),cursor.getString(cursor.getColumnIndex(NUM)).trim());
            contactBean.setPhone(contactBean.getPhone().replaceAll(" ", ""));
            if (!contactBean.getPhone().startsWith("0")){
                if (contactBean.getPhone().startsWith("+86")){
                    contactBean.setPhone(contactBean.getPhone().replaceFirst("\\+86", ""));
                }
            }
            phoneDtos.add(contactBean);
        }
        return phoneDtos;
    }
    

}
