package com.nickming.compress_lib.core;

import com.hzy.lib7z.Un7Zip;

import java.io.File;

/**
 * class description here
 *
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-06 下午5:40
 * Copyright (c) 2017 nickming All right reserved.
 */

public class ZipStrategy extends BaseArchiveStrategy{

    @Override
    void compressFile(File[] files, String outPath) {

    }

    @Override
    void unCompressFile(String filePath, String outPath, IArchiveListener listener) {
        try {
            notifyStart(listener);
            if (!checkFileState(filePath, outPath, listener)) {
                return;
            }
            notifyProgress(listener, 0);
            boolean result = Un7Zip.extract7z(filePath, outPath);
            if (result) {
                notifyProgress(listener, 100);
                notifyCompleted(listener, outPath);
            } else {
                notifyError(listener, "解压7Z失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
