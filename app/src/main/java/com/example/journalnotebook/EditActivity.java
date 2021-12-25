package com.example.journalnotebook;

import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    private EditText editText,editText2,editText3,editText4;
    private String old_content = "";
    private String old_endpoint = "";
    private String old_price = "";
    private String old_text = "";

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private Button set_date;
    private Button set_time;
    private TextView date;
    private TextView time;
    private int[] dateArray = new int[3];
    private int[] timeArray = new int[2];

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
        editText2 = findViewById(R.id.et2);
        editText3 = findViewById(R.id.et3);
        editText4 = findViewById(R.id.et4);

        set_date = findViewById(R.id.set_date);
        set_time = findViewById(R.id.set_time);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        init();
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
             old_endpoint = getIntent.getStringExtra("endpoint");
            //Log.e("1202",old_endpoint);
             old_price = getIntent.getStringExtra("price");
             old_text = getIntent.getStringExtra("text");
            long old_fileid = getIntent.getLongExtra("fileid", 1);
            String old_filetag = getIntent.getStringExtra("filetag");

            old_time = getIntent.getStringExtra("time");
            Log.e("1202",old_time);
            String[] wholeTime = old_time.split(" ");
            String[] temp = wholeTime[0].split("-");
            String[] temp1 = wholeTime[1].split(":");
            Log.e("1202-1",temp[0] + "," + temp[1] + "," + temp[2] + ",");
            setDateTV(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            setTimeTV(Integer.parseInt(temp1[0]), Integer.parseInt(temp1[1]));


            //setTimeTV(12,52);

            old_Tag = getIntent.getIntExtra("tag", 1);
            editText.setText(old_content);

            editText2.setText(old_endpoint);
            editText3.setText(old_price);
            editText4.setText(old_text);

            editText.setSelection(old_content.length());
            /*
            editText2.setSelection(old_endpoint.length());
            editText3.setSelection(old_price.length());
            editText4.setSelection(old_text.length());

             */
        }else if (openMode == 4){
            old_time = dateToStr();
            Log.e("1207",old_time);;
            String[] wholeTime = old_time.split(" ");
            String[] temp = wholeTime[0].split("-");
            String[] temp1 = wholeTime[1].split(":");
            Log.e("1202-1",temp[0] + "," + temp[1] + "," + temp[2] + ",");
            setDateTV(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            setTimeTV(Integer.parseInt(temp1[0]), Integer.parseInt(temp1[1]));

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
        String dates = date.getText().toString() + " " + time.getText().toString();
        if (openMode == 4) {
            if (editText.getText().toString().length() == 0) {
                intent.putExtra("mode", -1); //没有信息

            } else {
                intent.putExtra("mode", 0); // 有一个新的
                intent.putExtra("content", editText.getText().toString());

                intent.putExtra("endpoint", editText2.getText().toString());
                intent.putExtra("price", editText3.getText().toString());
                intent.putExtra("text", editText4.getText().toString());
                //TODO
                intent.putExtra("fileid", 1);
                intent.putExtra("filetag", "00");

                intent.putExtra("time", date.getText().toString() + " " + time.getText().toString());
                intent.putExtra("tag", tag);
            }
        }
        else {
            if (editText.getText().toString().equals(old_content) &&
                    editText2.getText().toString().equals(old_endpoint) &&
                    editText3.getText().toString().equals(old_price) &&
                    editText4.getText().toString().equals(old_text) &&
                    dates.equals(old_time) && !tagChange)
                intent.putExtra("mode", -1); // 没有修改
            else {
                intent.putExtra("mode", 1); //有修改
                intent.putExtra("content", editText.getText().toString());

                intent.putExtra("endpoint", editText2.getText().toString());
                intent.putExtra("price", editText3.getText().toString());
                intent.putExtra("text", editText4.getText().toString());
                //TODO
                intent.putExtra("fileid", 1);
                intent.putExtra("filetag", "00");

                //TODO time THE PLUG PUT intent.putExtra("time", old_time);
                intent.putExtra("time", date.getText().toString() + " " + time.getText().toString());
                Log.e("1209",date.getText().toString() + " " + time.getText().toString());
                intent.putExtra("id", id);
                intent.putExtra("tag", tag);
            }
        }

    }
    //时间戳
    public String dateToStr () {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }

    private void init(){
        Calendar ca = Calendar.getInstance();
        int  mYear = ca.get(Calendar.YEAR);
        int  mMonth = ca.get(Calendar.MONTH);
        int  mDay = ca.get(Calendar.DAY_OF_MONTH);
        int  mHour = ca.get(Calendar.HOUR_OF_DAY);
        int  mMinute = ca.get(Calendar.MINUTE);


        dateArray[0] = mYear;
        dateArray[1] = mMonth;
        dateArray[2] = mDay;
        timeArray[0] = mHour;
        timeArray[1] = mMinute;

        set_date = findViewById(R.id.set_date);
        set_time = findViewById(R.id.set_time);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        //初始化两个textView
        setDateTV(dateArray[0], dateArray[1], dateArray[2]);
        setTimeTV((timeArray[1]>54? timeArray[0]+1 : timeArray[0]), (timeArray[1]+5)%60);
        Log.d(TAG, "init: "+dateArray[1]);


        set_date.setOnClickListener(this);
        set_time.setOnClickListener(this);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                /*
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;

                 */
                setDateTV(year, month+1, dayOfMonth);

            }
        };
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setTimeTV(hourOfDay, minute);
            }
        };
    }


    private void setDateTV(int y, int m, int d){
        //更新tv和dateArray
        String temp = y + "-";
        if(m<10) temp += "0";
        temp += (m + "-");
        if(d<10) temp +="0";
        temp += d;
        date.setText(temp);
        dateArray[0] = y;
        dateArray[1] = m;
        dateArray[2] = d;
    }

    private void setTimeTV(int h, int m){
        String temp = "";
        if(h<10) temp += "0";
        temp += (h + ":");
        if(m<10) temp += "0";
        temp += m;
        time.setText(temp);
        timeArray[0] = h;
        timeArray[1] = m;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_date: //选择日期
                DatePickerDialog dialog = new DatePickerDialog(com.example.journalnotebook.EditActivity.this,
                        R.style.DayDialogTheme, dateSetListener,
                        dateArray[0], dateArray[1] - 1, dateArray[2]);
                dialog.show();
                break;
            case R.id.set_time://选择时间
                TimePickerDialog dialog1 = new TimePickerDialog(com.example.journalnotebook.EditActivity.this,
                        R.style.DayDialogTheme, timeSetListener,
                        timeArray[0], timeArray[1], true);
                dialog1.show();
                break;
        }
    }

}