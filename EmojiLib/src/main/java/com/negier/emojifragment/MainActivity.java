package com.negier.emojifragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.negier.emojifragment.bean.Emoji;
import com.negier.emojifragment.fragment.EmojiFragment;
import com.negier.emojifragment.util.EmojiUtils;

/**
 * 使用：
 * 照着我MainActivity的写就行了，其它可以原样复制。
 * 注意：
 *   activity_main.xml里的CirclePointIndicatorView，写成自己的包名路径。
 * 自定义的地方：
 *   表情GridView Item的大小，默认32dp，可在item_gridview_emoji.xml和EmojiGridViewAdapter.java里修改，建议同时修改。
 *   在textView显示的表情的大小，默认22dp.可在EmojiUtils里修改。
 *   表情默认3行7列，可在EmojiFragment里修改。
 *   添加表情，直接在EmojiUtils里对应位置添加。
 * 测试N遍，无BUG，欢迎阅读源码，欢迎封装。
 */

public class MainActivity extends AppCompatActivity implements EmojiFragment.OnEmojiClickListener{

    private EditText mEditText;
    private EmojiFragment emojiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.edit_text);

        emojiFragment = EmojiFragment.newInstance(EmojiFragment.BOTTOM);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl, emojiFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        int index = mEditText.getSelectionStart();
        Editable editableText = mEditText.getEditableText();
        editableText.insert(index,emoji.getName());
        displayEmoji(mEditText,index+emoji.getName().length());
    }

    @Override
    public void onEmojiDelete() {
        String content = mEditText.getText().toString();
        int index = mEditText.getSelectionStart();
        if (TextUtils.isEmpty(content)){
            return;
        }
        if ("]".equals(content.substring(index-1,index))){
            int lastIndexOf = content.lastIndexOf("[",index-1);
            if (lastIndexOf == -1) {
                onKeyDownDelete(mEditText);
            } else {
                mEditText.getText().delete(lastIndexOf,index);
                displayEmoji(mEditText,lastIndexOf);
            }
            return;
        }
        onKeyDownDelete(mEditText);
    }
    public void onKeyDownDelete(EditText editText){
        int index = mEditText.getSelectionStart();
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
        editText.onKeyDown(KeyEvent.KEYCODE_DEL,keyEvent);
        displayEmoji(mEditText,index-1);
    }
    public void displayEmoji(TextView textView,int indexSelection){
        EmojiUtils.showEmojiTextView(this,textView,textView.getText().toString(),18);
        if (textView instanceof EditText) {
            ((EditText) textView).setSelection(indexSelection);
        }
    }

}
