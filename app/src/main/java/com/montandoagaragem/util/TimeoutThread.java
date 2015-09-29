package com.montandoagaragem.util;

import android.util.Log;


/**
 * Created by Emerson Oliveira on 29/09/15 at 14:38.
 *
 */
public class TimeoutThread implements Runnable {

    public static boolean FLAG = true;
    @Override
    public void run() {

        try {
            Thread.sleep(5 * 1000);

            if (FLAG) {
                SocketManagement.closeConexao();
                Log.i("UPE", "Fechou a conexão");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
