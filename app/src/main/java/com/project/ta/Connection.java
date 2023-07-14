package com.project.ta;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Connection {
    private static Connection vInstance;
    private RequestQueue requestQueue;
    private static Context vCtx;

    private Connection (Context context) {
        vCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(vCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized Connection getInstance(Context context) {
        if (vInstance == null) {
            vInstance = new Connection(context);
        }
        return vInstance;
    }

    public<T> void addToRequestQue (Request<T> request) {
        getRequestQueue().add(request);
    }
}
