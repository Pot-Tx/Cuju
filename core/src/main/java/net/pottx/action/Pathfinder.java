package net.pottx.action;

import net.pottx.Pos;

import java.util.*;

public class Pathfinder
{
    protected Set<PathPoint> openPoints;
    protected Set<PathPoint> closedPoints;
    protected Pos startingPos;
    protected Pos targetPos;
    protected IFilter filter;

    public Pathfinder()
    {
        openPoints = new HashSet<>();
        closedPoints = new HashSet<>();
        startingPos = new Pos(0, 0);
        targetPos = new Pos(0, 0);
    }

    public Pathfinder setSartingPos(Pos pos)
    {
        startingPos.set(pos.getX(), pos.getY());
        return this;
    }

    public Pathfinder setTargetPos(Pos pos)
    {
        targetPos.set(pos.getX(), pos.getY());
        return this;
    }

    public Pathfinder setFilter(IFilter filter)
    {
        this.filter = filter;
        return this;
    }

    public Pos find(Random random)
    {
        if (startingPos == null || targetPos == null)
        {
            return null;
        }
        clear();
        openPoints.add(new PathPoint(startingPos, null, 0.0F, 256F));
        while(true)
        {
            PathPoint focus = getNextFocus(random);
            if (focus == null)
            {
                return null;
            }
            else if (focus.distToEnd == 0.0F)
            {
                while (focus.disToStart > 1.0F)
                {
                    focus = focus.origin;
                }
                return new Pos(focus.x, focus.y);
            }
            else
            {
                spread(focus);
            }
        }
    }

    protected PathPoint getNextFocus(Random random)
    {
        float distMin = 256F;
        List<PathPoint> candidates = new ArrayList<>();
        for (PathPoint point : openPoints)
        {
            float dist = point.distToEnd;
            if (dist <= distMin)
            {
                if (dist < distMin)
                {
                    distMin = dist;
                    candidates.clear();
                }
                candidates.add(point);
            }
        }
        return candidates.isEmpty() ? null : candidates.get(random.nextInt(candidates.size()));
    }

    protected void spread(PathPoint focus)
    {
        openPoints.remove(focus);
        closedPoints.add(focus);
        Pos pos = new Pos(focus.x, focus.y);
        for (int x = -1; x <= 1; x++)
        {
            for (int y = -1; y <= 1; y++)
            {
                pos.set(focus.x + x, focus.y + y);
                if (filter.canPathThrough(pos) || pos.getStepDist(targetPos) == 0)
                {
                    PathPoint pathPoint = new PathPoint(pos, focus, pos.getStepDist(startingPos), pos.getStepDist(targetPos));
                    if (!closedPoints.contains(pathPoint))
                    {
                        openPoints.add(pathPoint);
                    }
                }
            }
        }
    }

    private void clear()
    {
        openPoints.clear();
        closedPoints.clear();
    }

    @FunctionalInterface
    public interface IFilter
    {
        boolean canPathThrough(Pos pos);
    }
}
