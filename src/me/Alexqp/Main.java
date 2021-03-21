package me.Alexqp;

import me.Alexqp.kryptoAPI.EllipticCurve;
import me.Alexqp.kryptoAPI.EllipticEncoder;
import me.Alexqp.kryptoAPI.MessageEncoder;

public class Main {

    public static void main(String[] args) {
        MessageEncoder encryptor = new MessageEncoder(2);
        String test = "das ist ein test";
        System.out.println("Encoder-Test: " + test + "->" + encryptor.decode(encryptor.encode(test)));
        EllipticEncoder ellipticEncryptor = new EllipticEncoder(new EllipticCurve(6833, 5984, 1180), encryptor, 5);
        System.out.println("EEncoder-Test: " + test + "->" + ellipticEncryptor.decode(ellipticEncryptor.encode(test)));
    }
}
