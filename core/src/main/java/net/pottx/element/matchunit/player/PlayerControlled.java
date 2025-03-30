package net.pottx.element.matchunit.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.pottx.Pos;
import net.pottx.action.Action;
import net.pottx.element.Court;

public class PlayerControlled extends Player
{
    public PlayerControlled(Court court, Pos pos)
    {
        super(court, pos);
    }

    @Override
    public void input()
    {
        if (!isActing())
        {
            Pos mousePos = new Pos(court.match.getMouseOver());
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
                break;

            default:
        }
        return -1.0F;
    }
}
