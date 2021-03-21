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

package com.github.alexqp.cryptoAPI;

import java.math.BigInteger;

public class Utility {

    public static long mod(long a, long b) {
        long result = a % b;
        return result >= 0 ? result : result + b;
    }

    public static int jacobi(long top, long bottom) {
        assert bottom > 0 && mod(bottom, 2) != 0;
        top = mod(top, bottom);
        int result = 1;
        while (top != 0) {
            while (mod(top, 2) == 0) {
                top /= 2;
                if (mod(bottom, 8) == 3 || mod(bottom, 8) == 5)
                    result = -result;
            }
            if (mod(top, 4) == 3 && mod(bottom, 4) == 3)
                result = -result;
            long temp = bottom;
            bottom = top;
            top = temp;
            top = mod(top, bottom);
        }
        if (bottom == 1)
            return result;
        return 0;
    }

    public static long modPow(long base, long exp, long m) {
        return BigInteger.valueOf(base).modPow(BigInteger.valueOf(exp), BigInteger.valueOf(m)).longValue(); // OK bc result is mod m which is a long
    }

    public static long sqrtModP(long a, long p) {
        assert p >= 3;
        long evenFactor = (p - 1) & (-(p - 1));
        long oddFactor = (p - 1) / evenFactor;
        long z;
        for (z = 1; jacobi(z, p) != -1; z++);
        z = modPow(z, oddFactor, p);
        long b = modPow(a, oddFactor, p);
        long x = modPow(a, (oddFactor + 1) / 2, p);
        while (true) {
            long ordB = 1;
            for (long temp = b; temp != 1; temp = modPow(temp, 2, p))
                ordB = ordB * 2;
            if (ordB == 1)
                return x;
            if (ordB == evenFactor)
                return 0;
            long temp = evenFactor / ordB;
            x = mod(x * modPow(z, temp / 2, p), p);
            b = mod(b * modPow(z, temp, p), p);
        }
    }
}
