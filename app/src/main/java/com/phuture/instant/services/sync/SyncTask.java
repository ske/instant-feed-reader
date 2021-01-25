package com.phuture.instant.services.sync;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.phuture.instant.db.Client;
import com.phuture.instant.model.Article;
import com.phuture.instant.model.Source;
import com.phuture.instant.services.sync.volley.VolleyRequest;
import com.phuture.instant.services.sync.volley.VolleyResponseListener;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SyncTask implements ISyncTask, VolleyResponseListener {

    private ISyncResult result;
    private ISyncTaskParams params;
    private ISyncTaskResultHandler handler;
    private Context ctx;
    private RequestQueue queue;

    private Map<Source, Object> status;
    private int activeRequests = -1;

    public SyncTask(Context ctx) {
        this.ctx = ctx;
        status = new LinkedHashMap<Source, Object>();
    }

    @Override
    public ISyncResult getResult() {
        return result;
    }

    @Override
    public ISyncTask setParams(ISyncTaskParams params) {
        this.params = params;
        return this;
    }

    @Override
    public ISyncTask setHandler(ISyncTaskResultHandler handler) {
        this.handler = handler;
        return this;
    }

    private void fetchCompleted() {
        final Map<Source, Object> status = this.status;

        result = new ISyncResult() {
            @Override
            public boolean isSuccess() {
                return true;
            }

            @Override
            public Object getData() {
                return status;
            }
        };
        notifyHandler();
    }

    private void notifyHandler() {
        if (this.handler != null) {
            this.handler.onResult(result);
        }
    }

    private void fetchSources() {
        queue = Volley.newRequestQueue(ctx);
        // Add request to the queue

        List<String> sourceIds = params.getSourceIdList();
        Collections.sort(sourceIds);

        for (String sourceId: sourceIds) {
            Source source = Client.instance(ctx).getDb().sourceDao().get(sourceId);
            if (source == null) continue;

            // Pre-init response data
            status.put(source, null);
            if (activeRequests==-1) activeRequests=0;
            activeRequests++;

            // Initialize request and add to the queue
            VolleyRequest request = new VolleyRequest(source.url, this);
            request.setRetryPolicy(new DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setTag(source);
            queue.add(request);
        }
    }

    @Override
    public void run() {
        if (params == null) {
            throw new RuntimeException("Parameter 'params' must be set.");
        }

        fetchSources();
    }

    @Override
    public void onResponse(Object tag, String response) {
        Source source = (Source)tag;
        // Store the result
        Article.IArticleStoreStatus status = Article.storeFromXml(ctx, source.id, response);

        // Store request status
        this.status.put((Source)tag, status);

        // Decrement active requests we have
        activeRequests--;

        // Go to completition handler if there are no more request in progress
        if (activeRequests==0) {
            fetchCompleted();
        }
    }

    @Override
    public void onErrorResponse(Object tag, VolleyError error) {
        // Store request status
        status.put((Source)tag, error);

        // Decrement active requests we have
        activeRequests--;

        // Go to completition handler if there are no more request in progress
        if (activeRequests==0) {
            fetchCompleted();
        }
    }

}
