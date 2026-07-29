package com.mobilerpgpack.phone.utils.storage;

import android.annotation.TargetApi;
import android.os.Build;

// Document provider: /sdcard/Android/data/<package_name>/files
@TargetApi(Build.VERSION_CODES.KITKAT)
public class AndroidDataDocumentsProvider extends HarmDocumentsProvider
{
    @Override
    protected String GetPath()
    {
        var context = getContext();
        if (context == null){
            return "";
        }
        var externalsFilesDIr = context.getExternalFilesDir(null);
        return externalsFilesDIr !=null ? externalsFilesDIr.getAbsolutePath() : "";
    }

    @Override
    protected String GetName()
    {
        return "Classic Core External storage";
    }
}
