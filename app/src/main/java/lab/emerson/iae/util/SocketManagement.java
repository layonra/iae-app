package lab.emerson.iae.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Emerson Oliveira on 26/09/15 at 16:58.
 *
 */
public class SocketManagement {


    public static String sendDataUDP (String message, String ip, int porta) throws IOException {

        //Prepara o socket para o envio de dados pela porta indicada
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(ip);
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        //Prepara e envia o pacote de dados
        sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, porta);
        clientSocket.send(sendPacket);

        //Prepara um pacote para recebimento de dados.
        //Aguarda a resposta do servidor(O ip relativo a URL enviada)
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        //RETURN_FLAG = true;//Altera o valor caso algum dado seja recebido
        String data = new String(receivePacket.getData());

        //Fecha o pacote
        clientSocket.close();

        return data.trim();
    }


    public static void sendDataTCP (String message, String ip, int porta) throws IOException {

        Socket cs = new Socket(ip, porta);


        OutputStream out = cs.getOutputStream();

        Log.i("UPE", "Antes do for");

        for (int i = 0; i < message.length(); i++) {
            out.write((int) message.charAt(i));
        }

        Log.i("UPE", "Depois do for");
        cs.close();

    }
}
