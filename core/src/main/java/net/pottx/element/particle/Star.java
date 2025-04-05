package net.pottx.element.particle;

import com.badlogic.gdx.math.Vector2;
import net.pottx.Cuju;
import net.pottx.element.Court;

import java.util.Random;

public class Star extends Particle
{
    private final Vector2 motion;
    private final float revolution;

    public Star(Court court, float x, float y, float motionX, float motionY)
    {
        super(court, Cuju.instance.getTexture("star"), x, y);
        motion = new Vector2(motionX, motionY);
        Random rand = court.match.rand;
        rotate((rand.nextFloat() - 0.5F) * 180F);
        revolution = (rand.nextFloat() - 0.5F) * 180F;
    }

    @Override
    public void logic(float delta)
    {
        translate(motion.x * delta, motion.y * delta);
        rotate(revolution * delta);

        motion.y -= 8.0F * delta;

        float deltaA = delta * 0.75F;
        if (getColor().a > deltaA)
        {
            setAlpha(Math.max(0.0F, getColor().a - deltaA));
        }
    }
}
