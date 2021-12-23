package com.example.journalnotebook;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.journalnotebook.Alarm.AlarmReceiver;
import com.example.journalnotebook.Alarm.EditAlarmActivity;
import com.example.journalnotebook.Alarm.Plan;
import com.example.journalnotebook.Alarm.PlanAdapter;
import com.example.journalnotebook.Alarm.PlanDatabase;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;


public class MainActivity extends BaseActivity implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private NoteDatabase dbHelper;
    private PlanDatabase planDbHelper;
    private Toolbar myToolbar;
    TextView textView;
    private ListView lv;
    private Context context = this;
    private NoteAdapter adapter;
    private List<Note> noteList = new ArrayList<Note>();
    private List<Plan> planList = new ArrayList<Plan>();
    //fab
    private FloatingActionButton mAddMemoFab, mAddNoteFab;
    private ExtendedFloatingActionButton mAddFab;
    TextView addNoteActionText;
    private Boolean isAllFabsVisible;

    private SharedPreferences sharedPreferences;
    //private ToggleButton content_switch;

    private AlarmManager alarmManager;
    //private Achievement achievement;
    private ListView lv_plan;
    private LinearLayout lv_layout;
    private LinearLayout lv_plan_layout;
    private PlanAdapter planAdapter;

    public static int curId = 5;

    String[] list_String = {"before one month", "before three months", "before six months", "before one year"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.greyMain));
        setContentView(R.layout.activity_main);
        //实例化闹钟管理器
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //achievement = new Achievement(context);
        initView();

    }

    @Override
    protected void needRefresh() {
        setTheme(R.style.AppTheme);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("opMode", 10);
        startActivity(intent);
        overridePendingTransition(R.anim.night_switch, R.anim.night_switch_over);
        finish();
    }

    public void initView() {
        initPrefs();
        textView = findViewById(R.id.et);
        lv = findViewById(R.id.lv);
        myToolbar = findViewById(R.id.myToolbar);
        //content_switch = findViewById(R.id.content_switch);
        lv_plan = findViewById(R.id.lv_plan);
        lv_layout = findViewById(R.id.lv_layout);
        lv_plan_layout = findViewById(R.id.lv_plan_layout);
        refreshLvVisibility();
        adapter = new NoteAdapter(getApplicationContext(), noteList);
        planAdapter = new PlanAdapter(getApplicationContext(), planList);
        refreshListView();
        lv.setAdapter(adapter);
        lv_plan.setAdapter(planAdapter);

        //自定义状态栏
        setSupportActionBar(myToolbar);

        lv.setOnItemClickListener(this);   //点击操作
        lv_plan.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this); //长按操作
        lv_plan.setOnItemLongClickListener(this);

        //fab

        mAddNoteFab = findViewById(R.id.add_note_fab);

        addNoteActionText = findViewById(R.id.add_note_action_text);

        //设置fab

        mAddNoteFab.setVisibility(View.GONE);

        addNoteActionText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        //mAddMemoFab.show();
        mAddNoteFab.show();

        addNoteActionText.setVisibility(View.VISIBLE);
        //mAddFab.extend();
        isAllFabsVisible = true;


        //添加日记
        mAddNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("mode", 4);  //新建日记
                startActivityForResult(intent, 1);


            }
        });

    }

    private void refreshLvVisibility() {
        //决定应该显示notes还是plans
        lv_layout.setVisibility(View.VISIBLE);
        lv_plan_layout.setVisibility(GONE);

    }

    //接受返回的结果(包括：删除的)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        int returnMode;
        long note_Id;
        returnMode = data.getExtras().getInt("mode", -1);
        note_Id = data.getExtras().getLong("id", 0);

        if (returnMode == 1) {  //更新当前笔记内容
            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag", 1);
            Note newNote = new Note(content, time, tag);
            newNote.setId(note_Id);
            BaseCrud op = new BaseCrud(context);
            op.open();
            op.updateNote(newNote);
            //achievement.editNote(op.getNote(note_Id).getContent(), content);
            op.close();
        }else if (returnMode == 0) {  // 创建新笔记
            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag", 1);
            Note newNote = new Note(content, time, tag);
            BaseCrud op = new BaseCrud(context);
            op.open();
            op.addNote(newNote);
            op.close();
            //achievement.addNote(content);
        }else if(returnMode==2){ //删除已经创建好的笔记内容
            Note delNote=new Note();
            delNote.setId(note_Id);
            BaseCrud op = new BaseCrud(context);
            op.open();
            op.removeNote(delNote);
            op.close();
            //achievement.deleteNote();
        }else if (returnMode == 11){  //编辑备忘录
            String title = data.getExtras().getString("title", null);
            String content = data.getExtras().getString("content", null);
            String time = data.getExtras().getString("time", null);
            Log.d(TAG, time);
            Plan plan = new Plan(title, content, time);
            plan.setId(note_Id);
            com.example.journalnotebook.Alarm.AlarmCrud op = new com.example.journalnotebook.Alarm.AlarmCrud(context);
            op.open();
            op.updatePlan(plan);
            op.close();
        }else if (returnMode == 12){  //删除存在的备忘录
            Plan plan = new Plan();
            plan.setId(note_Id);
            com.example.journalnotebook.Alarm.AlarmCrud op = new com.example.journalnotebook.Alarm.AlarmCrud(context);
            op.open();
            op.removePlan(plan);
            op.close();
        }else if (returnMode == 10){  //创建新的备忘录
            String title = data.getExtras().getString("title", null);
            String content = data.getExtras().getString("content", null);
            String time = data.getExtras().getString("time", null);
            Plan newPlan = new Plan(title, content, time);
            com.example.journalnotebook.Alarm.AlarmCrud op = new com.example.journalnotebook.Alarm.AlarmCrud(context);
            op.open();
            op.addPlan(newPlan);
            Log.d(TAG, "onActivityResult: "+ time);
            op.close();
        }
        refreshListView();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initPrefs() {
        sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.contains("nightMode")) {
            editor.putBoolean("nightMode", false);
            editor.apply();
        }
        if (!sharedPreferences.contains("reverseSort")) {
            editor.putBoolean("reverseSort", false);
            editor.apply();
        }
        if (!sharedPreferences.contains("fabColor")) {
            editor.putInt("fabColor", -500041);
            editor.apply();
        }
        if (!sharedPreferences.contains("tagListString")) {
            String s = "no tag_life_study_work_play";
            editor.putString("tagListString", s);
            editor.apply();
        }


        if(!sharedPreferences.contains("fabPlanColor")){
            editor.putInt("fabPlanColor", -500041);
            editor.apply();
        }
        if(!sharedPreferences.contains("noteTitle")){
            editor.putBoolean("noteTitle", true);
            editor.apply();
        }
    }
    //实时更新列表内容
    private void refreshListView(){
        SharedPreferences sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        BaseCrud op = new BaseCrud(context);
        op.open();
        if (noteList.size() > 0) noteList.clear();
        noteList.addAll(op.getAllNotes());
        //排序
        if (sharedPreferences.getBoolean("reverseSort", false)) sortNotes(noteList, 2);
        else sortNotes(noteList, 1);
        op.close();
        adapter.notifyDataSetChanged();

        //
        com.example.journalnotebook.Alarm.AlarmCrud op1 = new com.example.journalnotebook.Alarm.AlarmCrud(context);
        op1.open();
        if(planList.size() > 0) {
            cancelAlarms(planList);//
            planList.clear();
        }
        planList.addAll(op1.getAllPlans());
        startAlarms(planList);//
        if (sharedPreferences.getBoolean("reverseSort", false)) sortPlans(planList, 2);
        else sortPlans(planList, 1);
        op1.close();
        planAdapter.notifyDataSetChanged();
        //achievement.listen();
    }

    //
    private void startAlarms(List<Plan> plans){
        for(int i = 0; i < plans.size(); i++) {
            startAlarm(plans.get(i));
        }
    }

    //
    private void startAlarm(Plan p) {
        Calendar c = p.getPlanTime();
        if(!c.before(Calendar.getInstance())) {
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            intent.putExtra("title", p.getTitle());
            intent.putExtra("content", p.getContent());
            intent.putExtra("id", (int)p.getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) p.getId(), intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

    //
    private void cancelAlarm(Plan p) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)p.getId(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    //
    private void cancelAlarms(List<Plan> plans){
        for(int i = 0; i < plans.size(); i++)
            cancelAlarm(plans.get(i));
    }

    @Override
    public void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if(intent!=null && intent.getIntExtra("mode", 0) == 1){
            //content_switch.setChecked(true);
            refreshLvVisibility();
        }
    }

    //主界面跳转编辑界面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv:
                Note curNote = (Note) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("content", curNote.getContent());
                intent.putExtra("id", curNote.getId());
                intent.putExtra("time", curNote.getTime());
                intent.putExtra("mode", 3);
                intent.putExtra("tag", curNote.getTag());
                startActivityForResult(intent, 1);
                break;
            case R.id.lv_plan:
                Plan curPlan = (Plan) parent.getItemAtPosition(position);
                Intent intent1 = new Intent(MainActivity.this, EditAlarmActivity.class);
                intent1.putExtra("title", curPlan.getTitle());
                intent1.putExtra("content", curPlan.getContent());
                intent1.putExtra("time", curPlan.getTime());
                intent1.putExtra("mode", 1);
                intent1.putExtra("id", curPlan.getId());
                startActivityForResult(intent1, 1);
                break;
        }
    }

    //长按删除日记
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv:
                final Note note = noteList.get(position);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确定")
                        .setMessage("确定要删除此条日记吗?")
                        .setIcon(R.drawable.ic_baseline_keyboard_voice_24)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BaseCrud op = new BaseCrud(context);
                                op.open();
                                op.removeNote(note);
                                op.close();
                                refreshListView();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            case R.id.lv_plan:
                final Plan plan = planList.get(position);
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("确定要删除此条备忘录吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                com.example.journalnotebook.Alarm.AlarmCrud op = new com.example.journalnotebook.Alarm.AlarmCrud(context);
                                op.open();
                                op.removePlan(plan);
                                op.close();
                                refreshListView();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
        return true;
    }

    //格式转换string -> milliseconds
    @RequiresApi(api = Build.VERSION_CODES.N)
    public long dateStrToSec(String date) throws ParseException, java.text.ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long secTime = format.parse(date).getTime();
        return secTime;
    }

    //转换当前的long值：1, 0, -1
    public int ChangeLong(Long l) {
        if (l > 0) return 1;
        else if (l < 0) return -1;
        else return 0;
    }

    //按时间排序笔记
    public void sortNotes(List<Note> noteList, final int mode) {
        Collections.sort(noteList, new Comparator<Note>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public int compare(Note o1, Note o2) {
                try {
                    if (mode == 1) {
                        return ChangeLong(dateStrToSec(o2.getTime()) - dateStrToSec(o1.getTime()));
                    }
                    else if (mode == 2) {//reverseSort
                        return ChangeLong(dateStrToSec(o1.getTime()) - dateStrToSec(o2.getTime()));
                    }
                } catch (ParseException | java.text.ParseException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
    }

    //按备忘录时间排序
    public void sortPlans(List<Plan> planList, final int mode){
        Collections.sort(planList, new Comparator<Plan>() {
            @Override
            public int compare(Plan o1, Plan o2) {
                try {
                    if (mode == 1)
                        return ChangeLong(calStrToSec(o1.getTime()) - calStrToSec(o2.getTime()));
                    else if (mode == 2) //reverseSort
                        return ChangeLong(calStrToSec(o2.getTime()) - calStrToSec(o1.getTime()));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
    }
    public long calStrToSec(String date) throws java.text.ParseException {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        long secTime = Objects.requireNonNull(format.parse(date)).getTime();
        return secTime;
    }
}