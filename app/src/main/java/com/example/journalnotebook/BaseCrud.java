package com.example.journalnotebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BaseCrud {
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;

    private static final String[] columns = {
            NoteDatabase.ID,
            NoteDatabase.CONTENT,

            NoteDatabase.ENDPOINT,
            NoteDatabase.PRICE,
            NoteDatabase.TEXT,
            NoteDatabase.FILEID,
            NoteDatabase.FILETAG,

            NoteDatabase.TIME,
            NoteDatabase.MODE
    };

    public BaseCrud(Context context){
        dbHandler = new NoteDatabase(context);
    }



    public void open(){
        db = dbHandler.getWritableDatabase();
    }

    public void close(){
        dbHandler.close();
    }

    //加入内容到数据库的列
    public Note addNote(Note note){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatabase.CONTENT, note.getContent());

        contentValues.put(NoteDatabase.ENDPOINT, note.getEndpoint());
        contentValues.put(NoteDatabase.PRICE, note.getPrice());
        contentValues.put(NoteDatabase.TEXT, note.getText());
        contentValues.put(NoteDatabase.FILEID, note.getFileid());
        contentValues.put(NoteDatabase.FILETAG, note.getFiletag());

        contentValues.put(NoteDatabase.TIME, note.getTime());
        contentValues.put(NoteDatabase.MODE, note.getTag());
        long insertId = db.insert(NoteDatabase.TABLE_NAME, null, contentValues);
        note.setId(insertId);
        return note;
    }

    public Note getNote(long id){
        Cursor cursor = db.query(NoteDatabase.TABLE_NAME,columns,NoteDatabase.ID + "=?",
                new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Note e = new Note(cursor.getString(1),cursor.getString(2), cursor.getString(3),cursor.getString(4),
                cursor.getInt(5),cursor.getString(6),cursor.getString(7),cursor.getInt(8));
        return e;
    }


    public List<Note> getAllNotes(){
        Cursor cursor = db.query(NoteDatabase.TABLE_NAME,columns,null,null,null, null, null);

        List<Note> notes = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Note note = new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(NoteDatabase.ID)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NoteDatabase.CONTENT)));

                note.setEndpoint(cursor.getString(cursor.getColumnIndex(NoteDatabase.ENDPOINT)));
                note.setPrice(cursor.getString(cursor.getColumnIndex(NoteDatabase.PRICE)));
                note.setText(cursor.getString(cursor.getColumnIndex(NoteDatabase.TEXT)));
                //TODO REMEMBER TO CAREFULNESS
                note.setFileid(cursor.getLong(cursor.getColumnIndex(NoteDatabase.FILEID)));
                note.setFiletag(cursor.getString(cursor.getColumnIndex(NoteDatabase.FILETAG)));

                note.setTime(cursor.getString(cursor.getColumnIndex(NoteDatabase.TIME)));
                note.setTag(cursor.getInt(cursor.getColumnIndex(NoteDatabase.MODE)));
                notes.add(note);
            }
        }
        return notes;
    }
    public int updateNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteDatabase.CONTENT, note.getContent());

        values.put(NoteDatabase.ENDPOINT, note.getEndpoint());
        values.put(NoteDatabase.PRICE, note.getPrice());
        values.put(NoteDatabase.TEXT, note.getText());
        values.put(NoteDatabase.FILEID, note.getFileid());
        values.put(NoteDatabase.FILETAG, note.getFiletag());

        values.put(NoteDatabase.TIME, note.getTime());
        values.put(NoteDatabase.MODE, note.getTag());
        return db.update(NoteDatabase.TABLE_NAME, values,
                NoteDatabase.ID + "=?",new String[] { String.valueOf(note.getId())});
    }

    public void removeNote(Note note) {
        db.delete(NoteDatabase.TABLE_NAME, NoteDatabase.ID + "=" + note.getId(), null);
    }

}
