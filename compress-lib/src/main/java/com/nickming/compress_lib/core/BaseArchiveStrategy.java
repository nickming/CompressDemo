package com.nickming.compress_lib.core;

import android.os.Handler;
import android.os.Looper;

import com.nickming.compress_lib.util.FileUtil;

import java.io.File;

/**
 * class description here
 *
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-06 下午4:45
 * Copyright (c) 2017 nickming All right reserved.
 */

public abstract class BaseArchiveStrategy {

    protected Handler mHandler;

    public BaseArchiveStrategy() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public abstract void compressFile(File[] files, String outPath);

    public abstract void unCompressFile(String filePath, String outPath, IArchiveListener listener);

    public abstract String getFileExtension();

    protected void notifyStart(final IArchiveListener listener) {
        if (null != listener) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onStart();
                }
            });
        }
    }

    protected void notifyProgress(final IArchiveListener listener, final int progress) {
        if (null != listener) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onProgress(progress);
                }
            });
        }
    }

    protected void notifyCompleted(final IArchiveListener listener, final String outPath) {
        if (null != listener) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onCompleted(outPath);
                }
            });
        }
    }


    protected void notifyError(final IArchiveListener listener, final String msg) {
        if (null != listener) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onError(msg);
                }
            });
        }
    }

    protected boolean checkFileState(String filePath, String outPath, IArchiveListener listener) {
        File file = new File(filePath);
        if (file == null && !file.exists()) {
            notifyError(listener, "路径为空!");
            return false;
        }
        notifyStart(listener);
        return FileUtil.createDirOrExists(outPath);
    }

}
