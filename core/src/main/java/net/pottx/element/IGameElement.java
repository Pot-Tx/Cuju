package net.pottx.element;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IGameElement
{
    void input();

    void logic(float delta);

    void draw(SpriteBatch batch);

    void dispose();
}
