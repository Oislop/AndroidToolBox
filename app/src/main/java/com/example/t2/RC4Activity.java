package com.example.t2;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.t2.util.RC4Util;

public class RC4Activity extends AppCompatActivity {

    private Toolbar toolBar;
    private EditText editText;
    private EditText editText1;
    private TextView textView;
    RC4Util rc4Util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rc4);

        editText = (EditText) findViewById(R.id.textB);
        textView = (TextView) findViewById(R.id.textA);
        editText1 = (EditText) findViewById(R.id.textV);
        toolBar= (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        rc4Util = new RC4Util();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRC4(View v) {
        String s1 = editText.getText().toString();
        String s2 = editText1.getText().toString();
        try{

            textView.append(rc4Util.encryRC4String(s1,s2));
        } catch (Exception e){

        }


    }
    public void onDisRC4(View v) {
        String s1 = editText.getText().toString();
        String s2 = editText1.getText().toString();
        textView.setText("");
        try{
            textView.append(rc4Util.decryRC4(s1,s2));
        } catch (Exception e){

        }



    }


}
