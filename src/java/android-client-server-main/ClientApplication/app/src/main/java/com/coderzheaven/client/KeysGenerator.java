package com.coderzheaven.client;

import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeysGenerator {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator kgen = KeyPairGenerator.getInstance("RSA");
        kgen.initialize(2048);
        KeyPair keys = kgen.generateKeyPair();

        FileWriter fileWP = null;
        FileWriter fileWPr= null;
        try{
            fileWP = new FileWriter("publicK.txt");
            fileWPr = new FileWriter("privateK.txt");

            fileWP.write(String.valueOf(keys.getPublic().getEncoded()));
            fileWPr.write(String.valueOf(keys.getPrivate().getEncoded()));

            fileWP.close();
            fileWPr.close();
        }catch( Exception exp){
            System.out.println("Exception message"+ exp);
        }
    }
}
