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
