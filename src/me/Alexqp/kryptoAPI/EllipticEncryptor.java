/*
 * Copyright (C) 2021-2021 Alexander Schmid
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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