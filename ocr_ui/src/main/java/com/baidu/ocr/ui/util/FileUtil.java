/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ocr.ui.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {

    public static File getSaveFile(Context context) {



        File file = new File(context.getFilesDir(), 1 + "pic.jpg");
//        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }


}
