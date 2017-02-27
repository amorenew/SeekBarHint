package com.example.seekbarhint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import it.moondroid.seekbarhint.library.SeekBarHint;


public class MainActivity extends AppCompatActivity implements ScrollListener {

    private SeekBarHint mSeekBarH;
    //    private SeekBarHint mSeekBarV;
    private LinearLayout layout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout1.setVisibility(View.GONE);
        mSeekBarH = (SeekBarHint) findViewById(R.id.seekbar_horizontal);
//        mSeekBarV = (SeekBarHint) findViewById(R.id.seekbar_vertical);

        mSeekBarH.setHintAdapter(new SeekBarHint.SeekBarHintAdapter() {
            @Override
            public String getHint(SeekBarHint seekBarHint, int progress) {
                return ">" + progress;
            }
        });
//        mSeekBarV.setHintAdapter(new SeekBarHint.SeekBarHintAdapter() {
//            @Override
//            public String getHint(SeekBarHint seekBarHint, int progress) {
//                return "Vertical: " + progress;
//            }
//        });
//        childAgePicker1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        if (scrollListener != null)
//                            scrollListener.enableScroll(false);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if (scrollListener != null)
//                            scrollListener.enableScroll(true);
//                        break;
//                }
//                return true;
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fixed:
                mSeekBarH.setPopupStyle(SeekBarHint.POPUP_FIXED);
//                mSeekBarV.setPopupStyle(SeekBarHint.POPUP_FIXED);
                layout1.setVisibility(View.VISIBLE);
                return true;

            case R.id.action_follow:
                mSeekBarH.setPopupStyle(SeekBarHint.POPUP_FOLLOW);
//                mSeekBarV.setPopupStyle(SeekBarHint.POPUP_FOLLOW);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void enableScroll(boolean isScroll) {
//            scrollContainer.setScrollingEnabled(isScroll);
    }
}
