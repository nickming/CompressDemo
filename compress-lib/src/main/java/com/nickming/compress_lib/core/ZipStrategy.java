package com.nickming.compress_lib.core;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

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
    public void compressFile(File[] files, String outPath) {

    }

    @Override
    public void unCompressFile(String filePath, String outPath, IArchiveListener listener) {
        try {
            if (!checkFileState(filePath, outPath, listener)) {
                return;
            }

            ZipFile zipFile=new ZipFile(filePath);
            zipFile.setFileNameCharset("GBK");
            if (!zipFile.isValidZipFile()){
                notifyError(listener,"该文件不是合法的zip文件");
                return;
            }
            if (zipFile.isEncrypted()){
                zipFile.setPassword("");
            }

            int total=zipFile.getFileHeaders().size();
            for (int i = 0; i < total; i++) {
                FileHeader fileHeader= (FileHeader) zipFile.getFileHeaders().get(i);
                zipFile.extractFile(fileHeader,outPath);
                int progress=(i+1)*100/total;
                notifyProgress(listener,progress);
            }

            notifyCompleted(listener,outPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFileExtension() {
        return "zip";
    }
}
