package net.pottx.action;

import net.pottx.Pos;

public class PathPoint
{
    public final int x;
    public final int y;
    public final PathPoint origin;
    public final float disToStart;
    public final float distToEnd;

    public PathPoint(Pos pos, PathPoint origin, float disToStart, float distToEnd)
    {
        x = pos.getX();
        y = pos.getY();
        this.disToStart = disToStart;
        this.distToEnd = distToEnd;
        this.origin = origin;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof PathPoint)
        {
            return ((PathPoint) obj).x == x && ((PathPoint) obj).y == y;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return x + (y << 8);
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
