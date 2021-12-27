package com.example.journalnotebook;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends BaseActivity implements View.OnClickListener {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    private EditText editText,editText2,editText3,editText4;
    private String old_content = "";
    private String old_endpoint = "";
    private String old_price = "";
    private String old_text = "";
    private long old_fileid;
    private long halfname = 0;
    private String old_filetag = "";

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

    private ImageView cameraPicture;

    private Uri imageUri, files;

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

        Button takePhoto = findViewById(R.id.take_photo);
        //TODO cameraPicture
        cameraPicture = (ImageView) findViewById(R.id.picture);

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
            old_price = getIntent.getStringExtra("price");old_text = getIntent.getStringExtra("text");
            old_fileid = getIntent.getLongExtra("fileid", 1L);
            Log.e("1212-3", String.valueOf(old_fileid));
            old_filetag = getIntent.getStringExtra("filetag");

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


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 动态申请权限
                if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    AlertDialog alertDialog2 = new AlertDialog.Builder(cameraPicture.getContext())
                            .setTitle("请求访问摄像头权限")
                            .setMessage("授权摄像头权限后请再次尝试拍照")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.CAMERA}, TAKE_PHOTO);
                                    Toast.makeText(getApplicationContext(),"授权摄像头权限后请再次尝试拍照！",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(EditActivity.this, "授权摄像头权限后才能拍照哦!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create();
                    alertDialog2.show();
                } else {
                    if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        AlertDialog alertDialog2 = new AlertDialog.Builder(cameraPicture.getContext())
                                .setTitle("还差一步，请授权访问手机存储")
                                .setMessage("授权后请再次尝试拍照!")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_PHOTO);
                                        Toast.makeText(getApplicationContext(),"授权后请再次尝试拍照！",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(EditActivity.this, "授权访问手机存储后才能拍照哦!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .create();
                        alertDialog2.show();

                    }else {
                        takePic();
                        Log.e("1211-2","startCamera");
                    }

                }
            }
        });

        //TODO 文件夹 文件的调用方法
        File appDir = new File(Environment.getExternalStorageDirectory(), "Mycamera");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        //String fileName = "1640510628736" +".jpg";
        String fileName = old_fileid +".jpg";
        File file = new File(appDir, fileName);



        // 创建一个File对象，用于保存摄像头拍下的图片，这里把图片命名为output_image.jpg
        // 并将它存放在手机SD卡的应用关联缓存目录下
        //TODO cache 文件的调用方法
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        // 对照片的更换设置
        try {
            // 如果上一次的照片存在，就删除
            if (outputImage.exists()) {
                outputImage.delete();
            }
            // 创建一个新的文件
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 如果Android版本大于等于7.0
        if (Build.VERSION.SDK_INT >= 24) {
            // 将File对象转换成一个封装过的Uri对象
            imageUri = FileProvider.getUriForFile(this, "com.example.journalnotebook.fileprovider", outputImage);
            files = FileProvider.getUriForFile(this, "com.example.journalnotebook.fileprovider", file);
            Log.e("MainActivity", outputImage.toString() + " 手机系统版本高于Android7.0");
        } else {
            // 将File对象转换为Uri对象，这个Uri标识着output_image.jpg这张图片的本地真实路径
            Log.e("MainActivity", outputImage.toString() + " 手机系统版本低于Android7.0");
            imageUri = Uri.fromFile(outputImage);
            files = Uri.fromFile(file);
        }

        //long old_fileid2 = getIntent.getLongExtra("fileid", 1);
        if(old_fileid == 0 ) {
            //新建日记时候加载默认图片
            Log.e("1212-1", String.valueOf(old_fileid));
            // 将图片解析成Bitmap对象
            Resources res = cameraPicture.getContext().getResources();
            int id = R.drawable.bgm_nodata;
            Bitmap b = BitmapFactory.decodeResource(res, id);
            //Bitmap bitmap2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(files));
            cameraPicture.setImageBitmap(b);
        }else {
            //编辑笔记加载对应名称的旧图片
            Log.e("1212-2", String.valueOf(old_fileid));
            try {
                // 将图片解析成Bitmap对象
                Bitmap bitmap2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(files));
                cameraPicture.setImageBitmap(bitmap2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        //获取保存的背景色
        getWindow().setBackgroundDrawableResource(curColor[MainActivity.curId]);
    }

protected void takePic() {

    // 动态申请权限
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.CAMERA}, TAKE_PHOTO);
    } else {
        // 启动相机程序
        startCamera();
    }
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
         //数据回调
        String dates = date.getText().toString() + " " + time.getText().toString();
        if (openMode == 4) {
            //新建，但是啥也没填
            if (editText.getText().toString().length() == 0) {
                intent.putExtra("mode", -1);

            } else {
                intent.putExtra("mode", 0);
                intent.putExtra("content", editText.getText().toString());

                intent.putExtra("endpoint", editText2.getText().toString());
                intent.putExtra("price", editText3.getText().toString());
                intent.putExtra("text", editText4.getText().toString());
                //TODO
                Log.e("1213-1", String.valueOf(halfname));
                intent.putExtra("fileid", halfname);
                intent.putExtra("filetag", "00");

                intent.putExtra("time", date.getText().toString() + " " + time.getText().toString());
                intent.putExtra("tag", tag);
            }
        }
        else {
            // 没有修改
            if (editText.getText().toString().equals(old_content) &&
                    editText2.getText().toString().equals(old_endpoint) &&
                    editText3.getText().toString().equals(old_price) &&
                    editText4.getText().toString().equals(old_text) &&
                    halfname == 0 &&
                    dates.equals(old_time) && !tagChange) {
                intent.putExtra("mode", -1);
                Log.e("1213-3", String.valueOf(halfname));
            }
            else {
                //有修改
                intent.putExtra("mode", 1);
                intent.putExtra("content", editText.getText().toString());

                intent.putExtra("endpoint", editText2.getText().toString());
                intent.putExtra("price", editText3.getText().toString());
                intent.putExtra("text", editText4.getText().toString());

                //TODO reedit the pic,need to save the new picdata, justify weather change the pic
                Log.e("1213-2", String.valueOf(halfname));

                if(halfname == 0 ) {
                    intent.putExtra("fileid", old_fileid);
                }else {
                    intent.putExtra("fileid", halfname);
                    //if (old_fileid.to.equals(halfname))
                }
                //intent.putExtra("fileid", 1640537623616L);
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

    private void startCamera() {
        Intent intent4 = new Intent("android.media.action.IMAGE_CAPTURE");
        // 指定图片的输出地址为imageUri
        intent4.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent4, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
                    try {
                        // 将图片解析成Bitmap对象
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        cameraPicture.setImageBitmap(bitmap);
                        //rename and 将图片保存到本地
                        saveToSystemGallery(bitmap);
                        Toast.makeText(getApplicationContext(),"图片loading成功！",Toast.LENGTH_SHORT).show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void saveToSystemGallery(Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Mycamera");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        halfname = System.currentTimeMillis();
        String fileName =  halfname + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        // 发送广播，通知图库更新
        sendBroadcast(intent);
    }
}