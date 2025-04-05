package net.pottx.element.matchunit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import net.pottx.Cuju;
import net.pottx.Pos;
import net.pottx.element.Court;

public class Pillar extends MatchUnit implements ICollideable
{
    private final float radius;

    public Pillar(Court court, Pos pos)
    {
        super(court, pos);
        radius = 0.25F;
    }

    @Override
    protected Sprite createSprite()
    {
        Texture texture = Cuju.instance.getTexture("pillar");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(sprite.getWidth() / 16F, sprite.getHeight() / 16F);
        return sprite;
    }

    @Override
    public String getName()
    {
        return "Pillar";
    }

    @Override
    public void tryCollide(Ball ball)
    {
        Vector2 dist = new Vector2(ball.exactPos.x, ball.exactPos.y).add(-exactPos.x, -exactPos.y);
        float len = dist.len();
        if (len < radius)
        {
            Vector2 radUnit = new Vector2(dist.x / len, dist.y / len);

            Vector2 moveOut = new Vector2(radUnit.x * radius, radUnit.y * radius);
            moveOut.add(-dist.x, -dist.y);
            ball.exactPos.add(moveOut.x, moveOut.y, 0.0F);

            Vector2 speed = new Vector2(ball.motion.x, ball.motion.y);
            float v = -speed.dot(radUnit);
            speed.set(2.0F * radUnit.x * v, 2.0F * radUnit.y * v);
            ball.motion.add(speed.x, speed.y, 0.0F);

            Cuju.instance.getSound("hit").play(Math.min(1.0F, 0.25F * speed.len()));
        }
    }
}
