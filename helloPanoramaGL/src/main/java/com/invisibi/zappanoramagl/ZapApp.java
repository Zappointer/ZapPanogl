package com.invisibi.zappanoramagl;

import android.content.Context;

/**
 * Created by jackf on 2014/5/26.
 */
public class ZapApp {

    private static ZapApp instance = new ZapApp();

    Context mContext;

    private ZapApp(){
    };

    public static ZapApp getInstance() {
        return instance;
    }

    public void SetContext(Context context) {
        mContext = context;
    }

    public Context GetContext() {
        return mContext;
    }


}
