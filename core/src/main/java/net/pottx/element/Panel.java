package net.pottx.element;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.pottx.Cuju;
import net.pottx.element.matchunit.MatchUnit;

public class Panel implements IGameElement
{
    public Court court;
    public BitmapFont font;
    private MatchUnit curUnit;
    private final Sprite frame;
    private final Sprite portrait;
    private String info;

    public Panel(Court court)
    {
        this.court = court;
        font = Cuju.instance.getFont();

        frame = new Sprite(Cuju.instance.getTexture("panel"));
        frame.setSize(144F, 48F);
        frame.setPosition(0.0F, 0.0F);
        portrait = new Sprite(Cuju.instance.getTexture("portrait_self"));
        portrait.setSize(32F, 32F);
        portrait.setPosition(16F, 8F);
    }

    @Override
    public void input()
    {
    }

    @Override
    public void logic(float delta)
    {
        MatchUnit mouseOver = court.getUnitAt(court.mousePos);
        if (curUnit != mouseOver)
        {
            curUnit = mouseOver;
            if (mouseOver != null)
            {
                portrait.setTexture(curUnit.getPortrait());
                info = curUnit.getInfo();
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch)
    {
        frame.draw(batch);
        if (curUnit != null)
        {
            if (portrait.getTexture() != null)
            {
                portrait.draw(batch);
            }

            font.setColor(Color.BLACK);
            font.getData().setScale(0.25F);
            font.draw(batch, curUnit.getName(), 60F, 38F);

            font.getData().setScale(0.21875F);
            if (info != null)
            {
                font.draw(batch, info, 60F, 28F);
            }
        }
    }

    @Override
    public void dispose()
    {
    }
}
