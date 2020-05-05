package com.example.instagram;

import android.webkit.CookieManager;

public class Utils {
    public static String getCookieInstagram(){
        String cookie = CookieManager.getInstance().getCookie(LinkUrlApi.URL_INSTAGRAM);

        if (cookie != null && !cookie.contains("ds_user_id")){
            return null;
        }else {
            return cookie;
        }
    }

    public static String getUserIdInstagram(){
        String cookie = CookieManager.getInstance().getCookie(LinkUrlApi.URL_INSTAGRAM);

        if (cookie != null && !cookie.contains("ds_user_id")){
            return null;
        }

        String id = cookie.substring(cookie.indexOf("ds_user_id=") + 11);
        id = id.substring(0, id.indexOf(";"));
        return id;
    }
}
