package com.iae.util;

import com.iae.entity.Usuario;


public class CreateProtocol {


    public static String generateProtocol (String process, Usuario data) {
        return  URL.PROCESSO.PROCESS_A + ";" + data.toString();
    }
}