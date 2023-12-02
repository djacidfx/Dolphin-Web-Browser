package com.codebyte.dolphinwebbrowser.VdstudioAppDatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.codebyte.dolphinwebbrowser.VdstudioAppModel.BookmarkData;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.HistoryData;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String COLUMN_BOOKMARK_ID = "bookmark_id";
    private static final String COLUMN_BOOKMARK_IMAGE = "bookmark_image";
    private static final String COLUMN_BOOKMARK_NAME = "bookmark_name";
    private static final String COLUMN_BOOKMARK_URL = "bookmark_url";
    private static final String COLUMN_HISTORY_DATE = "history_date";
    private static final String COLUMN_HISTORY_ID = "history_id";
    private static final String COLUMN_HISTORY_IMAGE = "history_image";
    private static final String COLUMN_HISTORY_NAME = "history_name";
    private static final String COLUMN_HISTORY_URL = "history_url";
    private static final String DATABASE_NAME = "light_browser.db";
    private static final String TABLE_BOOKMARK = "Bookmark";
    private static final String TABLE_HISTORY = "History";
    private final Context context;
    private String createBookmarkTable = "CREATE TABLE Bookmark ( bookmark_id INTEGER PRIMARY KEY AUTOINCREMENT, bookmark_name TEXT, bookmark_url TEXT, bookmark_image TEXT  ) ";
    private String createHistoryTable = "CREATE TABLE History ( history_id INTEGER PRIMARY KEY AUTOINCREMENT, history_name TEXT, history_url TEXT, history_image TEXT, history_date TEXT  ) ";
    private String dropBookmarkTable = "DROP TABLE IF EXISTS Bookmark";
    private String dropHistoryTable = "DROP TABLE IF EXISTS History";

    public Database(Context context2) {
        super(context2, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.context = context2;
    }

    public static void copyFile(FileInputStream fileInputStream, FileOutputStream fileOutputStream) throws IOException {
        FileChannel fileChannel;
        try {
            FileChannel channel = fileInputStream.getChannel();
            try {
                fileChannel = fileOutputStream.getChannel();
                try {
                    channel.transferTo(0, channel.size(), fileChannel);
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (Throwable th) {
                            if (fileChannel != null) {
                                fileChannel.close();
                            }
                            throw th;
                        }
                    }
                    if (fileChannel != null) {
                        fileChannel.close();
                    }
                } catch (Throwable unused) {
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (Throwable th2) {
                            if (fileChannel != null) {
                                fileChannel.close();
                            }
                            throw th2;
                        }
                    }
                    if (fileChannel == null) {
                        fileChannel.close();
                    }
                }
            } catch (Throwable unused2) {
                fileChannel = null;
                if (channel != null) {
                }
                if (fileChannel == null) {
                }
            }
        } catch (Throwable unused3) {
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(this.createBookmarkTable);
        sQLiteDatabase.execSQL(this.createHistoryTable);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL(this.dropBookmarkTable);
        sQLiteDatabase.execSQL(this.dropHistoryTable);
        onCreate(sQLiteDatabase);
    }

    public void exportDB() {
        String downloadPath = MyApplication.getDownloadPath();
        if (!new File(downloadPath).exists()) {
            new File(downloadPath).mkdirs();
        }
        File dataDirectory = Environment.getDataDirectory();
        File file = new File(dataDirectory, "/data/" + this.context.getPackageName() + "/databases/" + DATABASE_NAME);
        File file2 = new File(new File(downloadPath), DATABASE_NAME);
        try {
            FileChannel channel = new FileInputStream(file).getChannel();
            FileChannel channel2 = new FileOutputStream(file2).getChannel();
            channel2.transferFrom(channel, 0, channel.size());
            channel.close();
            channel2.close();
            Toast.makeText(this.context, "Exported successfully...!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean importDatabase(String str) throws IOException {
        close();
        File file = new File(str);
        File file2 = new File(Environment.getDataDirectory(), "/data/" + this.context.getPackageName() + "/databases/" + DATABASE_NAME);
        if (!file.exists()) {
            return false;
        }
        copyFile(new FileInputStream(file), new FileOutputStream(file2));
        getWritableDatabase().close();
        return true;
    }

    public void addBookmark(BookmarkData bookmark_Data) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BOOKMARK_NAME, bookmark_Data.getName());
        contentValues.put(COLUMN_BOOKMARK_URL, bookmark_Data.getUrl());
        contentValues.put(COLUMN_BOOKMARK_IMAGE, bookmark_Data.getImage());
        writableDatabase.insert(TABLE_BOOKMARK, null, contentValues);
        writableDatabase.close();
    }

    public void addHistory(HistoryData history_Data) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_HISTORY_NAME, history_Data.getName());
        contentValues.put(COLUMN_HISTORY_URL, history_Data.getUrl());
        contentValues.put(COLUMN_HISTORY_IMAGE, history_Data.getImage());
        contentValues.put(COLUMN_HISTORY_DATE, history_Data.getDate());
        writableDatabase.insert(TABLE_HISTORY, null, contentValues);
        writableDatabase.close();
    }

    @SuppressLint("Range")
    public List<BookmarkData> getAllBookmark() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select * from Bookmark", null);
        if (rawQuery.moveToFirst()) {
            do {
                BookmarkData bookmark_Data = new BookmarkData();
                bookmark_Data.setId(rawQuery.getString(rawQuery.getColumnIndex(COLUMN_BOOKMARK_ID)));
                bookmark_Data.setName(rawQuery.getString(rawQuery.getColumnIndex(COLUMN_BOOKMARK_NAME)));
                bookmark_Data.setUrl(rawQuery.getString(rawQuery.getColumnIndex(COLUMN_BOOKMARK_URL)));
                bookmark_Data.setImage(rawQuery.getString(rawQuery.getColumnIndex(COLUMN_BOOKMARK_IMAGE)));
                arrayList.add(bookmark_Data);
            } while (rawQuery.moveToNext());
        }
        rawQuery.close();
        readableDatabase.close();
        return arrayList;
    }

    @SuppressLint("Range")
    public List<HistoryData> getAllHistory() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select * from History", null);
        if (rawQuery.moveToFirst()) {
            do {
                HistoryData history_Data = new HistoryData();
                history_Data.setId(rawQuery.getString(rawQuery.getColumnIndex(COLUMN_HISTORY_ID)));
                history_Data.setName(rawQuery.getString(rawQuery.getColumnIndex(COLUMN_HISTORY_NAME)));
                history_Data.setUrl(rawQuery.getString(rawQuery.getColumnIndex(COLUMN_HISTORY_URL)));
                history_Data.setImage(rawQuery.getString(rawQuery.getColumnIndex(COLUMN_HISTORY_IMAGE)));
                history_Data.setDate(rawQuery.getString(rawQuery.getColumnIndex(COLUMN_HISTORY_DATE)));
                arrayList.add(history_Data);
            } while (rawQuery.moveToNext());
        }
        rawQuery.close();
        readableDatabase.close();
        return arrayList;
    }

    public Integer deleteBookmark(String str) {
        return Integer.valueOf(getWritableDatabase().delete(TABLE_BOOKMARK, "bookmark_id = ?", new String[]{str}));
    }

    public Integer deleteHistory(String str) {
        return Integer.valueOf(getWritableDatabase().delete(TABLE_HISTORY, "history_id = ?", new String[]{str}));
    }

    public boolean updateBookmark(BookmarkData bookmark_Data) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BOOKMARK_NAME, bookmark_Data.getName());
        contentValues.put(COLUMN_BOOKMARK_URL, bookmark_Data.getUrl());
        contentValues.put(COLUMN_BOOKMARK_IMAGE, bookmark_Data.getImage());
        writableDatabase.update(TABLE_BOOKMARK, contentValues, "bookmark_id = ?", new String[]{bookmark_Data.getId()});
        return true;
    }

    public boolean deleteBookmarkTable() {
        try {
            getWritableDatabase().execSQL("delete from Bookmark");
            return true;
        } catch (Exception unused) {
            return true;
        }
    }

    public boolean deleteHistoryTable() {
        try {
            getWritableDatabase().execSQL("delete from History");
            return true;
        } catch (Exception unused) {
            return true;
        }
    }
}
