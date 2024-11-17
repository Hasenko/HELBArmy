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
            Deserter nearestEnemyDeserter = getNearestEnemyDeserter();

            if (nearestEnemyDeserter == null)
            {
                return;
            }

            if (!isCloseToEntity(nearestEnemyDeserter))
            {
                goToEntity(nearestEnemyDeserter);
            }
        }
        else
        {
            System.out.println(this + " is fighting");
            fightRandomAdjacentUnity();
        }
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
