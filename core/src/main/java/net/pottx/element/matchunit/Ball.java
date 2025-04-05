package net.pottx.element.matchunit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.pottx.Cuju;
import net.pottx.Pos;
import net.pottx.element.Court;
import net.pottx.element.matchunit.player.Player;

public class Ball extends MatchUnit
{
    public Vector3 motion;
    private float revolution;
    public Player holdingPlayer;

    public Ball(Court court, Pos pos)
    {
        super(court, pos);
        motion = new Vector3();
        revolution = 0.0F;
        holdingPlayer = null;
    }

    @Override
    protected Sprite createSprite()
    {
        Texture texture = Cuju.instance.getTexture("ball");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(sprite.getWidth() / 16F, sprite.getHeight() / 16F);
        sprite.setOriginCenter();
        return sprite;
    }

    @Override
    public void logic(float delta)
    {
        if (motion.len() > 0.0F)
        {
            court.tryCollide(this);
            MatchUnit unit = court.getUnitAt(tilePos);
            if (tilePos.equals(court.getGoal().tilePos))
            {
                court.getGoal().tryCollide(this);
            }
            else if (unit instanceof ICollideable)
            {
                ((ICollideable) unit).tryCollide(this);
                court.getGoal().tryCollide(this);
            }
        }

        if (holdingPlayer != null)
        {
            exactPos.x = holdingPlayer.exactPos.x + (holdingPlayer.facingLeft ? -0.15F : 0.15F);
            exactPos.y = holdingPlayer.exactPos.y - 0.0625F;
            if (exactPos.z == 0.0F)
            {
                launch(0.0F, 0.0F, 1.5F);
            }
        }
        else
        {
            if (motion.len() > 0.0F)
            {
                tilePos.set(exactPos.x, exactPos.y);
            }
            else
            {
                if (court.getUnitAt(tilePos) instanceof ICollideable)
                {
                    Vector2 deviation = new Vector2(exactPos.x, exactPos.y).add(-(float) tilePos.getX() - 0.5F, -(float) tilePos.getY() - 0.5F);
                    float angle = deviation.angleDeg();
                    if (angle >= 45F && angle < 135F)
                    {
                        tilePos.move(0, 1);
                    }
                    else if (angle >= 135F && angle < 225F)
                    {
                        tilePos.move(-1, 0);
                    }
                    else if (angle >= 225F && angle < 315F)
                    {
                        tilePos.move(0, -1);
                    }
                    else
                    {
                        tilePos.move(1, 0);
                    }
                }
                else if (!court.isPosValid(tilePos))
                {
                    tilePos.set(exactPos.x, exactPos.y);
                }
            }
        }

        exactPos.add(motion.x * delta, motion.y * delta, motion.z * delta);
        sprite.rotate(revolution * delta);

        if (exactPos.z <= 0.0F)
        {
            exactPos.z = 0.0F;
            motion.x *= 0.75F;
            motion.y *= 0.75F;
            revolution *= 0.75F;
            motion.z = Math.abs(0.25F * motion.z);

            Cuju.instance.getSound("hit").play(Math.min(1.0F, 0.25F * motion.z));

            if (motion.x * motion.x + motion.y * motion.y < 0.125F)
            {
                motion.x = 0.0F;
                motion.y = 0.0F;
            }
            if (motion.z < 0.5F)
            {
                motion.z = 0.0F;
            }
        }
        else
        {
            motion.z -= 8.0F * delta;
        }

        super.logic(delta);
    }

    public void launch(float x, float y, float z)
    {
        motion.set(x, y, z);
        revolution = (court.match.rand.nextFloat() - 0.5F) * 180F;
    }

    @Override
    public String getName()
    {
        return "Ball";
    }
}
