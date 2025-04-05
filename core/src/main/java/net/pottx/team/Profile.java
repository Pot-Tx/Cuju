package net.pottx.team;

import java.util.Random;

public class Profile
{
    public static final String[] LAST_NAMES = new String[] {"Zhao", "Qian", "Sun", "Li", "Zhou", "Wu", "Zheng", "Wang"};
    public static final String[] FIRST_NAMES = new String[] {"Da", "Er", "San", "Si", "Wu", "Liu"};

    public final String name;
    private int speed;
    private int spread;
    private int stamina;
    public boolean tired;

    public Profile(Random random)
    {
        name = LAST_NAMES[random.nextInt(LAST_NAMES.length)] + " " + FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        speed = random.nextInt(8);
        spread = random.nextInt(16);
        stamina = 1 + random.nextInt(4);
        tired = false;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getSpread()
    {
        return spread;
    }

    public int getStamina()
    {
        return stamina;
    }
}
