package net.pottx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.pottx.element.Court;
import net.pottx.element.matchunit.Ball;

public class Space extends Viewport
{
    public final Vector2 focus;
    public final Court court;
    public final Vector2 mouseOver;

    public Space(Court court, float width, float height)
    {
        setWorldSize(width, height);
        setCamera(new OrthographicCamera());
        this.court = court;
        focus = new Vector2(width / 2.0F, height / 2.0F);
        mouseOver = new Vector2();
        updateMouseOver();
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera)
    {
        int height = Math.round(getWorldHeight() * (float) screenWidth / getWorldWidth());
        setScreenBounds(0, screenHeight - height, screenWidth, height);

        super.update(screenWidth, screenHeight, centerCamera);
    }

    public void apply(float delta, boolean centerCamera)
    {
        updateMouseOver();
        if (!centerCamera)
        {
            Ball ball = court.getBall();
            Vector3 actingPos = (ball.holdingPlayer == null && ball.motion.len() > 0.125F) ? ball.exactPos : court.getActingPlayer().exactPos;
            focus.set(court.sizeX / 2.0F, court.sizeY / 2.0F);
            focus.add(0.25F * actingPos.x, 0.25F * (actingPos.y + actingPos.z));
            focus.add(0.125F * mouseOver.x, 0.125F * mouseOver.y);
            focus.x /= 1.375F;
            focus.y /= 1.375F;

            float dx = (focus.x - getCamera().position.x) * (1.0F - (float) Math.pow(0.5D, 16D * (double) delta));
            float dy = (focus.y - getCamera().position.y) * (1.0F - (float) Math.pow(0.5D, 16D * (double) delta));
            getCamera().translate(dx, dy, 0.0F);
        }

        super.apply(centerCamera);
    }

    public void apply(float delta)
    {
        apply(delta, false);
    }

    public void updateMouseOver()
    {
        mouseOver.set(unproject(new Vector2((float) Gdx.input.getX(), (float) Gdx.input.getY())));
    }
}
