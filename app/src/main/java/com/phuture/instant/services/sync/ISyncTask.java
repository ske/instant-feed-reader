package com.phuture.instant.services.sync;

public interface ISyncTask extends Runnable {
    public ISyncResult getResult();
    public ISyncTask setParams(ISyncTaskParams params);
    public ISyncTask setHandler(ISyncTaskResultHandler handler);
}
