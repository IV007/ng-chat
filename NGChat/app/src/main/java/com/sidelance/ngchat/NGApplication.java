package com.sidelance.ngchat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Ivan on 10/3/2015.
 */
public class NGApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "yeaMOvq7xvSfLqIt9R8O2pDrd0nhjyh2psYfYQp9", "UbXid8LggsL9ttvzF2WQ3xiqRPvGxutaBJ4Ca5Mh");

    }
}
