package uaes.bda.antianchor.demo.application;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static Context getContext(){
        return  mContext;
    }

}

