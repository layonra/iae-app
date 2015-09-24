package server;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Emerson Oliveira on 23/09/15 at 22:43.
 *
 */
public class ClientServer {


    public static String receiverMessage() {

        String s = null;
        try {
            ServerSocket server = new ServerSocket(1030);
            Log.i("UPE", "Ouvindo");
            Socket conexao = server.accept();
            Log.i("UPE", "Conexão estabelecida");
            InputStream is = conexao.getInputStream();

            int c;
            s = null;

            while ((c = is.read()) != -1) {
                Log.i("UPE", "" + (char) c);

                if (s == null) {
                    s = (char) c + "";
                } else {
                    s = s + (char) c;
                }
            }

        } catch (IOException e) {
        }

        return s;
    }

}
