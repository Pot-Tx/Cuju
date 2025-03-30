package net.pottx.element.matchunit.player;

import net.pottx.Pos;
import net.pottx.action.Behavior;
import net.pottx.element.Court;

public class PlayerBot extends Player
{
    private final Behavior behavior;

    public PlayerBot(Court court, Pos pos, Behavior behavior)
    {
        super(court, pos);
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
}
