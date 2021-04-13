
package com.mediabuttonhandling.reactnative;

import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RNMediaButtonHandlingModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private ReactContext reactContext;
    private DeviceEventManagerModule.RCTDeviceEventEmitter mJSModule;
    private static MediaButtonHandlingModule instance;

    public static RNMediaButtonHandlingModule initMediaButtonHandlingEvent(ReactApplicationContext reactContext){
        instance = new RNMediaButtonHandlingModule(reactContext);
        return instance;
    }

    protected RNMediaButtonHandlingModule (ReactApplicationContext mReactApplicationContext){
        super(mReactApplicationContext);
        reactContext = mReactApplicationContext;
    }
    //Event Emitter
    public void onMediaButtonEvent(int keyCode, KeyEvent keyEvent){
        if(!reactContext.hasActiveCatalystInstance())
            return;
        if (mJSModule == null){
            mJSModule = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
        }
        mJSModule.emit("onMediaButtonEvent", getJsEventParams(keyCode, keyEvent));
    }

    //Bundles the basic parameters
    private WritableMap getJsEventParams(int keyCode, KeyEvent keyEvent){
        WritableMap params = new WritableNativeMap();
        int action = keyEvent.getAction();
        char pressedKey = (char) keyEvent.getUnicodeChar();
        params.putInt("keyCode", keyCode);
        params.putInt("action", action);
        params.putString("pressedKey", String.valueOf(pressedKey));
        return params;
    }

    //Returns instance to the Package Module
    public static RNMediaButtonHandlingModule getInstance(){
        return instance;
    }

    @ReactMethod
    public void logFunction(String msg){
        String TAG = "MediaButtonModule";
        Log.d(TAG, msg);
    }    

    @Override
    public String getName() {
      return "MediaButtonModule";
    }
}