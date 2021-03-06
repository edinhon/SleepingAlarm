package com.example.nthucs.sleepingalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by NTHUCS on 2016/6/22.
 */
public class Parameter_DBSet {

    // Table Name
    public static final String TABLE_NAME = "parameter";

    //Key ID
    public static final String KEY_ID = "_id";

    // Other columns
    public static final String VIBRATABLE_COLUMN = "vibratable";
    public static final String MONEY_COLUMN = "money";
    public static final String NUMBER_TIME_TICKET_COLUMN = "numberTimeTicket";
    public static final String NUMBER_RING_TICKET_COLUMN = "numberRingTicket";
    public static final String SLEEP_TIME_COLUMN = "sleepTime";
    public static final String ALL_SET_COLUMN = "allSet";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    VIBRATABLE_COLUMN + " INTEGER NOT NULL, " +
                    MONEY_COLUMN + " INTEGER NOT NULL, " +
                    NUMBER_TIME_TICKET_COLUMN + " INTEGER NOT NULL, " +
                    NUMBER_RING_TICKET_COLUMN + " INTEGER NOT NULL, " +
                    SLEEP_TIME_COLUMN + " INTEGER NOT NULL, " +
                    ALL_SET_COLUMN + " INTEGER NOT NULL)";


    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public Parameter_DBSet(Context context) {
        db = ParameterDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public Parameter insert(Parameter item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        if(item.isVibratable()) cv.put(VIBRATABLE_COLUMN, 1);
        else cv.put(VIBRATABLE_COLUMN, 0);
        cv.put(MONEY_COLUMN, item.getMoney());
        cv.put(NUMBER_TIME_TICKET_COLUMN, item.getNumberTimeTicket());
        cv.put(NUMBER_RING_TICKET_COLUMN, item.getNumberRingTicket());
        cv.put(SLEEP_TIME_COLUMN, item.getSleepTime());
        if(item.isAllSet()) cv.put(ALL_SET_COLUMN, 1);
        else cv.put(ALL_SET_COLUMN, 0);

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
    public boolean update(Parameter item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        if(item.isVibratable()) cv.put(VIBRATABLE_COLUMN, 1);
        else cv.put(VIBRATABLE_COLUMN, 0);
        cv.put(MONEY_COLUMN, item.getMoney());
        cv.put(NUMBER_TIME_TICKET_COLUMN, item.getNumberTimeTicket());
        cv.put(NUMBER_RING_TICKET_COLUMN, item.getNumberRingTicket());
        cv.put(SLEEP_TIME_COLUMN, item.getSleepTime());
        if(item.isAllSet()) cv.put(ALL_SET_COLUMN, 1);
        else cv.put(ALL_SET_COLUMN, 0);

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
    public ArrayList<Parameter> getAll() {
        ArrayList<Parameter> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public Parameter get(long id) {
        // 準備回傳結果用的物件
        Parameter item = null;
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
    public Parameter getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Parameter result = new Parameter(0, 0, 0);

        result.setId(cursor.getLong(0));
        if(cursor.getInt(1) == 1) result.setVibratable(true);
        else result.setVibratable(false);
        result.setMoney(cursor.getInt(2));
        result.setNumberTimeTicket(cursor.getInt(3));
        result.setNumberRingTicket(cursor.getInt(4));
        result.setSleepTime(cursor.getLong(5));
        if(cursor.getInt(6) == 1) result.setAllSet(true);
        else result.setAllSet(false);


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
