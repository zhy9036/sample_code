package com.zy.dbutil;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDbAdapter{
	
	public static final String BODY = "body";
	public static final String ROWID = "_id";
	public static final String TIME = "cur_time";
	public static final String CREATED = "created";
	
	private static final String TAG = "DiaryDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private static final String SQL = "create table memo(_id integer primary key autoincrement,"
			+"body text not null, created text not null, cur_time text not null);";
	public static final String DATABASE_NAME="database";
	public static final String DATABASE_TABLE="memo";
	private static final int DB_VERSION = 2;
	
	private final Context mCtx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper{

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DB_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS memo");
			onCreate(db);
		}
		
	}

	public MemoDbAdapter(Context ctx){
		mCtx = ctx;
	}
	
	public MemoDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	public void close(){
		mDbHelper.close();
	}
	
	public long createMemo(String body){
		ContentValues initialValues = new ContentValues();
		initialValues.put(BODY, body);
		Calendar cal = Calendar.getInstance();
		DateFormat calendar = DateFormat.getDateInstance(DateFormat.FULL);
		String created = calendar.format(new Date());
		initialValues.put(CREATED, created);
		String mins = (cal.get(Calendar.MINUTE)<10)?"0"+cal.get(Calendar.MINUTE):cal.get(Calendar.MINUTE)+"";
		initialValues.put(TIME, cal.get(Calendar.HOUR_OF_DAY)+":"
				   +mins);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public boolean deleteDiary(long rowId){
		return mDb.delete(DATABASE_TABLE, ROWID+"="+rowId, null)>0;
	}
	public Cursor getAllNotes(){
		return mDb.query(DATABASE_TABLE, new String[]{ROWID,BODY,CREATED,TIME}, null, null, null, null, null);
	}
	
	public Cursor getMemo(long rowId) throws SQLException{
		Cursor mCursor=mDb.query(true, DATABASE_TABLE, new String[]{ROWID,BODY,CREATED,TIME},
				ROWID+"="+rowId, null,null,null,null,null);
		if(mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public boolean undateMemo(long rowId, String body){
		ContentValues initialValues = new ContentValues();
		initialValues.put(BODY, body);
		Calendar cal = Calendar.getInstance();
		DateFormat calendar = DateFormat.getDateInstance(DateFormat.FULL);
		String created = calendar.format(new Date());
		initialValues.put(CREATED, created);
		String mins = (cal.get(Calendar.MINUTE)<10)?"0"+cal.get(Calendar.MINUTE):cal.get(Calendar.MINUTE)+"";
		initialValues.put(TIME, cal.get(Calendar.HOUR_OF_DAY)+":"
				   +mins);
		initialValues.put(CREATED, created);
		return mDb.update(DATABASE_TABLE,initialValues,ROWID+"="+rowId, null)>0;	
	}
	
	
}
