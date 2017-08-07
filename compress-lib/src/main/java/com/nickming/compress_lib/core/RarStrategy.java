package com.nickming.compress_lib.core;

import java.io.File;
import java.io.FileOutputStream;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

/**
 * 作者： Nickming
 * 功能：
 * 日期：2017/8/7
 */

public class RarStrategy extends BaseArchiveStrategy {


    @Override
    public void compressFile(File[] files, String outPath) {

    }

    /**
     * 默认实现不带密码的解压方式，带密码的后续实现
     *
     * @param filePath
     * @param outPath
     * @param listener
     */
    @Override
    public void unCompressFile(String filePath, String outPath, IArchiveListener listener) {
        try {
            if (!checkFileState(filePath, outPath, listener)) {
                return;
            }

            FileOutputStream fileOutputStream = null;
            Archive archive = new Archive(new File(filePath));
            FileHeader fh = null;
            final int total = archive.getFileHeaders().size();
            fh = archive.nextFileHeader();
            int index = 0;
            while (fh != null) {
                String entrypath = "";
                if (fh.isUnicode()) {//解決中文乱码
                    entrypath = fh.getFileNameW().trim();
                } else {
                    entrypath = fh.getFileNameString().trim();
                }
                entrypath = entrypath.replaceAll("\\\\", "/");

                File file = new File(outPath + entrypath);

                if (fh.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(file);
                    archive.extractFile(fh, fileOutputStream);
                    fileOutputStream.close();
                    int progress = (index + 1) * 100 / total;
                    index++;
                    notifyProgress(listener, progress);
                }
                fh = archive.nextFileHeader();
            }
            notifyCompleted(listener, outPath);
            fileOutputStream.close();
            archive.close();


//                //根据不同的文件系统获取不同的文件地址和文件名
//                String destFileName = "";
//                String destDirName = "";
//                if (File.separator.equals("/")) {
//                    destFileName = (outPath + fh.getFileNameW().trim()).replaceAll("\\\\", "/");
//                    destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
//                } else {
//                    destFileName = (outPath + fh.getFileNameW().trim()).replaceAll("/", "\\\\");
//                    destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
//                }
//                //根据文件名来处理解压
//                File dir = new File(destDirName);
//                FileUtil.createDirOrExists(dir);
//                if (FileUtil.createDirOrExists(destFileName)) {
//                    new File(des).mkdirs();
//                } else {
//                    fileOutputStream = new FileOutputStream(new File(destFileName));
//                    archive.extractFile(fh, fileOutputStream);
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                    //通知进度更新
//                    int progress = (i + 1) * 100 / total;
//                    notifyProgress(listener, progress);
//                }

        } catch (Exception e) {
            e.printStackTrace();
            notifyError(listener, "解压失败:" + e.getMessage().trim());
        }
    }

    @Override
    public String getFileExtension() {
        return "rar";
    }
}
