package net.pottx.element.matchunit.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import net.pottx.Cuju;
import net.pottx.Pos;
import net.pottx.action.Action;
import net.pottx.element.Court;
import net.pottx.team.Profile;

public class PlayerControlled extends Player
{
    public PlayerControlled(Court court, Pos pos, Profile profile)
    {
        super(court, pos, profile);
    }

    @Override
    public void input()
    {
        if (!isActing())
        {
            Pos mousePos = court.mousePos;
            if (court.isPosValid(mousePos))
            {
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
                {
                    court.waitingAnim = control(mousePos, Input.Buttons.LEFT);
                }
                else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT))
                {
                    court.waitingAnim = control(mousePos, Input.Buttons.RIGHT);
                }
            }
        }
    }

    public float control(Pos target, int button)
    {
        int stepDist = tilePos.getStepDist(target);
        switch (button)
        {
            case Input.Buttons.LEFT:
                if (stepDist == 0)
                {
                    return setAction(Action.WAIT, target);
                }
                else if (stepDist == 1 && !court.isPosBlocked(target))
                {
                    return setAction(Action.MOVE, target);
                }
                Cuju.instance.getSound("select").play();
                break;

            case Input.Buttons.RIGHT:
                if (isHoldingBall() && stepDist > 0)
                {
                    if (target.equals(court.getGoal().tilePos))
                    {
                        return setAction(Action.SHOOT, target);
                    }
                    return setAction(Action.KICK, target);
                }
                Cuju.instance.getSound("select").play();
                break;

            default:
        }
        return -1.0F;
    }

    @Override
    protected Sprite createSprite()
    {
        Texture texture = Cuju.instance.getTexture("player_self");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(sprite.getWidth() / 16F, sprite.getHeight() / 16F);
        sprite.setOriginCenter();
        return sprite;
    }

    @Override
    public Texture getPortrait()
    {
        return Cuju.instance.getTexture("portrait_self");
    }
}
