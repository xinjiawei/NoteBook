package com.example.journalnotebook;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "com.example.journalnotebook.action.FOO";
    public static final String ACTION_BAZ = "com.example.journalnotebook.action.BAZ";

    // action声明 这里有五个背景音乐
    public static final String ACTION_MUSIC_1 = "com.example.journalnotebook.action.music1";
    public static final String ACTION_MUSIC_2 = "com.example.journalnotebook.action.music2";
    public static final String ACTION_MUSIC_3 = "com.example.journalnotebook.action.music3";
    public static final String ACTION_MUSIC_4 = "com.example.journalnotebook.action.music4";
    public static final String ACTION_MUSIC_5 = "com.example.journalnotebook.action.music5";
    public static final String ACTION_MUSIC_6 = "com.example.journalnotebook.action.music6";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.example.journalnotebook.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.journalnotebook.extra.PARAM2";

    // 声明MediaPlayer对象
    private static MediaPlayer mediaPlayer;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        if (intent != null) {
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }

            // 根据intent设置的action来执行对应服务的操作
            if (ACTION_MUSIC_1.equals(action)){
                //handleActionMusic1();
            }else if (ACTION_MUSIC_2.equals(action)){
                //handleActionMusic2();
            }else if (ACTION_MUSIC_3.equals(action)){
                //handleActionMusic3();
            }else if (ACTION_MUSIC_4.equals(action)){
                //handleActionMusic4();
            }else if (ACTION_MUSIC_5.equals(action)){
                //handleActionMusic5();
            }else if (ACTION_MUSIC_6.equals(action)){
                //handleActionMusic6();
            }
        }
    }



    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

