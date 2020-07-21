package com.example.t2.shortLink;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.t2.R;
import com.githang.statusbar.StatusBarCompat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class shortLinkActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private EditText editText;
    private TextView textView;
    private Button button;
    private Spinner mSpinner;
    private Handler handler;
    private String longurl = null, url;
    boolean isurl = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_link);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        createSpinner();

    }

    private void setUrl() {
        Pattern pattern = Pattern.compile("[hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://|[wW]{3}/.");
        Matcher matcher = pattern.matcher(url);
        url = matcher.find() && matcher.start() == 0 ? url : "http://" + url;

    }

    protected void createSpinner() {
        mSpinner = (Spinner) findViewById(R.id.spinner);
        editText = (EditText) findViewById(R.id.editText_shortlink);
        button = (Button) findViewById(R.id.button14);
        textView = (TextView) findViewById(R.id.textview_shortlink);
        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        textView.setText(longurl);
                    }
                };

                String str = mSpinner.getSelectedItem().toString();
                if (str.equals("百度")) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            url = editText.getText().toString().trim();
                            setUrl();
                            Log.d("***SUC*** ", "url:" + url);
                            longurl = baiduDwz.createShortUrl(url);
                            if(longurl ==""){
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "非法URL", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            Log.d("***SUC*** ", "longurl:" + longurl);
                            Message m = handler.obtainMessage();
                            handler.sendMessage(m);

                        }
                    }).start();

                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            url = editText.getText().toString().trim();
                            Log.d("***SUC*** ", "url:" + url);
                            setUrl();
                            longurl = SinaTcn.sinaShortUrl(url);
                            Log.d("***SUC*** ", "longurl:" + longurl);
                            Message m = handler.obtainMessage();
                            handler.sendMessage(m);
                        }
                    }).start();

                }
            }
        });
    }


}
