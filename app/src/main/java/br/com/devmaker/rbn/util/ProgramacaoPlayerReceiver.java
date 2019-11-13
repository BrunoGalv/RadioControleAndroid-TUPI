package br.com.devmaker.rbn.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Cinq on 06/12/2017.
 */

public class ProgramacaoPlayerReceiver  extends BroadcastReceiver {
    private String TAG = "AlarmLog";

    @Override
    public void onReceive(Context context, Intent intent) {
        String programa = intent.getExtras().getString("nomePrograma");
        String locutor = intent.getExtras().getString("nomeLocutor");
        Intent intentMain = new Intent();
        intentMain.putExtra("nomePrograma", programa);
        intentMain.putExtra("nomeLocutor", locutor);
        intentMain.setAction("setTextProgramacao");
        context.sendBroadcast(intentMain);
    }
}