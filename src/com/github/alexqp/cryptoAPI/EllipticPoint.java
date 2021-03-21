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

import com.sun.istack.internal.Nullable;

import java.util.Objects;

public class EllipticPoint {

    private final long x;
    private final long y;
    private final boolean inf;

    public static EllipticPoint INFINITY = new EllipticPoint(0, 1, true);

    public EllipticPoint(long x, long y) {
        this(x, y,false);
    }

    public EllipticPoint(long x, long y, boolean infinity) {
        this.x = x;
        this.y = y;
        this.inf = infinity;
    }

    public long getX() {
        if (inf)
            return 0;
        return x;
    }

    public long getY() {
        if (inf)
            return 1;
        return y;
    }

    public boolean isInfinity() {
        return this.inf;
    }

    public EllipticPoint inverse() {
        return new EllipticPoint(this.getX(), -this.getY(), this.isInfinity());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj.getClass().equals(this.getClass()) && this.getX() == ((EllipticPoint) obj).getX() && this.getY() == ((EllipticPoint) obj).getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getX(), this.getY(), inf);
    }
}
