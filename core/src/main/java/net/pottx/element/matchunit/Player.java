package net.pottx.element.matchunit;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import net.pottx.Action;
import net.pottx.Cuju;
import net.pottx.Pos;
import net.pottx.element.Court;
import net.pottx.element.sign.Sign;

public class Player extends MatchUnit
{
    private Action action;

    public boolean facingLeft;

    public Player(Court court, Pos pos)
    {
        super(court, pos);
        action = Action.NONE;
        facingLeft = pos.getX() > court.sizeX / 2;
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
        if (isActing() && action.perform(this, delta))
        {
            setAction(Action.NONE, tilePos);
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

        super.logic(delta);
    }

    public float act(Pos target, int button)
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
        return 0.0F;
    }

    public boolean isActing()
    {
        return action != Action.NONE;
    }

    private float setAction(Action action, Pos pos)
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
}
