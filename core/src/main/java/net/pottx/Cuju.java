package net.pottx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Cuju extends Game
{
    public static final Cuju instance = new Cuju();
    private boolean loading;
    public AssetManager textureManager;

    @Override
    public void create()
    {
        textureManager = new AssetManager();
        textureManager.load("assets/ball.png", Texture.class);
        textureManager.load("assets/ground.png", Texture.class);
        textureManager.load("assets/player.png", Texture.class);
        textureManager.load("assets/selection.png", Texture.class);
        textureManager.load("assets/acting.png", Texture.class);
        textureManager.load("assets/silence.png", Texture.class);
        textureManager.load("assets/pillar.png", Texture.class);
        textureManager.load("assets/goal.png", Texture.class);
        textureManager.load("assets/star.png", Texture.class);
        loading = true;
    }

    @Override
    public void render()
    {
        if (loading)
        {
            if (textureManager.update())
            {
                loading = false;
                Match match = new Match();
                setScreen(match);
            }
            else
            {
                return;
            }
        }
        super.render();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        textureManager.dispose();
    }
}
