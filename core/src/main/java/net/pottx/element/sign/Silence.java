package net.pottx.element.sign;

import net.pottx.Cuju;
import net.pottx.element.Court;

public class Silence extends Sign
{
    public Silence(Court court, float x, float y)
    {
        super(court, Cuju.instance.textureManager.get("assets/silence.png"), x, y);
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
