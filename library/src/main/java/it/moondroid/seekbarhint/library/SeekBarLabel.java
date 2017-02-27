package it.moondroid.seekbarhint.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by TCIG_PC_54 on 1/14/2017.
 */

public class SeekBarLabel extends RelativeLayout {
    //    private RelativeLayout seekLayout;
    private View seekBar;
    private TextView tvSeekLabel;
    private CardView seekCard;
    private float dX, dY;
    private int step, value = 0, min = 1, max = 17;
    private OnProgressListener onProgressListener;
    private int seekLayoutResId;

    public SeekBarLabel(Context context) {
        this(context, null);
    }

    public SeekBarLabel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SeekBarLabel(Context context, AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(final Context context, AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode()) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarLabel, defStyleAttr, 0);

        seekLayoutResId = a.getResourceId(R.styleable.SeekBarLabel_seekLayout, R.layout.view_seekbar_label);
        min = a.getInteger(R.styleable.SeekBarLabel_seekMin, 0);
        max = a.getInt(R.styleable.SeekBarLabel_seekMax, 100);


        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout seekLayout = (RelativeLayout) layoutInflater.inflate(seekLayoutResId, this);
        seekBar = seekLayout.findViewById(R.id.viewSeekBar);
        seekCard = (CardView) seekLayout.findViewById(R.id.seekCard);
        tvSeekLabel = (TextView) seekLayout.findViewById(R.id.tvSeek);
        tvSeekLabel.setText(String.valueOf(value + 1));

        seekCard.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, final MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        updateProgress(view, event);
                    case MotionEvent.ACTION_MOVE:
                        updateProgress(view, event);
                    case MotionEvent.ACTION_UP:
                        updateProgress(view, event);
                        break;

                }
                return true;
            }
        });

    }

    private void updateProgress(View view, MotionEvent event) {
        if (seekCard.getX() >= seekBar.getX())
            view.animate()
                    .x(event.getRawX() + dX)
                    .setDuration(0)
                    .start();
        if (seekCard.getX() < seekBar.getX())
            view.animate()
                    .x(0)
                    .setDuration(0)
                    .start();
        if (seekCard.getX() + seekCard.getWidth() > seekBar.getX() + seekBar.getWidth())
            view.animate()
                    .x(seekBar.getX() + seekBar.getWidth() - seekCard.getWidth())
                    .setDuration(0)
                    .start();
        if (step == 0)
            step = seekBar.getWidth() / max;
        int value = (int) (max -
                ((seekBar.getWidth() - event.getRawX()) / step));
        if (value < min)
            value = min;
        if (value > max)
            value = max;

//        String timeText = String.valueOf(value);
//        if (value > 9) {
//            if (value % 2 == 0)
//                timeText = "" + (value / 2) + ":00";
//            else
//                timeText = "" + (value / 2) + ":30";
//        } else {
//            if (value % 2 == 0)
//                timeText = "" + (value / 2) + ":00";
//            else
//                timeText = "" + (value / 2) + ":30";
//        }
        if (onProgressListener != null)
            onProgressListener.onProgress(value);
        tvSeekLabel.setText(String.valueOf(value));
    }

    public OnProgressListener getOnProgressListener() {
        return onProgressListener;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public interface OnProgressListener {
        void onProgress(int value);
    }
}
