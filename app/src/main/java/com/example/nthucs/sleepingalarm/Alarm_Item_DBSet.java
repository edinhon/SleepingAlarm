package com.example.nthucs.sleepingalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/6/19.
 */
public class Alarm_Item_DBSet {
    // Table Name
    public static final String TABLE_NAME = "alarm_item";

    //Key ID
    public static final String KEY_ID = "_id";

    // Other columns
    public static final String HOUR_COLUMN = "hour";
    public static final String MINUTE_COLUMN = "minute";
    public static final String TEXT_COLUMN = "text";
    public static final String MONDAY_COLUMN = "Monday";
    public static final String TUESDAY_COLUMN = "Tuesday";
    public static final String WEDNESDAY_COLUMN = "Wednesday";
    public static final String THURSDAY_COLUMN = "Thursday";
    public static final String FRIDAY_COLUMN = "Friday";
    public static final String SATURDAY_COLUMN = "Saturday";
    public static final String SUNDAY_COLUMN = "Sunday";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    HOUR_COLUMN + " INTEGER NOT NULL, " +
                    MINUTE_COLUMN + " INTEGER NOT NULL, " +
                    TEXT_COLUMN + " TEXT NOT NULL, " +
                    MONDAY_COLUMN + " INTEGER NOT NULL, " +
                    TUESDAY_COLUMN + " INTEGER NOT NULL, " +
                    WEDNESDAY_COLUMN + " INTEGER NOT NULL, " +
                    THURSDAY_COLUMN + " INTEGER NOT NULL, " +
                    FRIDAY_COLUMN + " INTEGER NOT NULL, " +
                    SATURDAY_COLUMN + " INTEGER NOT NULL, " +
                    SUNDAY_COLUMN + " INTEGER NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public Alarm_Item_DBSet(Context context) {
        db = AlarmDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public Alarm_Item insert(Alarm_Item item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(HOUR_COLUMN, item.getHour());
        cv.put(MINUTE_COLUMN, item.getMinute());
        cv.put(TEXT_COLUMN, item.getText());
        if(item.weekStart[0]) cv.put(MONDAY_COLUMN, 1);
        else cv.put(MONDAY_COLUMN, 0);
        if(item.weekStart[1]) cv.put(TUESDAY_COLUMN, 1);
        else cv.put(TUESDAY_COLUMN, 0);
        if(item.weekStart[2]) cv.put(WEDNESDAY_COLUMN, 1);
        else cv.put(WEDNESDAY_COLUMN, 0);
        if(item.weekStart[3]) cv.put(THURSDAY_COLUMN, 1);
        else cv.put(THURSDAY_COLUMN, 0);
        if(item.weekStart[4]) cv.put(FRIDAY_COLUMN, 1);
        else cv.put(FRIDAY_COLUMN, 0);
        if(item.weekStart[5]) cv.put(SATURDAY_COLUMN, 1);
        else cv.put(SATURDAY_COLUMN, 0);
        if(item.weekStart[6]) cv.put(SUNDAY_COLUMN, 1);
        else cv.put(SUNDAY_COLUMN, 0);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        item.setId(id);
        // 回傳結果
        return item;
    }

    // 修改參數指定的物件
    public boolean update(Alarm_Item item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        cv.put(HOUR_COLUMN, item.getHour());
        cv.put(MINUTE_COLUMN, item.getMinute());
        cv.put(TEXT_COLUMN, item.getText());
        if(item.weekStart[0]) cv.put(MONDAY_COLUMN, 1);
        else cv.put(MONDAY_COLUMN, 0);
        if(item.weekStart[1]) cv.put(TUESDAY_COLUMN, 1);
        else cv.put(TUESDAY_COLUMN, 0);
        if(item.weekStart[2]) cv.put(WEDNESDAY_COLUMN, 1);
        else cv.put(WEDNESDAY_COLUMN, 0);
        if(item.weekStart[3]) cv.put(THURSDAY_COLUMN, 1);
        else cv.put(THURSDAY_COLUMN, 0);
        if(item.weekStart[4]) cv.put(FRIDAY_COLUMN, 1);
        else cv.put(FRIDAY_COLUMN, 0);
        if(item.weekStart[5]) cv.put(SATURDAY_COLUMN, 1);
        else cv.put(SATURDAY_COLUMN, 0);
        if(item.weekStart[6]) cv.put(SUNDAY_COLUMN, 1);
        else cv.put(SUNDAY_COLUMN, 0);

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + item.getId();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    // 讀取所有記事資料
    public ArrayList<Alarm_Item> getAll() {
        ArrayList<Alarm_Item> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public Alarm_Item get(long id) {
        // 準備回傳結果用的物件
        Alarm_Item item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    // 把Cursor目前的資料包裝為物件
    public Alarm_Item getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Alarm_Item result = new Alarm_Item(0, 0, "");

        result.setId(cursor.getLong(0));
        result.setHour(cursor.getInt(1));
        result.setMinute(cursor.getInt(2));
        result.setText(cursor.getString(3));
        if(cursor.getInt(4) == 1) result.weekStart[0] = true;
        else result.weekStart[0] = false;
        if(cursor.getInt(5) == 1) result.weekStart[1] = true;
        else result.weekStart[1] = false;
        if(cursor.getInt(6) == 1) result.weekStart[2] = true;
        else result.weekStart[2] = false;
        if(cursor.getInt(7) == 1) result.weekStart[3] = true;
        else result.weekStart[3] = false;
        if(cursor.getInt(8) == 1) result.weekStart[4] = true;
        else result.weekStart[4] = false;
        if(cursor.getInt(9) == 1) result.weekStart[5] = true;
        else result.weekStart[5] = false;
        if(cursor.getInt(10) == 1) result.weekStart[6] = true;
        else result.weekStart[6] = false;

        // 回傳結果
        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }
}
