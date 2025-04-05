package net.pottx.team.team;

import net.pottx.Pos;
import net.pottx.action.Behavior;
import net.pottx.element.Court;
import net.pottx.element.matchunit.player.PlayerBot;


public class TeamBot extends Team
{
    private final Behavior[] behaviors;

    public TeamBot(Behavior... behaviors)
    {
        this.behaviors = new Behavior[members.length];
        for (int i = 0; i < members.length; i++)
        {
            if (behaviors.length > i)
            {
                this.behaviors[i] = behaviors[i];
            }
            else
            {
                this.behaviors[i] = Behavior.RECKLESS;
            }
        }
    }

    @Override
    public void dispatchPlayers(Court court)
    {
        int gap = court.sizeY / 2;
        for (int i = 0; i < members.length; i++)
        {
            if (members[i] != null)
            {
                int mod = i % 2;
                Pos pos = new Pos(court.sizeX - 1 - (i / 2) * 2, gap + (mod == 0 ? -1 : 1) * gap / 2);
                if (court.sizeY % 2 == 0 && mod == 0)
                {
                    pos.move(0, -1);
                }
                court.spawnPlayer(new PlayerBot(court, pos, members[i], behaviors[i]));
            }
        }
    }
}
