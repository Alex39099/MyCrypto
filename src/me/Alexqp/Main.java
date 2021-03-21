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

package me.Alexqp;

import me.Alexqp.cryptoAPI.EllipticCurve;
import me.Alexqp.cryptoAPI.EllipticEncoder;
import me.Alexqp.cryptoAPI.MessageEncoder;

public class Main {

    public static void main(String[] args) {
        MessageEncoder encryptor = new MessageEncoder(2);
        String test = "das ist ein test";
        System.out.println("Encoder-Test: " + test + "->" + encryptor.decode(encryptor.encode(test)));
        EllipticEncoder ellipticEncryptor = new EllipticEncoder(new EllipticCurve(6833, 5984, 1180), encryptor, 5);
        System.out.println("EEncoder-Test: " + test + "->" + ellipticEncryptor.decode(ellipticEncryptor.encode(test)));
    }
}
