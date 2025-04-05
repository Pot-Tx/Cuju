package net.pottx.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import net.pottx.action.Pathfinder;
import net.pottx.element.Court;
import net.pottx.element.Panel;
import net.pottx.team.Profile;
import net.pottx.team.team.Team;
import net.pottx.team.team.TeamBot;
import net.pottx.team.team.TeamControlled;

import java.util.Random;

public class Match implements Screen
{
    public Space space;
    public Hud hud;
    private final SpriteBatch batch;
    private final Court court;
    private final Panel panel;
    public final Random rand;
    public final Pathfinder pathfinder;
    public final Team teamSelf;
    public final Team teamEnemy;

    public Match()
    {
        court = new Court(this, 13, 7);
        panel = new Panel(court);
        space = new Space(court, 15F, 10F);
        hud = new Hud(court, 144F, 48F);
        batch = new SpriteBatch();
        rand = new Random();
        pathfinder = new Pathfinder();
        pathfinder.setFilter(pos -> !court.isPosBlocked(pos));
        teamSelf = new TeamControlled();
        teamEnemy = new TeamBot();
    }

    @Override
    public void show()
    {
        for (int i = 0; i < 4; i++)
        {
            teamSelf.addMember(i, new Profile(rand));
        }
        for (int i = 0; i < 4; i++)
        {
            teamEnemy.addMember(i, new Profile(rand));
        }
        court.init();
    }

    @Override
    public void render(float delta)
    {
        court.input();

        court.logic(delta);
        panel.logic(delta);

        ScreenUtils.clear(Color.FOREST);

        space.apply(delta);
        batch.setProjectionMatrix(space.getCamera().combined);
        batch.begin();
        court.draw(batch);
        batch.end();

        hud.apply();
        batch.setProjectionMatrix(hud.getCamera().combined);
        batch.begin();
        panel.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        space.update(width, height);
        hud.update(width, height);
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        court.dispose();
    }

    public Vector2 getMouseOver()
    {
        return space.mouseOver;
    }
}
