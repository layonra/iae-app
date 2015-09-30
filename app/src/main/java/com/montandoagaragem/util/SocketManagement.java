package com.montandoagaragem.util;

import android.util.Log;

import com.montandoagaragem.entity.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Emerson Oliveira on 26/09/15 at 16:58.
 *
 */
public class SocketManagement implements Serializable {

    private static DatagramSocket clientSocket;


    public static String sendDataUDP (String message, String ip, int porta) throws IOException {

        //Prepara o socket para o envio de dados pela porta indicada
        clientSocket = new DatagramSocket();
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

        TimeoutThread.FLAG = false;
        Log.i("UPE", "Alterou a flag");

        String data = new String(receivePacket.getData());

        //Fecha o pacote
        clientSocket.close();

        return data.trim();
    }


    public static void sendDataTCP (Object object, String ip, int porta) throws IOException {

        Socket cs = new Socket(ip, porta);
        ObjectOutputStream os = new ObjectOutputStream(cs.getOutputStream());

        os.writeObject(object);
        os.flush();
        os.close();

        cs.close();

        /*
        Socket cs = new Socket(ip, porta);


        OutputStream out = cs.getOutputStream();

        Log.i("UPE", "Antes do for");

        for (int i = 0; i < message.length(); i++) {
            out.write((int) message.charAt(i));
        }

        Log.i("UPE", "Depois do for");
        cs.close();
        */

    }

    public static Usuario sendDataTCP (String message, String ip, int porta) throws IOException, UsuarioInexistenteException {

        Log.i("UPE", "Entrou no método");

        Socket cs = new Socket(ip, porta);
        Usuario usuario;

        ObjectOutputStream out = new ObjectOutputStream(cs.getOutputStream());
        ObjectInputStream obj = new ObjectInputStream(cs.getInputStream());

        try {

            String[] s = message.split(";");

            String email = s[0];
            String senha = s[1];

            Usuario u = new Usuario();

            u.setEmail(email);
            u.setSenha(senha);

            out.writeObject(u);
            Log.i("UPE", "Escreveu");
            usuario = (Usuario) obj.readObject();

            out.close();
            obj.close();
            cs.close();

            Log.i("UPE", usuario + "");
        } catch (ClassNotFoundException e) {
            Log.i("UPE", "Caiu no catch");
            throw new UsuarioInexistenteException();
        }

        return usuario;
    }


    public static void closeConexao() {
        clientSocket.close();
    }
}
