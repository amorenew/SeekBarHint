package com.example.seekbarhint;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Locale;

import amorenew.library.LockableScrollView;
import amorenew.library.ScrollListener;
import amorenew.library.SeekBarHint;
import amorenew.library.SeekBarLabel;


public class MainActivity extends AppCompatActivity implements ScrollListener {

    private SeekBarHint mSeekBarH;
    //    private SeekBarHint mSeekBarV;
    private LinearLayout layout1;
    private SeekBarLabel seekLabel1;
    private LockableScrollView lockableScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lockableScrollView = findViewById(R.id.lockableScrollView);
        layout1 = findViewById(R.id.layout1);
        layout1.setVisibility(View.VISIBLE);
        mSeekBarH = findViewById(R.id.seekbar_horizontal);
        seekLabel1 = findViewById(R.id.seekLabel1);
        seekLabel1.setProgress(10);
        seekLabel1.setOnProgressListener(new SeekBarLabel.OnProgressListener() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public String getProgressText(int progress) {
                return "[" + progress + "]";
            }
        });
        seekLabel1.setScrollListener(new ScrollListener() {
            @Override
            public void enableScroll(boolean isScroll) {
                lockableScrollView.setScrollingEnabled(isScroll);
            }
        });
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
                seekLabel1.setProgress(8);
                return true;

            case R.id.action_follow:
                mSeekBarH.setPopupStyle(SeekBarHint.POPUP_FOLLOW);
                Resources resources = getResources();
                String localLanguage = resources.getConfiguration().locale.getLanguage();
                Locale locale = new Locale("AR");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                resources.updateConfiguration(config, resources.getDisplayMetrics());
                recreate();
//                mSeekBarV.setPopupStyle(SeekBarHint.POPUP_FOLLOW);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void enableScroll(boolean isScroll) {
        // scrollContainer.setScrollingEnabled(isScroll);
    }
}
