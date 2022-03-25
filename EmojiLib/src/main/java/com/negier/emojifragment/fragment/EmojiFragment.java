package com.negier.emojifragment.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.negier.emojifragment.R;
import com.negier.emojifragment.adapter.EmojiGridViewAdapter;
import com.negier.emojifragment.adapter.EmojiViewPagerAdapter;
import com.negier.emojifragment.bean.Emoji;
import com.negier.emojifragment.util.EmojiUtils;
import com.negier.emojifragment.util.SPUtils;
import com.negier.emojifragment.view.CirclePointIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/1/29 0029.
 */

public class EmojiFragment extends Fragment implements View.OnClickListener{
    private CirclePointIndicatorView mCirclePointIndicatorView;
    private ViewPager mVPEmoji;
    private int mRows=4;//行
    private int mColumns=7;//列
    private int mLastPosition;
    private ArrayList<Emoji> mRecentEmojiLists;
    private final String RECENT_EMOJI="rencentEmoji";
    private TextView mTVRecent;
    private TextView mTVDefault;
    public static final int TOP=1;//界面点击按钮【最近、默认】在表情上方
    public static final int BOTTOM=2;//界面点击按钮【最近、默认】在表情下方
    private ArrayList<Emoji> mEmojiLists;
    ArrayList<GridView> gridViewLists = new ArrayList<>();

    public static EmojiFragment newInstance(int emojiOrientation){
        EmojiFragment emojiFragment = new EmojiFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("emojiOrientation",emojiOrientation);
        emojiFragment.setArguments(bundle);
        return emojiFragment;
    }

    /**
     * 如果其实现了这个OnEmojiClickListener
     * 那么其不用setOnEmojiClickListener也可以。
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        if (context instanceof OnEmojiClickListener){
            this.listener = (OnEmojiClickListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mEmojiLists = EmojiUtils.getEmojiLists();
        try {
            if (SPUtils.getSPCollection(getActivity(),RECENT_EMOJI)!=null){
                mRecentEmojiLists=(ArrayList<Emoji>) SPUtils.getSPCollection(getActivity(),RECENT_EMOJI);
            }else{
                mRecentEmojiLists = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
//        int emojiOrientation = bundle.getInt("emojiOrientation");
        View view;
        view=inflater.inflate(R.layout.fragment_emoji_top, null);
//        if (emojiOrientation!=0&&emojiOrientation==TOP){
//            view=inflater.inflate(R.layout.fragment_emoji_top, null);
//        }else{
//            view=inflater.inflate(R.layout.fragment_emoji_bottom, null);
//        }
        mCirclePointIndicatorView = (CirclePointIndicatorView) view.findViewById(R.id.circle_point_indicator_view);
        mVPEmoji = (ViewPager) view.findViewById(R.id.vp_emoji);
        mTVRecent = (TextView) view.findViewById(R.id.tv_recent);
        mTVDefault = (TextView) view.findViewById(R.id.tv_default);
        initViews();
        return view;
    }

    @Override
    public void onDestroyView() {
        try {
            SPUtils.setSPCollection(getActivity(),mRecentEmojiLists,RECENT_EMOJI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroyView();
    }

    private void initViews() {
        mTVRecent.setOnClickListener(this);
        mTVDefault.setOnClickListener(this);
        mTVDefault.setSelected(true);
        initViewPager(mEmojiLists);
    }

    private void initViewPager(ArrayList<Emoji> emojiLists) {
        gridViewLists.clear();
        int viewPagerCount = getViewPagerCount(emojiLists);
        mCirclePointIndicatorView.init(viewPagerCount);
        for (int i = 0; i < viewPagerCount; i++) {
            GridView gridView = getViewPagerItem(i, emojiLists);
            gridViewLists.add(gridView);
        }
        mVPEmoji.setAdapter(new EmojiViewPagerAdapter(getActivity(),gridViewLists));
        mVPEmoji.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCirclePointIndicatorView.playBy(mLastPosition,position);
                mLastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getViewPagerCount(ArrayList<Emoji> emojiLists){
        int count = emojiLists.size();
        return count%(mRows*mColumns-1)==0?count/(mRows*mColumns-1):count/(mRows*mColumns-1)+1;
    }

    private GridView getViewPagerItem(int position, ArrayList<Emoji> emojiLists){
        GridView gridView = (GridView)View.inflate(getActivity(), R.layout.gridview_emoji, null);
        final List<Emoji>  subEmojiLists = new ArrayList<>();
        subEmojiLists.addAll(emojiLists.subList(position * (mRows * mColumns - 1), (position + 1) * (mRows * mColumns - 1) > emojiLists.size() ? emojiLists.size() : (position + 1) * (mRows * mColumns - 1)));
        for (int i = subEmojiLists.size(); i < (mColumns*mRows-1); i++) {
            subEmojiLists.add(null);
        }
        Emoji emoji = new Emoji();
        emoji.setImageUri(R.mipmap.face_delete);
        subEmojiLists.add(emoji);
        gridView.setAdapter(new EmojiGridViewAdapter(getActivity(),subEmojiLists));
        gridView.setNumColumns(mColumns);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener!=null){
                    if (position==mRows*mColumns-1){
                        listener.onEmojiDelete();
                    }else {
                        if (subEmojiLists.get(position)!=null){
                            Emoji emoji = subEmojiLists.get(position);
                            listener.onEmojiClick(emoji);
                            insertRecentEmojiLists(emoji);
                        }
                    }
                }
            }
        });
        return gridView;
    }

    private void insertRecentEmojiLists(Emoji emoji){
        if (emoji!=null){
            if (mRecentEmojiLists.contains(emoji)){
                int index = mRecentEmojiLists.indexOf(emoji);
                Emoji oldEmoji = mRecentEmojiLists.get(0);
                mRecentEmojiLists.set(index,oldEmoji);
                mRecentEmojiLists.set(0,emoji);
                return;
            }
            if (mRecentEmojiLists.size()==mRows*mColumns-1){
                mRecentEmojiLists.remove(mRows*mColumns-2);
            }
            mRecentEmojiLists.add(0,emoji);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_recent) {
            if (!mTVRecent.isSelected()) {
                mTVRecent.setSelected(true);
                initViewPager(mRecentEmojiLists);
            }
            mTVDefault.setSelected(false);
        } else if (id == R.id.tv_default) {
            if (!mTVDefault.isSelected()) {
                mTVDefault.setSelected(true);
                initViewPager(mEmojiLists);
            }
            mTVRecent.setSelected(false);
        }
    }

    private OnEmojiClickListener listener;
    public void setOnEmojiClickListener(OnEmojiClickListener listener){
        this.listener=listener;
    }
    public interface OnEmojiClickListener{
        void onEmojiClick(Emoji emoji);
        void onEmojiDelete();
    }
}
