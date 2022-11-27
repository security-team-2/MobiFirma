package com.coderzheaven.client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;




public class KeysGenerator {

   /* public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator kgen = KeyPairGenerator.getInstance("RSA");
        kgen.initialize(2048);
        KeyPair kp = kgen.generateKeyPair();

        Key pub = kp.getPublic();
        Key pvt = kp.getPrivate();


        String outFile = "key";
        FileOutputStream out = new FileOutputStream(outFile + ".key");
        out.write(pvt.getEncoded());
        out.close();

        out = new FileOutputStream(outFile + ".pub");
        out.write(pvt.getEncoded());
        out.close();
    }
    */

}
