package com.montandoagaragem.util;

import com.montandoagaragem.entity.Usuario;


public class CreateProtocol {


    public static String generateProtocol (String process, Usuario data) {
        return  URL.PROCESSO.PROCESSO_A + ";" + data.toString();
    }
}
