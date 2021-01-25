package com.phuture.instant.services.sync.volley;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Request class based on StringRequest to pass 'Tag' to the response listener
 *
 */
public class VolleyRequest extends Request<String> {
    /** Lock to guard mListener as it is cleared on cancel() and read on delivery. */
    private final Object mLock = new Object();

    @Nullable
    @GuardedBy("mLock")
    protected VolleyResponseListener listener;

    public VolleyRequest(String url, @NonNull VolleyResponseListener listener) {
        super(Method.GET, url, null);
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0");
        return headers;
    }

    @Override
    protected void deliverResponse(String response) {
        VolleyResponseListener listener;
        synchronized (mLock) {
            listener = this.listener;
        }
        if (listener!=null) {
            listener.onResponse(getTag(), response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        // Otherwise dispatch the error
        VolleyResponseListener listener;
        synchronized (mLock) {
            listener = this.listener;
        }
        if (listener!=null) {
            listener.onErrorResponse(getTag(), error);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        synchronized (mLock) {
            listener = null;
        }
    }

    @Override
    @SuppressWarnings("DefaultCharset")
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            // Since minSdkVersion = 8, we can't call
            // new String(response.data, Charset.defaultCharset())
            // So suppress the warning instead.
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

}
