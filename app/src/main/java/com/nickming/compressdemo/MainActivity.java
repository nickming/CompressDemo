package com.nickming.compressdemo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nickming.compress_lib.CompressManager;
import com.nickming.compress_lib.core.IArchiveListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String DEST_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/compress/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button= (Button) findViewById(R.id.unCompressZipBtnId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/test/testZ.zip";
                unCompressFile(path);
            }
        });

        findViewById(R.id.unCompressRarBtnId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/test/testR.rar";
                unCompressFile(path);
            }
        });

        findViewById(R.id.unCompress7zBtnId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/test/test7.7z";
                unCompressFile(path);
            }
        });
    }

    private void unCompressFile(final String path) {
        CompressManager.getInstance().unCompressFile(path, DEST_PATH, new IArchiveListener() {
            @Override
            public void onProgress(int progress) {
                Log.i(TAG, "onProgress: "+progress);
            }

            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 开始解压:"+path);
            }

            @Override
            public void onCompleted(String outPath) {
                Log.i(TAG, "onCompleted: 解压完成:"+outPath);
            }

            @Override
            public void onError(String msg) {
                Log.i(TAG, "onError: 解压失败:"+msg);
            }
        });
    }
}
