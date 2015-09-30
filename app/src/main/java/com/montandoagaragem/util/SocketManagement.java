package com.montandoagaragem.util;

import com.montandoagaragem.entity.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketManagement implements Serializable {

    private static DatagramSocket clientSocket;


    public static String sendDataUDP (String message, String ip, int porta) throws IOException {

        //Prepara o socket para o envio de dados pela porta indicada
        clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(ip);
        byte[] sendData;
        byte[] receiveData = new byte[1024];

        //Prepara e envia o pacote de dados
        sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, porta);
        clientSocket.send(sendPacket);

        //Prepara um pacote para recebimento de dados.
        //Aguarda a resposta do servidor(O ip relativo a URL enviada)
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        //Flag do TimeoutThread. Que caso não alterado seu valor para true, quando a thread acorda, fecha a conexão
        TimeoutThread.FLAG = false;

        String data = new String(receivePacket.getData());

        //Fecha o pacote
        clientSocket.close();

        return data.trim();
    }

    public static Usuario sendDataTCP (Object object, String ip, int porta) throws IOException, UsuarioInexistenteException {

        //Cria a conexão
        Socket cs = new Socket(ip, porta);
        Usuario usuario;

        ObjectOutputStream out = new ObjectOutputStream(cs.getOutputStream());
        ObjectInputStream obj = new ObjectInputStream(cs.getInputStream());

        try {
            //Escreve o objeto
            out.writeObject(object);
            //Lê o objeto de retorno
            usuario = (Usuario) obj.readObject();

           //Fecha as conexões
            out.close();
            obj.close();
            cs.close();

        } catch (ClassNotFoundException e) {
            throw new UsuarioInexistenteException();
        }

        return usuario;
    }

    public static List<Usuario> getDataTCP (Object object, String ip, int porta) throws IOException, UsuarioInexistenteException {

        Socket cs = new Socket(ip, porta);
        List<Usuario> usuarios = new ArrayList<>();

        ObjectOutputStream out = new ObjectOutputStream(cs.getOutputStream());
        ObjectInputStream obj = new ObjectInputStream(cs.getInputStream());

        try {
            out.writeObject(object);

            usuarios = (List<Usuario>) obj.readObject();

            out.close();
            obj.close();
            cs.close();

        } catch (ClassNotFoundException e) {
            throw new UsuarioInexistenteException();
        }

        return usuarios;

    }

    public static void closeConexao() {
        clientSocket.close();
    }
}
