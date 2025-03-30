package net.pottx.element.sign;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.pottx.element.Court;
import net.pottx.element.IGameElement;

public abstract class Sign extends Sprite implements IGameElement
{
    public Court court;

    public Sign(Court court, Texture texture, float x, float y)
    {
        super(texture);
        this.court = court;
        setSize(texture.getWidth() / 16F, texture.getHeight() / 16F);
        setCenter(x, y);
    }

    @Override
    public void input()
    {
    }

    @Override
    public abstract void logic(float delta);

    @Override
    public void draw(SpriteBatch batch)
    {
        super.draw(batch);
    }

    @Override
    public void dispose()
    {
    }
}
