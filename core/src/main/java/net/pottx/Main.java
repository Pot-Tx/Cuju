package net.pottx;

import com.badlogic.gdx.ApplicationAdapter;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter
{
    private Cuju game;

    @Override
    public void create()
    {
        game = Cuju.instance;
        game.create();
    }

    @Override
    public void render()
    {
        game.render();
    }

    @Override
    public void resize(int width, int height)
    {
        game.resize(width, height);
    }

    @Override
    public void dispose()
    {
        game.dispose();
    }
}
