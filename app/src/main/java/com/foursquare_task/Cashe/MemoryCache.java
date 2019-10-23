package com.foursquare_task.Cashe;


import android.graphics.Bitmap;
import android.util.Log;


public class MemoryCache implements  ICasheJson
{


    @Override
    public void saveJsonToCashe(Object json, String request) {
        Log.e("QP","Memory cashe save : "+request);
        Cache.getInstance().getCashe().put(request,json);
    }

    @Override
    public Object getJsonFromCashe(String request) {
        Log.e("QP","Memory cashe get : "+request);
        Object object = null;
        object = (Object) Cache.getInstance().getCashe().get(request);
        return object;
    }
} // class of MemoryCache
