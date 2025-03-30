package net.pottx.element.matchunit.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import net.pottx.Cuju;
import net.pottx.Pos;
import net.pottx.action.Action;
import net.pottx.element.Court;
import net.pottx.element.matchunit.Ball;
import net.pottx.element.matchunit.MatchUnit;
import net.pottx.element.sign.Sign;

public class Player extends MatchUnit
{
    private Action action;
    public boolean facingLeft;
    private boolean triedCatch;
    private float motionZ;

    public Player(Court court, Pos pos)
    {
        super(court, pos);
        action = Action.NONE;
        facingLeft = pos.getX() > court.sizeX / 2;
        triedCatch = false;
        motionZ = 0.0F;
    }

    @Override
    protected Sprite createSprite()
    {
        Texture texture = Cuju.instance.textureManager.get("assets/player.png");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(sprite.getWidth() / 16F, sprite.getHeight() / 16F);
        sprite.setOriginCenter();
        return sprite;
    }

    @Override
    public void logic(float delta)
    {
        if (isActing())
        {
            if (action.perform(this, delta))
            {
                setAction(Action.NONE, tilePos);
            }
        }
        else
        {
            tryCatchBall();
        }

        if (sprite.isFlipX() != facingLeft)
        {
            sprite.setFlip(facingLeft, false);
        }

        float rotation = sprite.getRotation();
        if (rotation != 0.0F)
        {
            if (Math.abs(rotation) < 1.0F)
            {
                sprite.setRotation(0.0F);
            }
            else
            {
                sprite.setRotation(rotation * (float) Math.pow(0.5D, 8.0D * (double) delta));
            }
        }

        exactPos.z += motionZ * delta;
        if (exactPos.z <= 0.0F)
        {
            exactPos.z = 0.0F;
            motionZ = 0.0F;
        }
        else
        {
            motionZ -= 8.0F * delta;
        }

        super.logic(delta);
    }

    public boolean isActing()
    {
        return action != Action.NONE;
    }

    public float setAction(Action action, Pos pos)
    {
        this.action = action;
        return action.set(this, pos);
    }

    public boolean isHoldingBall()
    {
        return court.getBall().holdingPlayer == this;
    }

    public boolean tryHoldBall()
    {
        Ball ball = court.getBall();
        if (tilePos.equals(ball.tilePos))
        {
            ball.holdingPlayer = this;
            return true;
        }
        return false;
    }

    public void releaseBall()
    {
        if (isHoldingBall())
        {
            court.getBall().holdingPlayer = null;
        }
    }

    protected void tryCatchBall()
    {
        Ball ball = court.getBall();
        if (!isHoldingBall() && tilePos.equals(ball.tilePos) && ball.exactPos.z < 1.0F)
        {
            if (!triedCatch)
            {
                triedCatch = true;
                if (court.match.rand.nextFloat() < 0.5F)
                {
                    performJumpAnim();
                    ball.motion.x /= 8.0F;
                    ball.motion.y /= 8.0F;
                    ball.motion.z /= 2.0F;
                }
            }
        }
        else if (triedCatch)
        {
            triedCatch = false;
        }
    }

    public boolean move(float delta)
    {
        float distX = (float) tilePos.getX() + 0.5F - exactPos.x;
        float distY = (float) tilePos.getY() + 0.5F - exactPos.y;
        float dist = (float) Math.sqrt(distX * distX + distY * distY);
        float step = delta * 8.0F;
        if (step < dist)
        {
            exactPos.add(step * distX / dist, step * distY / dist, 0.0F);
            return false;
        }
        else
        {
            exactPos.x = (float) tilePos.getX() + 0.5F;
            exactPos.y = (float) tilePos.getY() + 0.5F;
            return true;
        }
    }

    public void spawnSign(Sign sign)
    {
        court.spawnSign(sign);
    }

    public void performRotationAnim(float rotation)
    {
        sprite.rotate(facingLeft ? -rotation : rotation);
    }

    public void performJumpAnim()
    {
        motionZ = 2.0F;
    }
}
