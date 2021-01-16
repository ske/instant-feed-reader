package com.phuture.instant.services.sync.volley;

import com.android.volley.VolleyError;

public interface VolleyResponseListener {
    void onResponse(Object tag, String response);
    void onErrorResponse(Object tag, VolleyError error);
}
