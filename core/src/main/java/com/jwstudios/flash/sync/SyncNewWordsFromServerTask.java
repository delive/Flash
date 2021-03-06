package com.jwstudios.flash.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

/**
 * User: johnwright
 * Date: 3/29/14
 * Time: 12:11 PM
 */

public class SyncNewWordsFromServerTask extends AsyncTask<SyncParm, Integer, Long> {
    // Do the long-running work in here
    ResponseCode responseCode;
    Context context;

    @Override
    protected Long doInBackground(SyncParm... syncParmses) {
        if (syncParmses.length > 0) {
            final SyncParm parm = syncParmses[0];
            final Context context = parm.getContext();
            this.context = context;
            try {
                this.responseCode = WordSyncer.syncNewWordsFromServer(context);
            } catch (IOException e) {
                responseCode = ResponseCode.ERROR;
            }
        }
        return 1L;
    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        if (this.context != null) {
            Toast.makeText(context, "From Server: " + responseCode.toString(), Toast.LENGTH_LONG).show();
        }
    }
}