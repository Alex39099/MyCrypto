package me.Alexqp.kryptoAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EllipticEncryptor {

    private final EllipticEncoder eEncoder;
    private final EllipticPoint eP;

    public EllipticEncryptor(EllipticEncoder eEncoder, EllipticPoint eP) {
        this.eEncoder = eEncoder;
        this.eP = eP;
        if (eEncoder.getEc().findNextPoint(eP.getX(), 1) == null)
            throw new IllegalArgumentException("eP is no valid point on the elliptic curve");
    }

    public EllipticPoint createPublicKey(long privateKey) {
        return eEncoder.getEc().pow(eP, privateKey);
    }

    public List<EllipticPoint[]> encrypt(String msg, EllipticPoint pubKey) {
        EllipticCurve ec = eEncoder.getEc();
        List<EllipticPoint[]> result = new ArrayList<>();
        for (EllipticPoint eMsgPoint : eEncoder.encode(msg)) {
            long random = new Random().nextLong();
            EllipticPoint[] rS = new EllipticPoint[2];
            rS[0] = ec.pow(eP, random);
            rS[1] = ec.add(eMsgPoint, ec.pow(pubKey, random));
            result.add(rS);
        }
        return result;
    }

    public String decrypt(List<EllipticPoint[]> values, long privKey) {
        EllipticCurve ec = eEncoder.getEc();
        List<EllipticPoint> eMsg = new ArrayList<>();
        for (EllipticPoint[] rS : values) {
            eMsg.add(ec.add(rS[1], ec.pow(rS[0], privKey).inverse()));
        }
        return eEncoder.decode(eMsg);
    }
}