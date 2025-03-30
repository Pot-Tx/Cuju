package net.pottx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.pottx.action.Pathfinder;
import net.pottx.element.Court;

import java.util.Random;

public class Match implements Screen
{
    public Viewport viewport;
    private final SpriteBatch batch;
    private final Court court;
    public final Random rand;
    public final Pathfinder pathfinder;

    public Match()
    {
        viewport = new FitViewport(9.0F, 7.0F);
        batch = new SpriteBatch();
        court = new Court(this, 9, 5);
        rand = new Random();
        pathfinder = new Pathfinder();
        pathfinder.setFilter(pos -> !court.isPosBlocked(pos));
    }

    @Override
    public void show()
    {
        court.init();
    }

    @Override
    public void render(float delta)
    {
        court.input();

        court.logic(delta);

        ScreenUtils.clear(Color.FOREST);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        court.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        court.dispose();
    }

    public Vector2 getMouseOver()
    {
        Vector2 mouse = new Vector2((float) Gdx.input.getX(), (float) Gdx.input.getY());
        return viewport.unproject(mouse);
    }
}
