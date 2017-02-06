/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package helper;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by RTsoftBD_Siddiqui on 2017-02-06.
 */

public class ConnectionDetect {

    private Context context;

    public ConnectionDetect(Context context) {
        this.context = context;
    }

    public boolean isConnected(){
        return ((ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
