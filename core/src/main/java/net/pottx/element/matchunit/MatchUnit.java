package net.pottx.element.matchunit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import net.pottx.Pos;
import net.pottx.element.Court;
import net.pottx.element.IGameElement;

public abstract class MatchUnit implements IGameElement
{
    public Court court;
    public final Pos tilePos;
    public final Vector3 exactPos;
    protected Sprite sprite;

    public MatchUnit(Court court, Pos pos)
    {
        this.court = court;
        this.tilePos = pos;
        exactPos = new Vector3((float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, 0.0F);
        sprite = createSprite();
        dragSprite();
    }

    @Override
    public void input()
    {
    }

    @Override
    public void logic(float delta)
    {
        dragSprite();
    }

    @Override
    public void draw(SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    public void dispose()
    {
    }

    protected abstract Sprite createSprite();

    protected void dragSprite()
    {
        sprite.setCenter(exactPos.x, exactPos.y + exactPos.z + sprite.getHeight() / 2.0F - 0.125F);
    }

    public Texture getPortrait()
    {
        return null;
    }

    public abstract String getName();

    public String getInfo()
    {
        return null;
    }
}
