package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codebyte.dolphinwebbrowser.VdstudioAppAdapter.DownloadAdapter;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.FileData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import dolphinwebbrowser.R;

public class DownloadActivity extends AppCompatActivity {
    private RecyclerView downloadRecycler;
    private Toolbar downloadTool;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_download);
        initView();
        initData();
    }

    private void initData() {
        ArrayList<FileData> arrayList = new ArrayList<>();
        arrayList.clear();
        File[] listFiles = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()).listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isFile()) {
                    File file2 = new File(file.getPath());
                    FileData fileData = new FileData();
                    fileData.setName(file2.getName());
                    fileData.setPath(file2.getPath());
                    fileData.setParent(file2.getParent());
                    fileData.setModified(String.valueOf(file2.lastModified()));
                    if (Build.VERSION.SDK_INT >= 26) {
                        try {
                            fileData.setCreation(String.valueOf(Files.readAttributes(Paths.get(file2.getPath(), new String[0]), BasicFileAttributes.class, new LinkOption[0]).creationTime()));
                        } catch (IOException e) {
                            fileData.setCreation(String.valueOf(file2.lastModified()));
                            e.printStackTrace();
                        }
                    } else {
                        fileData.setCreation(String.valueOf(file2.lastModified()));
                    }
                    if (!file2.getName().contains(".db")) {
                        arrayList.add(fileData);
                    }
                }
            }
        }
        initAdapter(arrayList);
    }

    private void initAdapter(ArrayList<FileData> arrayList) {
        DownloadAdapter download_Adapter = new DownloadAdapter(this, arrayList);
        this.downloadRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        this.downloadRecycler.setAdapter(download_Adapter);
    }

    private void initView() {
        this.downloadTool = (Toolbar) findViewById(R.id.download_tool);
        this.downloadRecycler = (RecyclerView) findViewById(R.id.download_recycler);
        this.downloadTool.setTitle("Downloads");
        this.downloadTool.setNavigationIcon(getResources().getDrawable(R.drawable.iv_back));
        this.downloadTool.setNavigationOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                DownloadActivity.this.onBackPressed();
            }
        });
    }
}
