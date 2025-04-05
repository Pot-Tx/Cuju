package net.pottx.team.team;

import net.pottx.Pos;
import net.pottx.element.Court;
import net.pottx.element.matchunit.player.PlayerControlled;

public class TeamControlled extends Team
{
    @Override
    public void dispatchPlayers(Court court)
    {
        int gap = court.sizeY / 2;
        for (int i = 0; i < members.length; i++)
        {
            if (members[i] != null)
            {
                int mod = i % 2;
                Pos pos = new Pos((i / 2) * 2, gap + (mod == 0 ? -1 : 1) * gap / 2);
                if (court.sizeY % 2 == 0 && mod == 0)
                {
                    pos.move(0, -1);
                }
                court.spawnPlayer(new PlayerControlled(court, pos, members[i]));
            }
        }
    }
}
