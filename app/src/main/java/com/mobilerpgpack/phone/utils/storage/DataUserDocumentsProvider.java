package com.mobilerpgpack.phone.utils.storage;

import android.annotation.TargetApi;
import android.os.Build;

// Document provider: /data/user/<user>
@TargetApi(Build.VERSION_CODES.KITKAT)
public class DataUserDocumentsProvider extends HarmDocumentsProvider
{
    @Override
    protected String GetPath()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return getContext().getDataDir().getAbsolutePath();
        }
        else
        {
            return getContext().getCacheDir().getAbsolutePath();
        }
    }

    @Override
    protected String GetName()
    {
        return "Classic Core Internal storage";
    }
}
