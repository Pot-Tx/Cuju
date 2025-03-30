package net.pottx.element.matchunit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import net.pottx.Cuju;
import net.pottx.Pos;
import net.pottx.element.Court;

public class Goal extends MatchUnit implements ICollideable
{
    private final float radius;
    private final float halfThickness;
    public final float minZ;
    public final float maxZ;
    public final float centerZ;

    public Goal(Court court, Pos pos)
    {
        super(court, pos);
        radius = 0.25F;
        halfThickness = 0.25F;
        minZ = 1.9375F;
        maxZ = 2.9375F;
        centerZ = (minZ + maxZ) / 2.0F;
    }

    @Override
    protected Sprite createSprite()
    {
        Texture texture = Cuju.instance.textureManager.get("assets/goal.png");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(sprite.getWidth() / 16F, sprite.getHeight() / 16F);
        return sprite;
    }

    @Override
    public void tryCollide(Ball ball)
    {
        if (ball.exactPos.z > minZ && ball.exactPos.z < maxZ)
        {
            float dX = ball.exactPos.x - exactPos.x;
            if (Math.abs(dX) < halfThickness)
            {
                float dY = ball.exactPos.y - exactPos.y;
                float dZ = ball.exactPos.z - centerZ;
                if (dY * dY + dZ * dZ < radius * radius)
                {
                    //TODO
                }
                else
                {
                    if (dX < 0.0F)
                    {
                        ball.exactPos.x -= halfThickness + dX;
                    }
                    else
                    {
                        ball.exactPos.x += halfThickness - dX;
                    }
                    ball.motion.x *= -1.0F;
                }
            }
        }
    }
}
