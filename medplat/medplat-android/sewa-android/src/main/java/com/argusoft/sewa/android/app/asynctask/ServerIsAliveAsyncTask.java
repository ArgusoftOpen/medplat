package com.argusoft.sewa.android.app.asynctask;

import android.os.AsyncTask;

import com.argusoft.sewa.android.app.core.SewaService;

public class ServerIsAliveAsyncTask extends AsyncTask<Void, Void, Boolean> {

    SewaService sewaService;

    public ServerIsAliveAsyncTask(SewaService sewaService) {
        super();
        this.sewaService = sewaService;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return sewaService.getServerIsAlive();
    }
}
