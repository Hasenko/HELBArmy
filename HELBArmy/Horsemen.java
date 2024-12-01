import java.util.ArrayList;

public class Horsemen extends MovableEntity {
    private static int safetyDistance;
    public Horsemen(Position position, String side, HELBArmy gameBoard)
    {
        super(position, side, "assets/unity/" + side + "_horsemen.png", gameBoard, 200, 10, 2.0);
        safetyDistance = 1;
    }

    @Override
    protected void play()
    {
        if (!isFighting())
        {
            ArrayList<Entity> nearestDeserterAndHorsemen = new ArrayList<>() {{
                add(getNearestEnemyDeserter());
                add(getNearestAllyHorsemen());
            }};

            MovableEntity target = (MovableEntity) getNearestEntity(nearestDeserterAndHorsemen);
            if (target == null)
            {
                return;
            }

            // if the Horsemen ally is closer than the enemy Deserter
            if (target instanceof Horsemen)
            {

                // if the distance between this Horsemen and the Horsement ally is not respected
                if (getDistance(this.position, target.position) + 1 < safetyDistance)
                {
                    // MoveAway from Horsemen ally
                    runAwayFrom(target);
                    System.out.println(getSide() + " Horsemen running away from : " + target);
                }
                else
                {
                    // Go to Horsemen ally
                    goToEntity(target);
                    System.out.println(getSide() + " Horsemen go to : " + target);
                }
            }
            else if (target instanceof Deserter)
            {

                if (!isCloseToEntity(target))
                {
                    goToEntity(target);
                }
            }
        }
        else
        {
            System.out.println(this + " is fighting");
            fightRandomAdjacentUnity();
        }
    }
    private Horsemen getNearestAllyHorsemen()
    {
        ArrayList<Entity> alliesHorsemen = new ArrayList<>();

        for (MovableEntity unity : gameBoard.unityList)
        {
            if (unity instanceof Horsemen)
            {
                if (unity.getSide().equals(this.getSide()) && !unity.equals(this))
                {
                    alliesHorsemen.add(unity);
                }
            }
        }
        
        return (Horsemen) getNearestEntity(alliesHorsemen);
    }

    private Deserter getNearestEnemyDeserter()
    {
        ArrayList<Entity> availableDeserter = new ArrayList<>();

        for (MovableEntity unity : gameBoard.unityList)
        {
            if (unity instanceof Deserter)
            {
                if (!unity.getSide().equals(this.getSide()))
                {
                    availableDeserter.add(unity);
                }
            }
        }
        
        return (Deserter) getNearestEntity(availableDeserter);
    }
}
