package net.pottx.element;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import net.pottx.Cuju;
import net.pottx.Match;
import net.pottx.Pos;
import net.pottx.action.Behavior;
import net.pottx.element.matchunit.*;
import net.pottx.element.matchunit.player.Player;
import net.pottx.element.matchunit.player.PlayerBot;
import net.pottx.element.matchunit.player.PlayerControlled;
import net.pottx.element.sign.Sign;

import java.util.*;

public class Court implements IGameElement, ICollideable
{
    public static final float TICK = 0.03125F;
    public final Match match;
    public final int sizeX;
    public final int sizeY;
    private final List<Player> players;
    private int curPlayer;
    private Ball ball;
    private final Pillar[] pillars;
    private Goal goal;
    public float waitingAnim;
    private final Set<Sign> signs;
    private Sprite actingPointer;
    private final List<MatchUnit> renderUnits;
    private final Comparator<MatchUnit> renderSorter;
    private float updateRemainder;

    public Court(Match match, int x, int y)
    {
        this.match = match;
        sizeX = x;
        sizeY = y;
        players = new ArrayList<>();
        pillars = new Pillar[2];
        curPlayer = 0;
        waitingAnim = -1.0F;
        signs = new HashSet<>();
        renderUnits = new ArrayList<>();
        renderSorter = (o1, o2) ->
        {
            float y1 = o1.exactPos.y;
            float y2 = o2.exactPos.y;

            if (o1 == goal && !(o2 instanceof ICollideable))
            {
                if (y2 > pillars[0].exactPos.y && y2 < pillars[1].exactPos.y)
                {
                    float z2 = o2.exactPos.z;
                    if (z2 > goal.maxZ)
                    {
                        return -1;
                    }
                    return 1;
                }
            }
            else if (o2 == goal && !(o1 instanceof ICollideable))
            {
                if (y1 > pillars[0].exactPos.y && y1 < pillars[1].exactPos.y)
                {
                    float z1 = o1.exactPos.z;
                    if (z1 > goal.maxZ)
                    {
                        return 1;
                    }
                    return -1;
                }
            }

            if (y1 < y2)
            {
                return 1;
            }
            else if (y1 > y2)
            {
                return -1;
            }
            return 0;
        };
        updateRemainder = 0.0F;
    }

    @Override
    public void input()
    {
        Player player = getActingPlayer();

        if (player != null)
        {
            if (waitingAnim < 0.0F)
            {
                player.input();
            }
        }
    }

    @Override
    public void logic(float delta)
    {
        players.forEach(player -> player.logic(delta));

        if (waitingAnim > 0.0F)
        {
            waitingAnim -= delta;
            if (waitingAnim <= 0.0F)
            {
                waitingAnim = 0.0F;
            }
        }

        if (waitingAnim == 0.0F && !getActingPlayer().isActing())
        {
            waitingAnim = -1.0F;
            if (++curPlayer == players.size())
            {
                curPlayer = 0;
            }
        }

        updateRemainder += delta;
        while (updateRemainder >= TICK)
        {
            updateRemainder -= TICK;
            ball.logic(TICK);
        }

        for (Sign sign : signs)
        {
            sign.logic(delta);
            if (sign.getColor().a == 0.0F)
            {
                despawnSign(sign);
            }
        }

        Player actingPlayer = getActingPlayer();
        if (!actingPlayer.isActing())
        {
            actingPlayer.facingLeft = match.getMouseOver().x < actingPlayer.exactPos.x;
        }
        actingPointer.setCenter(actingPlayer.exactPos.x, actingPlayer.exactPos.y - 0.0625F);
        float a = actingPointer.getColor().a;
        if (actingPlayer.isActing() || waitingAnim > 0.0F)
        {
            if (a > 0.0F)
            {
                actingPointer.setAlpha(Math.max(0.0F, a - 8.0F * delta));
            }
        }
        else if (a < 1.0F)
        {
            actingPointer.setAlpha(Math.min(1.0F, a + 8.0F * delta));
        }
    }

