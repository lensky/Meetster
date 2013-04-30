package com.russia.meetster.data;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MeetsterSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();

    private static MeetsterSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new MeetsterSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
