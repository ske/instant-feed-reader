package com.phuture.instant.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Downloader {

    protected String url;
    protected Context ctx;
    protected String responseString;
    protected Object data;

    public Downloader(Context ctx, String url, Object data) {
        this.url = url;
        this.ctx = ctx;
        this.data = data;
    }

    public void get(final IDownloadResult handler) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        Request<String> request = new StringRequest(this.url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    handler.onResultReceived(response, data);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Request failed: " + error.getMessage() + ", " + error.toString());
                    handler.onResultReceived(null, data);
                }
            }
        );

        queue.add(request);
    }

    public static void execute(final Context ctx, final String url, Object data, final IDownloadResult handler) {
        new Thread() {
            @Override
            public void run() {
                Downloader dl = new Downloader(ctx, url, data);
                dl.get(new IDownloadResult() {
                    @Override
                    public void onResultReceived(String result, Object data) {
                        handler.onResultReceived(result, data);
                    }
                });
            }
        }.start();
    }
}
