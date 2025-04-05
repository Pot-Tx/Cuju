package net.pottx.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.pottx.element.Court;

public class Hud extends Viewport
{
    public final Court court;

    public Hud(Court court, float width, float height)
    {
        setWorldSize(width, height);
        setCamera(new OrthographicCamera());
        this.court = court;
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera)
    {
        int height = Math.round(getWorldHeight() * (float) screenWidth / getWorldWidth());
        setScreenBounds(0, 0, screenWidth, height);

        super.update(screenWidth, screenHeight, centerCamera);
    }

    @Override
    public void apply()
    {
        super.apply(true);
    }
}
