package it.moondroid.seekbarhint.library;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SeekBarHint extends AppCompatSeekBar implements SeekBar.OnSeekBarChangeListener {

    public static final int POPUP_FIXED = 1;
    public static final int POPUP_FOLLOW = 0;
    private PopupWindow mPopup;
    private View mPopupView;
    private TextView mPopupTextView;
    private int mPopupLayout;
    private int mPopupWidth;
    private int mPopupOffset;
    private boolean mPopupAlwaysShown;
    private int mPopupStyle;
    private int mPopupAnimStyle;
    private String mThumbColor;
    private String mProgressColor;
    private OnSeekBarChangeListener mInternalListener;
    private OnSeekBarChangeListener mExternalListener;
    private SeekBarHintAdapter mHintAdapter;

    public SeekBarHint(Context context) {
        this(context, null);
    }

    public SeekBarHint(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarHintStyle);
    }

    public SeekBarHint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarHint, defStyleAttr, R.style.Widget_SeekBarHint);
        //
        mPopupLayout = a.getResourceId(R.styleable.SeekBarHint_popupLayout, R.layout.seekbar_hint_popup);
        mPopupWidth = (int) a.getDimension(R.styleable.SeekBarHint_popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupOffset = (int) a.getDimension(R.styleable.SeekBarHint_popupOffset, 0);
        mPopupStyle = a.getInt(R.styleable.SeekBarHint_popupStyle, POPUP_FOLLOW);
        mPopupAnimStyle = a.getResourceId(R.styleable.SeekBarHint_popupAnimationStyle, R.style.SeekBarHintPopupAnimation);
        mPopupAlwaysShown = a.getBoolean(R.styleable.SeekBarHint_popupAlwaysShown, false);
        mThumbColor = a.getString(R.styleable.SeekBarHint_thumbColor);
        mProgressColor = a.getString(R.styleable.SeekBarHint_progressColor);
        a.recycle();

        setOnSeekBarChangeListener(this);

        initHintPopup();
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        hidePopup();
//        showPopupOnPost();
//    }
//
//    @Override
//    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        hidePopup();
//        showPopupOnPost();
//    }

    private void initHintPopup() {
        String popupText = null;
        if (mHintAdapter != null) {
            popupText = mHintAdapter.getHint(this, getProgress());
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupView = inflater.inflate(mPopupLayout, null);
        mPopupView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mPopupTextView = (TextView) mPopupView.findViewById(android.R.id.text1);
        mPopupTextView.setText(popupText != null ? popupText : String.valueOf(getProgress()));

        mPopup = new PopupWindow(mPopupView, mPopupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        mPopup.setAnimationStyle(mPopupAnimStyle);
        if (mThumbColor != null && !mThumbColor.isEmpty()) {
            getThumb().setColorFilter(Color.parseColor(mThumbColor), PorterDuff.Mode.SRC_IN);
        }
        if (mProgressColor != null && !mProgressColor.isEmpty()) {
            getProgressDrawable().setColorFilter(Color.parseColor(mProgressColor), PorterDuff.Mode.SRC_IN);
        }
        if (mPopupAlwaysShown) showPopupOnPost();
    }

    private void showPopupOnPost() {
        post(new Runnable() {
            @Override
            public void run() {
                showPopup();
            }
        });
    }

    private void showPopup() {
        Point offsetPoint = null;
        switch (getPopupStyle()) {
            case POPUP_FOLLOW:
                offsetPoint = getFollowOffset();
                break;
            case POPUP_FIXED:
                offsetPoint = getFixedOffset();
                break;
        }
        mPopup.showAtLocation(this, Gravity.NO_GRAVITY, 0, 0);
        mPopup.update(this, offsetPoint.x, offsetPoint.y, -1, -1);
    }

    private void hidePopup() {
        if (mPopup.isShowing()) {
            mPopup.dismiss();
        }
    }

    @LayoutRes
    public int getPopupLayout() {
        return mPopupLayout;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public api
    ///////////////////////////////////////////////////////////////////////////

    public void setPopupLayout(@LayoutRes int layout) {
        this.mPopupLayout = layout;
        if (mPopup != null) mPopup.dismiss();
        initHintPopup();
    }

    @PopupStyle
    public int getPopupStyle() {
        return mPopupStyle;
    }

    public void setPopupStyle(@PopupStyle int style) {
        mPopupStyle = style;
        if (mPopupAlwaysShown) showPopupOnPost();
    }

    public boolean isPopupAlwaysShown() {
        return mPopupAlwaysShown;
    }

    public void setPopupAlwaysShown(boolean alwaysShown) {
        this.mPopupAlwaysShown = alwaysShown;
        if (alwaysShown) showPopupOnPost();
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        if (mInternalListener == null) {
            mInternalListener = l;
            super.setOnSeekBarChangeListener(l);
        } else {
            mExternalListener = l;
        }
    }

    public void setHintAdapter(SeekBarHintAdapter l) {
        mHintAdapter = l;
        if (mPopupTextView != null) {
            mPopupTextView.setText(mHintAdapter.getHint(this, getProgress()));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mExternalListener != null) {
            mExternalListener.onStartTrackingTouch(seekBar);
        }
        showPopup();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Progress tracking
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mExternalListener != null) {
            mExternalListener.onStopTrackingTouch(seekBar);
        }

        if (!mPopupAlwaysShown) hidePopup();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String popupText = null;
        if (mHintAdapter != null) {
            popupText = mHintAdapter.getHint(this, progress);
        }
        mPopupTextView.setText(popupText != null ? popupText : String.valueOf(progress));

        if (mPopupStyle == POPUP_FOLLOW) {
            Point offsetPoint = getFollowOffset();
            mPopup.update(this, offsetPoint.x, offsetPoint.y, -1, -1);
        }

        if (mExternalListener != null) {
            mExternalListener.onProgressChanged(seekBar, progress, fromUser);
        }
    }

    @NonNull
    private Point getFixedOffset() {
        Point point;
        switch ((int) getRotation() % 360) {
            case 0:
                point = getHorizontalOffset();
                point.x = getWidth() / 2 - mPopupView.getMeasuredWidth() / 2;
                break;
            case 90:
                point = getVerticalOffset();
                point.y = getWidth() / 2 - mPopupView.getMeasuredHeight() / 2 - getPaddingLeft() - getPaddingRight();
                break;
            default:
                throw new IllegalArgumentException("Rotation " + String.valueOf(getRotation()) +
                        "is not supported, use 0 or 90");
        }
        return point;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Offset computation
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    private Point getFollowOffset() {
        Point point;
        switch ((int) getRotation() % 360) {
            case 0:
                point = getHorizontalOffset();
                break;
            case 90:
                point = getVerticalOffset();
                break;
            default:
                throw new IllegalArgumentException("Rotation " + String.valueOf(getRotation()) +
                        "is not supported, use 0 or 90");
        }
        return point;
    }

    @NonNull
    private Point getHorizontalOffset() {
        int xOffset = getFollowPosition() - mPopupView.getMeasuredWidth() / 2 + getHeight() / 2;
        int yOffset = -(getHeight() + mPopupView.getMeasuredHeight() + mPopupOffset);
        return new Point(xOffset, yOffset);
    }

    @NonNull
    private Point getVerticalOffset() {
        int xOffset = (-getHeight() / 2 + getThumbOffset()) + mPopupOffset;
        //
        int yOddOffset = -(getPaddingLeft() + getHeight() / 2 + getThumbOffset() / 3 * 2);
        int yOffset = getFollowPosition() + yOddOffset;
        return new Point(xOffset, yOffset);
    }

    private int getFollowPosition() {
        return (int) (getProgress() * (getWidth() - getPaddingStart() - getPaddingEnd()) / (float) getMax());
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POPUP_FIXED, POPUP_FOLLOW})
    public @interface PopupStyle {
    }

    ///////////////////////////////////////////////////////////////////////////
    // Hint interfaces
    ///////////////////////////////////////////////////////////////////////////

    public interface SeekBarHintAdapter {
        String getHint(SeekBarHint seekBarHint, int progress);
    }

}
