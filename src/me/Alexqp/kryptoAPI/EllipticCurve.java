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

import com.sun.istack.internal.Nullable;

public class EllipticCurve {

    private final long p;
    private final long a;
    private final long b;

    public EllipticCurve(long p, long a, long b) {
        assert p >= 3;
        this.p = p;
        this.a = Utility.mod(a, p);
        this.b = Utility.mod(b, p);
        if (Utility.mod(4 * Utility.modPow(this.a, 3, p) + 27 * Utility.modPow(this.b, 2, p), p) == 0)
            throw new IllegalArgumentException("E has singularities!");
    }

    public long getP() {
        return this.p;
    }

    @Nullable
    public EllipticPoint findNextPoint(long x, long iterations) {
        iterations = x + iterations;
        for (long i = x; i < iterations; i++) {
            long equation = Utility.modPow(i, 3, p) + a * (i) + b;
            long y = Utility.sqrtModP(equation, p);
            if (y != 0)
                return this.createPoint(x, y);
        }
        return null;
    }

    private EllipticPoint createPoint(long x, long y) {
        return new EllipticPoint(Utility.mod(x,p), Utility.mod(y,p));
    }

    public EllipticPoint add(EllipticPoint one, EllipticPoint two) {
        if (one.isInfinity())
            return two;
        if (two.isInfinity())
            return one;

        // Note that all coordinates are between 0 and p - 1 inclusive
        if (one.getX() == two.getY() && Utility.mod(one.getY() + two.getY(), p) == 0)
            return EllipticPoint.INFINITY;

        long factor;
        if (one.getX() != two.getX()) {
            factor = one.getY() - two.getY() * Utility.modPow(one.getX() - two.getX(), p - 2, p);
        } else {
            factor = (3 * Utility.modPow(one.getX(), 2, p) + a) * Utility.modPow(2 * one.getY(), p - 2, p);
        }

        long resultX = Utility.modPow(factor, 2, p) - one.getX() - two.getX();
        return this.createPoint(resultX, -factor * (resultX - one.getX()) - one.getY());
    }

    public EllipticPoint pow(EllipticPoint base, long power) {
        EllipticPoint powered = base;
        EllipticPoint result;
        if (power % 2 != 0) {
            result = base;
        } else {
            result = EllipticPoint.INFINITY;
        }

        while (power > 1) {
            power = power / 2;
            powered = this.add(powered, powered);
            if (power % 2 != 0) {
                result = this.add(result, powered);
            }
        }
        return result;
    }
}