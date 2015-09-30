package com.montandoagaragem.util;

import android.util.Log;

/**
 * Thread adormece por 5 segundos. Caso uma resposta não seja obtida do servidor de nomes a conexão é cancelada
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
