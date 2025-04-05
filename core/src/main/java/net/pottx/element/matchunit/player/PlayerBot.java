package net.pottx.element.matchunit.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import net.pottx.Cuju;
import net.pottx.Pos;
import net.pottx.action.Behavior;
import net.pottx.element.Court;
import net.pottx.team.Profile;

public class PlayerBot extends Player
{
    private final Behavior behavior;

    public PlayerBot(Court court, Pos pos, Profile profile, Behavior behavior)
    {
        super(court, pos, profile);
        this.behavior = behavior;
    }

    @Override
    public void input()
    {
        if (!isActing())
        {
            court.waitingAnim = behavior.act(this);
        }
    }

    @Override
    protected Sprite createSprite()
    {
        Texture texture = Cuju.instance.getTexture("player_enemy");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(sprite.getWidth() / 16F, sprite.getHeight() / 16F);
        sprite.setOriginCenter();
        return sprite;
    }

    @Override
    public Texture getPortrait()
    {
        return Cuju.instance.getTexture("portrait_enemy");
    }
}
