package net.pottx.team.team;

import net.pottx.element.Court;
import net.pottx.team.Profile;

public abstract class Team
{
    protected final Profile[] members;

    public Team()
    {
        members = new Profile[4];
    }

    public void addMember(int index, Profile profile)
    {
        members[index] = profile;
    }

    public abstract void dispatchPlayers(Court court);
}
