package net.pottx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import net.pottx.view.Match;

public class Cuju extends Game
{
    public static final Cuju instance = new Cuju();
    private boolean loading;
    private AssetManager assetManager;

    @Override
    public void create()
    {
        assetManager = new AssetManager();

        assetManager.load("textures/ball.png", Texture.class);
        assetManager.load("textures/ground.png", Texture.class);
        assetManager.load("textures/player_self.png", Texture.class);
        assetManager.load("textures/player_enemy.png", Texture.class);
        assetManager.load("textures/selection.png", Texture.class);
        assetManager.load("textures/acting.png", Texture.class);
        assetManager.load("textures/silence.png", Texture.class);
        assetManager.load("textures/pillar.png", Texture.class);
        assetManager.load("textures/goal.png", Texture.class);
        assetManager.load("textures/star.png", Texture.class);
        assetManager.load("textures/panel.png", Texture.class);
        assetManager.load("textures/portrait_self.png", Texture.class);
        assetManager.load("textures/portrait_enemy.png", Texture.class);

        assetManager.load("fonts/rosesareff0000.fnt", BitmapFont.class);

        assetManager.load("sounds/hit.ogg", Sound.class);
        assetManager.load("sounds/select.ogg", Sound.class);
        assetManager.load("sounds/score.ogg", Sound.class);

        loading = true;
    }

    @Override
    public void render()
    {
        if (loading)
        {
            if (assetManager.update())
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
        assetManager.dispose();
    }

    public Texture getTexture(String name)
    {
        return assetManager.get("textures/" + name + ".png", Texture.class);
    }

    public Sound getSound(String name)
    {
        return assetManager.get("sounds/" + name + ".ogg", Sound.class);
    }

    public BitmapFont getFont()
    {
        return assetManager.get("fonts/rosesareff0000.fnt", BitmapFont.class);
    }
}
