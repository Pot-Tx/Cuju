package net.pottx;

import net.pottx.element.matchunit.Ball;
import net.pottx.element.matchunit.MatchUnit;
import net.pottx.element.matchunit.Player;
import net.pottx.element.sign.Silence;

import java.util.Random;

public abstract class Action
{
    public static final Action NONE = new Action()
    {
        @Override
        public float set(Player player, Pos pos)
        {
            return 0.0F;
        }

        @Override
        public boolean perform(Player player, float delta)
        {
            return false;
        }
    };

    public static final Action WAIT = new Action()
    {
        @Override
        public float set(Player player, Pos pos)
        {
            if (player.isHoldingBall() || !player.tryHoldBall())
            {
                player.spawnSign(new Silence(player.court, player.exactPos.x, player.exactPos.y + 1.0F));
            }
            return 0.25F;
        }

        @Override
        public boolean perform(Player player, float delta)
        {
            return true;
        }
    };

    public static final Action MOVE = new Action()
    {
        @Override
        public float set(Player player, Pos pos)
        {
            player.tilePos = pos;
            if (player.isHoldingBall())
            {
                player.court.getBall().tilePos.set(pos);
            }
            return 0.25F;
        }

        @Override
        public boolean perform(Player player, float delta)
        {
            if (player.move(delta))
            {
                player.tryHoldBall();
                return true;
            }
            return false;
        }
    };

    public static final Action KICK = new Action()
    {
        @Override
        public float set(Player player, Pos pos)
        {
            if (player.isHoldingBall())
            {
                Ball ball = player.court.getBall();
                player.releaseBall();
                Random rand = player.court.match.rand;
                float z = 1.0F + rand.nextFloat() * 2.0F;
                float t = 2.5F * z / 8.0F;
                float x = ((float) pos.getX() + 0.5F - ball.exactPos.x) * (1.0F + (rand.nextFloat() - 0.5F) * 0.25F) / t;
                float y = ((float) pos.getY() + 0.5F - ball.exactPos.y) * (1.0F + (rand.nextFloat() - 0.5F) * 0.25F) / t;
                ball.exactPos.z = 0.0F;
                ball.launch(x, y, z);
                player.performRotationAnim(Math.min(30F, (float) Math.sqrt(x * x + z * z) * 5.0F));
            }
            return 1.0F;
        }

        @Override
        public boolean perform(Player player, float delta)
        {
            Ball ball = player.court.getBall();
            if (ball.motion.len() == 0.0F)
            {
                MatchUnit unit = player.court.getUnitAt(ball.tilePos);
                if (unit instanceof Player)
                {
                    ((Player) unit).tryHoldBall();
                }
                return true;
            }
            return false;
        }
    };

    public static final Action SHOOT = new Action()
    {
        @Override
        public float set(Player player, Pos pos)
        {
            if (player.isHoldingBall())
            {
                Ball ball = player.court.getBall();
                player.releaseBall();
                Random rand = player.court.match.rand;
                float dZ = player.court.getGoal().centerZ;
                float z = (float) Math.sqrt(16F * dZ);
                float t = z / 8.0F;
                z *= 1.0F + (rand.nextFloat() - 0.5F) * 0.25F;
                float x = ((float) pos.getX() + 0.5F - ball.exactPos.x) * (1.0F + (rand.nextFloat() - 0.5F) * 0.25F) / t;
                float y = ((float) pos.getY() + 0.5F - ball.exactPos.y) * (1.0F + (rand.nextFloat() - 0.5F) * 0.25F) / t;
                ball.exactPos.z = 0.0F;
                ball.launch(x, y, z);
                player.performRotationAnim(Math.min(30F, (float) Math.sqrt(x * x + z * z) * 5.0F));
            }
            return 1.5F;
        }

        @Override
        public boolean perform(Player player, float delta)
        {
            Ball ball = player.court.getBall();
            if (ball.motion.len() < 0.125F)
            {
                MatchUnit unit = player.court.getUnitAt(ball.tilePos);
                if (unit instanceof Player)
                {
                    ((Player) unit).tryHoldBall();
                }
                return true;
            }
            return false;
        }
    };

    public abstract float set(Player player, Pos pos);

    public abstract boolean perform(Player player, float delta);
}
