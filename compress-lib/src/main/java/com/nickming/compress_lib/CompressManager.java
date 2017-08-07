package com.nickming.compress_lib;

import com.nickming.compress_lib.core.BaseArchiveStrategy;
import com.nickming.compress_lib.core.IArchiveListener;
import com.nickming.compress_lib.core.RarStrategy;
import com.nickming.compress_lib.core.SevenZipStrategy;
import com.nickming.compress_lib.core.ZipStrategy;
import com.nickming.compress_lib.util.FileUtil;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * class description here
 *
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-06 下午4:39
 * Copyright (c) 2017 nickming All right reserved.
 */

public class CompressManager {

    private Executor mThreadPool;

    private RarStrategy mRarStrategy;
    private SevenZipStrategy mSevenZipStrategy;
    private ZipStrategy mZipStrategy;

    private static class InstanceHolder {
        public static CompressManager INSTANCE = new CompressManager();
    }

    public static CompressManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private CompressManager() {
        mThreadPool = Executors.newSingleThreadExecutor();
        mRarStrategy = new RarStrategy();
        mSevenZipStrategy = new SevenZipStrategy();
        mZipStrategy = new ZipStrategy();
    }

    public void unCompressFile(final String filePath, final String outPath, final IArchiveListener listener) {
        File file = new File(filePath);
        final BaseArchiveStrategy baseArchiveStrategy = getSuitableStrategy(file);
        if (baseArchiveStrategy == null) {
            if (listener != null) {
                listener.onError("不支持该格式文件");
            }
            return;
        } else {
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    baseArchiveStrategy.unCompressFile(filePath, outPath, listener);
                }
            });
        }
    }

    private BaseArchiveStrategy getSuitableStrategy(File file) {
        String extension = FileUtil.getFileExtension(file);
        switch (extension) {
            case SupportType._7Z:
                return mSevenZipStrategy;
            case SupportType._RAR:
                return mRarStrategy;
            case SupportType._ZIP:
                return mZipStrategy;
            default:
                return null;
        }
    }

    public static class SupportType {
        public final static String _RAR = "rar";
        public final static String _ZIP = "zip";
        public final static String _7Z = "7z";
    }

}
