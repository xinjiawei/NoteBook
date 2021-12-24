package com.example.journalnotebook;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends BaseActivity {

    private EditText editText;
    private String old_content = "";
    private String old_time = "";
    private int old_Tag = 1;
    private long id = 0;
    private int openMode = 0;
    private int tag = 1;
    public Intent intent = new Intent(); //信息的发送
    private boolean tagChange = false;
    private Toolbar myToolbar;
    private boolean isRead;
    Toast toast1, toast2;
    private Intent intentMusic;
    private int[] curColor = {
            R.color.blackColor,
            R.color.Violet,
            R.color.DoderBlue,
            R.color.Auqamarin,
            R.color.HotPink,
            R.color.white};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);



        editText = findViewById(R.id.et);
        myToolbar=findViewById(R.id.myToolbar);


        //编辑界面的头部
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //实现返回主界面
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoSetMessage();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //返回编辑内容的相关操作
        Intent getIntent = getIntent();
        openMode = getIntent.getIntExtra("mode", 0);

        if (openMode == 3) {//打开已存在的note
            id = getIntent.getLongExtra("id", 0);
            old_content = getIntent.getStringExtra("content");
            old_time = getIntent.getStringExtra("time");
            old_Tag = getIntent.getIntExtra("tag", 1);
            editText.setText(old_content);
            editText.setSelection(old_content.length());
        }

        //获取保存的背景色
        getWindow().setBackgroundDrawableResource(curColor[MainActivity.curId]);
    }

    @Override
    protected void needRefresh() {
        setTheme(R.style.AppTheme);
        startActivity(new Intent(this, EditActivity.class));
        overridePendingTransition(R.anim.night_switch, R.anim.night_switch_over);
        finish();
    }

    //返回编辑页并保存内容
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            autoSetMessage();
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        //TODO stopService(intentMusic);
        return super.onKeyDown(keyCode, event);
    }


    public void autoSetMessage() {
        if (openMode == 4) {
            if (editText.getText().toString().length() == 0) {
                intent.putExtra("mode", -1); //没有信息
            } else {
                intent.putExtra("mode", 0); // 有一个新的
                intent.putExtra("content", editText.getText().toString());
                intent.putExtra("time", dateToStr());
                intent.putExtra("tag", tag);
            }
        } else {
            if (editText.getText().toString().equals(old_content) && !tagChange)
                intent.putExtra("mode", -1); // 没有修改
            else {
                intent.putExtra("mode", 1); //有修改
                intent.putExtra("content", editText.getText().toString());
                intent.putExtra("time", dateToStr());
                intent.putExtra("id", id);
                intent.putExtra("tag", tag);
            }
        }
    }
    //时间戳
    public String dateToStr () {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}