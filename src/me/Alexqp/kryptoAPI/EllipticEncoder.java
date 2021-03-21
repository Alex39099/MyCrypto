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

public class EllipticEncoder {

    private final EllipticCurve ec;
    private final MessageEncoder msgEncoder;
    private final int space;

    public EllipticEncoder(EllipticCurve ec, MessageEncoder msgEncoder, int space) {
        this.ec = ec;
        this.msgEncoder = msgEncoder;
        this.space = space;

        if (space * msgEncoder.getMaxNumber() >= ec.getP()) {
            throw new IllegalArgumentException("space is too big for ec and msgEncoder (" + space * (msgEncoder.getMaxNumber() + 1) + ">= " + ec.getP() + ")");
        }
    }

    public EllipticCurve getEc() {
        return this.ec;
    }

    public List<EllipticPoint> encode(String msg) {
        List<Long> values = msgEncoder.encode(msg);
        List<EllipticPoint> result = new ArrayList<>();
        for (Long x : values) {
            EllipticPoint point = ec.findNextPoint(space * x, space);
            if (point == null)
                throw new IllegalArgumentException("Msg could not be encoded. (not enough space)");
            result.add(point);
        }
        return result;
    }

    public String decode(List<EllipticPoint> values) {
        List<Long> numbers = new ArrayList<>();
        for (EllipticPoint ecPoint : values) {
            numbers.add(ecPoint.getX() / space);
        }
        return msgEncoder.decode(numbers);
    }
}
