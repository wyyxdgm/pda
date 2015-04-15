package com.skyworth.skyclientcenter.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skyworth.skyclientcenter.history.HistoryData;
import com.skyworth.webSDK.webservice.resource.EpgChannel;

public class SkyDatabase
{
    public static final String DB_CREATE_HISTORY = "" + "CREATE TABLE IF NOT EXISTS history_info "
            + "(_id integer primary key autoincrement, " + "webUrl String unique not null," + // 播放的url
            "title String not null," + // 播放的名称
            "currentTime int," + // 播放到的时间
            "Duration int," + // 总时间
            "count int," + // 播放到那一集
            "source String," + // 播放到那一集
            "date String" + ")";

    public static final String DB_CREATE_COLLECT = "CREATE TABLE IF NOT EXISTS collect_data " + "("
            + "_id integer primary key autoincrement," + "channel_id String unique not null,"
            + "channel_name String," + "channel_icon String," + "channel_play String,"
            + "pg_name String," + "begin_time String," + "collect_date" + ")";

    public static final String DB_NAME = "SkyMobile.db2";
    public static final int DB_VERSION = 1;

    private Context mContext = null;

    public SkyDatabase(Context ctx)
    {
        mContext = ctx;
    }

    /**
     * 获取收藏
     */
    public List<EpgChannel> getOldCollectData()
    {
        List<EpgChannel> list = new ArrayList<EpgChannel>();

        SQLiteDatabase db = open();
        Cursor cursor = db.query("collect_data", null, null, null, null, null, null);

        while (cursor.moveToNext())
        {
            String id = cursor.getString(cursor.getColumnIndex("channel_id"));
            String name = cursor.getString(cursor.getColumnIndex("channel_name"));
            String icon = cursor.getString(cursor.getColumnIndex("channel_icon"));
            String url = cursor.getString(cursor.getColumnIndex("channel_play"));
            String pg_name = cursor.getString(cursor.getColumnIndex("pg_name"));
            String begin_time = cursor.getString(cursor.getColumnIndex("begin_time"));
            EpgChannel epgChannel = new EpgChannel();
            epgChannel.ch_id = id;
            epgChannel.ch_name = name;
            epgChannel.ch_img = icon;
            epgChannel.ch_url = url;
            epgChannel.pg_name = pg_name;
            epgChannel.start_time = begin_time;
            list.add(epgChannel);
        }

        cursor.close();
        db.close();
        return list;
    }

    /**
     * 获取历史记录
     */
    public List<HistoryData> getOldHistoryData()
    {
        List<HistoryData> list = new ArrayList<HistoryData>();

        SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery("select * from history_info order by date desc", null);

        while (cursor.moveToNext())
        {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String url = cursor.getString(cursor.getColumnIndex("webUrl"));
            String source = cursor.getString(cursor.getColumnIndex("source"));
            HistoryData historyData = new HistoryData();
            historyData.setUrl(url);
            historyData.setTitle(title);
            historyData.setDate(date);
            historyData.setSource(source);
            list.add(historyData);
        }

        cursor.close();
        db.close();
        return list;
    }

    private SkyDBHelper mDBHelper = null;

    //
    // 打开数据库
    public SQLiteDatabase open()
    {
        mDBHelper = new SkyDBHelper(mContext);
        return mDBHelper.getWritableDatabase();
    }

    public void close()
    {
        mDBHelper.close();
    }

    public static class SkyDBHelper extends SQLiteOpenHelper
    {

        public SkyDBHelper(Context context)
        {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DB_CREATE_HISTORY);
            db.execSQL(DB_CREATE_COLLECT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        }

    }

}
