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

//        String fixCoookie =  "ig_did=53380998-7484-49A3-B7A5-5D364E51FAFE; mid=XnIvqAALAAGLJJN8tgwgC2EkMTmC; datr=xzdyXoVeMgv8GIBq_y5z882F; shbid=14796; fbm_124024574287414=base_domain=.instagram.com; csrftoken=fVrRqyWzRRHKo7N9UwDEdbYjpGDyLAoR; ds_user_id=23865900368; sessionid=23865900368%3AUanZhQjmvNiywv%3A12; shbts=1588647877.6656075; urlgen=\"{\\\"183.81.122.179\\\": 18403}:1jWiBG:iaVe5DliNLqUhl3C8sTAvIQZjHY\"";
//        return fixCoookie;
    }

    public static String getUserIdInstagram(){
        String cookie = getCookieInstagram();

        if (cookie != null && !cookie.contains("ds_user_id")){
            return null;
        }

        String id = cookie.substring(cookie.indexOf("ds_user_id=") + 11);
        id = id.substring(0, id.indexOf(";"));
        return id;
    }
}
