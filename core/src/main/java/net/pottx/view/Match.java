package net.pottx.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import net.pottx.action.Pathfinder;
import net.pottx.element.Court;

import java.util.Random;

public class Match implements Screen
{
    public Space space;
    private final SpriteBatch batch;
    private final Court court;
    public final Random rand;
    public final Pathfinder pathfinder;

    public Match()
    {
        court = new Court(this, 9, 5);
        space = new Space(court, 9.0F, 9.0F);
        batch = new SpriteBatch();
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
        space.apply(delta);
        batch.setProjectionMatrix(space.getCamera().combined);
        batch.begin();
        court.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        space.update(width, height);
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
        return space.mouseOver;
    }
}
