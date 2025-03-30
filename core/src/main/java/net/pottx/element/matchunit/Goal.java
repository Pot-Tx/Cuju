package net.pottx.element.matchunit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import net.pottx.Cuju;
import net.pottx.Pos;
import net.pottx.element.Court;
import net.pottx.element.sign.Congrat;

import java.util.Random;

public class Goal extends MatchUnit implements ICollideable
{
    private final float radius;
    private final float halfThickness;
    public final float minZ;
    public final float maxZ;
    public final float centerZ;
    private boolean shot;

    public Goal(Court court, Pos pos)
    {
        super(court, pos);
        radius = 0.25F;
        halfThickness = 0.25F;
        minZ = 1.9375F - 0.125F;
        maxZ = 2.9375F - 0.125F;
        centerZ = (minZ + maxZ) / 2.0F - 0.125F;
        shot = false;
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
                    if (!shot)
                    {
                        shot = true;
                        for (int i = 0; i < 4; i++)
                        {
                            Random rand = court.match.rand;
                            float relX = rand.nextFloat() - 0.5F;
                            float relY = rand.nextFloat() - 0.5F;
                            float motX = relX * (2.0F + rand.nextFloat() * 6.0F);
                            float motY = 1.5F + rand.nextFloat() * 4.5F;
                            court.spawnSign(new Congrat(court, exactPos.x + relX, exactPos.y + centerZ + relY, motX, motY));
                        }
                    }
                }
                else if (!shot)
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

                    if (ball.exactPos.z - minZ < Court.TICK * ball.motion.z)
                    {
                        ball.exactPos.z = minZ;
                        ball.motion.z *= -1.0F;
                    }
                    else if (ball.exactPos.z - maxZ > Court.TICK * ball.motion.z)
                    {
                        ball.motion.z *= -1.0F;
                        ball.exactPos.z = maxZ;
                    }
                }
                else
                {
                    Vector2 dist = new Vector2(dY, dZ);
                    float len = dist.len();
                    Vector2 radUnit = new Vector2(dist.x / len, dist.y / len);

                    Vector2 moveOut = new Vector2(radUnit.x * radius, radUnit.y * radius);
                    moveOut.add(-dist.x, -dist.y);
                    ball.exactPos.add(0.0F, moveOut.x, moveOut.y);

                    Vector2 speed = new Vector2(ball.motion.y, ball.motion.z);
                    float v = -speed.dot(radUnit);
                    speed.set(2.0F * radUnit.x * v, 2.0F * radUnit.y * v);
                    ball.motion.add(0.0F, speed.x, speed.y);
                }
            }
            else if (shot)
            {
                shot = false;
            }
        }
    }
}
