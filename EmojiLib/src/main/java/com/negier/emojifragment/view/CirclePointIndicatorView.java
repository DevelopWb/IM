package com.negier.emojifragment.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.negier.emojifragment.R;
import com.negier.emojifragment.util.PxUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/29 0029.
 */

public class CirclePointIndicatorView extends LinearLayout {
    private final Context context;
    private final int mHeight = 16;
    private int mMaxHeight;
    private Bitmap mNormalBitmap;
    private Bitmap mSelectBitmap;
    private ArrayList<ImageView> mImageViews;
    private AnimatorSet mAnimatorSet;
    private AnimatorSet mLastImageAnimSet;
    private AnimatorSet mNextImageAnimSet;

    public CirclePointIndicatorView(Context context) {
        this(context, null);
    }

    public CirclePointIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePointIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mMaxHeight = PxUtils.dpToPx(context, mHeight);
        mNormalBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.indicator_point_nomal);
        mSelectBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.indicator_point_select);
    }

    public void init(int count) {
        this.removeAllViews();
        mImageViews = new ArrayList<>();
        LayoutParams layoutParams = new LayoutParams(mMaxHeight, mMaxHeight);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            if (i == 0) {
                imageView.setImageBitmap(mSelectBitmap);
            } else {
                imageView.setImageBitmap(mNormalBitmap);
            }
            RelativeLayout relativeLayout = new RelativeLayout(context);
            relativeLayout.addView(imageView, relativeParams);
            this.addView(relativeLayout, layoutParams);
            mImageViews.add(imageView);
        }
    }

    public void playTo(int position) {
        for (ImageView imageView : mImageViews) {
            imageView.setImageBitmap(mNormalBitmap);
        }
        ImageView selectImageView = mImageViews.get(position);
        selectImageView.setImageBitmap(mSelectBitmap);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(selectImageView, "scaleX", 0.25f, 1f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(selectImageView, "scaleY", 0.25f, 1f);
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
            mAnimatorSet=null;
        }
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(anim1).with(anim2);
        mAnimatorSet.setDuration(100);
        mAnimatorSet.start();
    }
    public void playBy(int lastPosition,int nextPosition){
        if (lastPosition<0||nextPosition<0){
            nextPosition=lastPosition=0;
        }
        for (ImageView imageView : mImageViews) {
            imageView.setImageBitmap(mNormalBitmap);
        }
        final ImageView lastImageView = mImageViews.get(lastPosition);
        ImageView nextImageView = mImageViews.get(nextPosition);
        nextImageView.setImageBitmap(mSelectBitmap);
        ObjectAnimator nextImageAnim1 = ObjectAnimator.ofFloat(nextImageView, "scaleX",0.25f,1.0f);
        ObjectAnimator nextImageAnim2 = ObjectAnimator.ofFloat(nextImageView, "scaleY",0.25f,1.0f);
        if (mNextImageAnimSet!=null&&mNextImageAnimSet.isRunning()){
            mNextImageAnimSet.cancel();
            mNextImageAnimSet=null;
        }
        mNextImageAnimSet = new AnimatorSet();
        mNextImageAnimSet.play(nextImageAnim1).with(nextImageAnim2);
        mNextImageAnimSet.setDuration(300);
        if (lastPosition==nextPosition){
            mNextImageAnimSet.start();//相等，就只播放一个动画。
            return;
        }
        ObjectAnimator lastImageAnim1 = ObjectAnimator.ofFloat(lastImageView, "scaleX", 1.0f, 0.25f);
        ObjectAnimator lastImageAnim2 = ObjectAnimator.ofFloat(lastImageView, "scaleY", 1.0f, 0.25f);
        if (mLastImageAnimSet!=null&&mLastImageAnimSet.isRunning()){
            mLastImageAnimSet.cancel();
            mLastImageAnimSet=null;
        }
        mLastImageAnimSet = new AnimatorSet();
        mLastImageAnimSet.play(lastImageAnim1).with(lastImageAnim2);
        mLastImageAnimSet.setDuration(100);
        mLastImageAnimSet.start();
        mLastImageAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator lastImageAnim1 = ObjectAnimator.ofFloat(lastImageView, "scaleX",1.0f);
                ObjectAnimator lastImageAnim2 = ObjectAnimator.ofFloat(lastImageView, "scaleY",1.0f);
                AnimatorSet lastImageAnimSet = new AnimatorSet();
                lastImageAnimSet.play(lastImageAnim1).with(lastImageAnim2);
                lastImageAnimSet.setDuration(100);
                lastImageAnimSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mNextImageAnimSet.start();
    }
}
