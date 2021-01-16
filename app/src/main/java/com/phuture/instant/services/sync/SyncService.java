package com.phuture.instant.services.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncService extends Service {

    public static final String PARAM_SOURCE_ID_LIST = "sourceIdList";

    protected ExecutorService executor;

    final Object lock = new Object();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newFixedThreadPool(4);
    }

    private void startSync(final String[] sourcesToSync) {
        synchronized (lock) {

            Toast.makeText(getApplicationContext(), "Sync", Toast.LENGTH_SHORT).show();

            final long start = System.currentTimeMillis();

            ISyncTaskParams params = new ISyncTaskParams() {
                @Override
                public List<String> getSourceIdList() {
                    return Arrays.asList(sourcesToSync);
                }
            };
            ISyncTaskResultHandler handler = new ISyncTaskResultHandler() {
                @Override
                public void onResult(ISyncResult result) {
                    long elapsed = System.currentTimeMillis() - start;
                    System.out.println("Sync completed, elapsed time in milliseconds: " + elapsed);
                    Toast.makeText(getApplicationContext(), "Sync OK", Toast.LENGTH_SHORT).show();
                    stopSelf();
                }
            };

            ISyncTask sync = new SyncTask(getApplicationContext())
                    .setParams(params)
                    .setHandler(handler);

            System.out.println("Sync start");

            executor.execute(sync);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] sourceIdList = intent.getStringArrayExtra(PARAM_SOURCE_ID_LIST);
        if (sourceIdList!=null && sourceIdList.length>0)  {
            startSync(sourceIdList);
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        executor.shutdown();
        executor = null;
        super.onDestroy();
    }

}
