package me.Alexqp.kryptoAPI;

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
