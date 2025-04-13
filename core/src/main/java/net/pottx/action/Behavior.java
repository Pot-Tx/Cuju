package net.pottx.action;

import net.pottx.Pos;
import net.pottx.element.matchunit.player.PlayerBot;

public abstract class Behavior
{
    public static final Behavior RECKLESS = new Behavior()
    {
        @Override
        public float actWithBall(PlayerBot player)
        {
            Pos goalPos = player.court.getGoal().tilePos;
            Pos target = new Pos(goalPos.getX(), goalPos.getY());
            if (!target.equals(player.tilePos))
            {
                return player.setAction(Action.SHOOT, target);
            }
            else
            {
                target.move(1, 0);
                if (!player.court.isPosBlocked(target))
                {
                    return player.setAction(Action.MOVE, target);
                }
                target.move(-2, 0);
                if (!player.court.isPosBlocked(target))
                {
                    return player.setAction(Action.MOVE, target);
                }
                return player.setAction(Action.WAIT, player.tilePos);
            }
        }

        @Override
        public float actWithoutBall(PlayerBot player)
        {
            Pathfinder pathfinder = player.court.match.pathfinder;
            Pos target = player.court.getBall().tilePos;
            target = pathfinder.setSartingPos(player.tilePos).setTargetPos(target).find(player.court.match.rand);
            if (target != null && !target.equals(player.tilePos) && !player.court.isPosBlocked(target))
            {
                return player.setAction(Action.MOVE, target);
            }
            else
            {
                return player.setAction(Action.WAIT, player.tilePos);
            }
        }
    };

    public float act(PlayerBot player)
    {
        if (player.isHoldingBall())
        {
            return actWithBall(player);
        }
        else
        {
            return actWithoutBall(player);
        }
    }

    public abstract float actWithBall(PlayerBot player);

    public abstract float actWithoutBall(PlayerBot player);
}
