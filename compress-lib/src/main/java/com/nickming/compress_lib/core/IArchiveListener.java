package com.nickming.compress_lib.core;

/**
 * class description here
 *
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-06 下午5:18
 * Copyright (c) 2017 nickming All right reserved.
 */

public interface IArchiveListener {

    void onProgress(int progress);

    void onStart();

    void onCompleted(String outPath);

    void onError(String msg);
}
