package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.codebyte.dolphinwebbrowser.VdstudioAppDatabase.Database;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.IOException;

import dolphinwebbrowser.R;

public class BookmarkSettings extends AppCompatActivity {
    public Database database;
    private Toolbar bookmarkTool;
    private TextView delete;
    private TextView exportBackup;
    private TextView importBackup;

    public static String getPathFromUri(Context context, Uri uri) {
        Uri uri2 = null;
        if (Build.VERSION.SDK_INT < 19 || !DocumentsContract.isDocumentUri(context, uri)) {
            if (!FirebaseAnalytics.Param.CONTENT.equalsIgnoreCase(uri.getScheme())) {
                if ("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            } else if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            } else {
                return getDataColumn(context, uri, null, null);
            }
        } else if (isExternalStorageDocument(uri)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + split[1];
            }
        } else if (isDownloadsDocument(uri)) {
            return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
        } else {
            if (isMediaDocument(uri)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                String str = split2[0];
                if ("image".equals(str)) {
                    uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(str)) {
                    uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(str)) {
                    uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
            }
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        Cursor cursor;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                    if (cursor != null) {
                        cursor.close();
                    }
                    return string;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (Throwable unused) {
            cursor = null;
            if (cursor != null) {
                cursor.close();
            }
            return null;
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_bookmark);
        this.database = new Database(this);
        initView();
        initListener();
    }

    private void initListener() {
        this.exportBackup.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                BookmarkSettings.this.database.exportDB();
            }
        });
        this.importBackup.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(BookmarkSettings.this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("*/*");
                    BookmarkSettings.this.startActivityForResult(intent, 100);
                    return;
                }
                ActivityCompat.requestPermissions(BookmarkSettings.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 102);
            }
        });
        this.delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new AlertDialog.Builder(BookmarkSettings.this).setTitle("Delete").setMessage("Do you want to delete all bookmark").setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        BookmarkSettings.this.database.deleteBookmarkTable();
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });
        this.bookmarkTool.setNavigationOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                BookmarkSettings.this.onBackPressed();
            }
        });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.bookmark_tool);
        this.bookmarkTool = toolbar;
        toolbar.setTitle("Bookmark Settings");
        this.bookmarkTool.setNavigationIcon(getResources().getDrawable(R.drawable.iv_back));
        this.exportBackup = (TextView) findViewById(R.id.export_backup);
        this.importBackup = (TextView) findViewById(R.id.import_backup);
        this.delete = (TextView) findViewById(R.id.delete);
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 102) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("*/*");
            startActivityForResult(intent, 100);
            return;
        }
        Toast.makeText(this, "Allow this permission...!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 100 && Build.VERSION.SDK_INT >= 19 && intent != null) {
            File file = new File(getPathFromUri(this, intent.getData()));
            if (!file.exists()) {
                Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
            } else if (file.getName().equals("light_browser.db")) {
                try {
                    if (this.database.importDatabase(file.getPath())) {
                        Toast.makeText(this, "Imported successfully...!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "not import this .db file", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Select light_browser.db file for your device", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
