package net.pottx;

import com.badlogic.gdx.math.Vector2;

public class Pos
{
    private int x;
    private int y;

    public Pos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Pos(float x, float y)
    {
        if (x < 0.0F)
        {
            x -= 1.0F;
        }
        if (y < 0.0F)
        {
            y -= 1.0F;
        }
        this.x = (int) x;
        this.y = (int) y;
    }

    public Pos(Vector2 vec)
    {
        this(vec.x, vec.y);
    }

    public void set(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(Pos pos)
    {
        x = pos.getX();
        y = pos.getY();
    }

    public void set(float x, float y)
    {
        if (x < 0.0F)
        {
            x -= 1.0F;
        }
        if (y < 0.0F)
        {
            y -= 1.0F;
        }
        set((int) x, (int) y);
    }

    public void move(int x, int y)
    {
        this.x += x;
        this.y += y;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Pos)
        {
            return ((Pos) obj).getX() == getX() && ((Pos) obj).getY() == getY();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    public int getStepDist(Pos pos)
    {
        return Math.max(Math.abs(pos.getX() - getX()), Math.abs(pos.getY() - getY()));
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
