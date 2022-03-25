package com.negier.emojifragment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.negier.emojifragment.R;
import com.negier.emojifragment.bean.Emoji;
import com.negier.emojifragment.util.EmojiUtils;
import com.negier.emojifragment.util.PxUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/31 0031.
 */

public class EmojiGridViewAdapter extends BaseAdapter {
    private final Context context;
    private final List<Emoji> emojis;

    public EmojiGridViewAdapter(Context context, List<Emoji> emojis) {
        this.context=context;
        this.emojis=emojis;
    }

    @Override
    public int getCount() {
        return emojis.size();
    }

    @Override
    public Object getItem(int position) {
        return emojis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_gridview_emoji, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        if (emojis.get(position) != null) {
            imageView.setImageBitmap(EmojiUtils.getEmojiItem(context.getResources(), emojis.get(position).getImageUri(), PxUtils.dpToPx(context, 32), PxUtils.dpToPx(context, 32)));
        }
        return view;
    }
}