    @Override
    public void draw(SpriteBatch batch)
    {
        Texture groundTexture = Cuju.instance.textureManager.get("assets/ground.png");
        for (int x = 0; x < sizeX; x++)
        {
            for (int y = 0; y < sizeY; y++)
            {
                batch.draw(groundTexture, (float) x, (float) y, 1.0F, 1.0F);
            }
        }
        Pos mousePos = new Pos(match.getMouseOver());

        if (isPosValid(mousePos))
        {
            Texture selectTexture = Cuju.instance.textureManager.get("assets/selection.png");
            batch.draw(selectTexture, (float) mousePos.getX(), (float) mousePos.getY(), 1.0F, 1.0F);
        }

        actingPointer.draw(batch);
        renderUnits.sort(renderSorter);
        renderUnits.forEach(unit -> unit.draw(batch));
        signs.forEach(sign -> sign.draw(batch));
    }

    @Override
    public void dispose()
    {
        players.forEach(MatchUnit::dispose);
        signs.forEach(Sign::dispose);
    }

    @Override
    public void tryCollide(Ball ball)
    {
        if (!isPosValid(ball.tilePos))
        {
            float clampedX = MathUtils.clamp(ball.exactPos.x, 0.015625F, (float) sizeX - 0.015625F);
            if (ball.exactPos.x != clampedX)
            {
                ball.motion.x = 0;
                ball.exactPos.x = clampedX;
            }

            float clampedY = MathUtils.clamp(ball.exactPos.y, 0.015625F, (float) sizeY - 0.015625F);
            if (ball.exactPos.y != clampedY)
            {
                ball.motion.y = 0;
                ball.exactPos.y = clampedY;
            }
        }
    }

    public void init()
    {
        spawnPlayer(new PlayerControlled(this, new Pos(0, 2)));
        spawnPlayer(new PlayerBot(this, new Pos(8, 2), Behavior.RECKLESS));
        ball = new Ball(this, new Pos(1, 2));
        renderUnits.add(ball);
        pillars[0] = new Pillar(this, new Pos(4, 1));
        pillars[1] = new Pillar(this, new Pos(4, 3));
        renderUnits.add(pillars[0]);
        renderUnits.add(pillars[1]);
        goal = new Goal(this, new Pos(4, 2));
        renderUnits.add(goal);
        Texture actingTexture = Cuju.instance.textureManager.get("assets/acting.png");
        actingPointer = new Sprite(actingTexture);
        actingPointer.setSize(actingPointer.getWidth() / 16F, actingPointer.getHeight() / 16F);
    }

    public void spawnPlayer(Player player)
    {
        players.add(player);
        renderUnits.add(player);
    }

    public Player getActingPlayer()
    {
        return curPlayer >= 0 && curPlayer < players.size() ? players.get(curPlayer) : null;
    }

    public Ball getBall()
    {
        return ball;
    }

    public Goal getGoal()
    {
        return goal;
    }

    public boolean isPosValid(Pos pos)
    {
        int x = pos.getX();
        int y = pos.getY();
        return x >= 0 && x < sizeX && y >= 0 && y < sizeY;
    }

    public MatchUnit getUnitAt(Pos pos)
    {
        if (isPosValid(pos))
        {
            for (Player player : players)
            {
                if (player.tilePos.equals(pos))
                {
                    return player;
                }
            }

            for (Pillar pillar : pillars)
            {
                if (pillar.tilePos.equals(pos))
                {
                    return pillar;
                }
            }
        }
        if (ball.tilePos.equals(pos))
        {
            return ball;
        }
        return null;
    }

    public boolean isPosBlocked(Pos pos)
    {
        if (isPosValid(pos))
        {
            MatchUnit unit = getUnitAt(pos);
            return unit != null && unit != ball;
        }
        return true;
    }

    public void spawnSign(Sign sign)
    {
        signs.add(sign);
    }

    public void despawnSign(Sign sign)
    {
        signs.remove(sign);
    }
}
