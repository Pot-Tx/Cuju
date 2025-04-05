package net.pottx.element.particle;

import net.pottx.Cuju;
import net.pottx.element.Court;

public class Silence extends Particle
{
    public Silence(Court court, float x, float y)
    {
        super(court, Cuju.instance.getTexture("silence"), x, y);
    }

    @Override
    public void logic(float delta)
    {
        translateY(delta * 0.75F);

        float deltaA = delta * 2.5F;
        if (getColor().a > deltaA)
        {
            setAlpha(Math.max(0.0F, getColor().a - deltaA));
        }
    }
}
