import java.util.ArrayList;
import java.util.HashMap;

public class Horsemen extends MovableEntity {
    public Horsemen(Position position, String side, HELBArmy gameBoard)
    {
        super(position, side, "assets/unity/" + side + "_horsemen.png", gameBoard, 200.0, 10.0);
        setAttackMultiplicator(new HashMap<>() {{
            put(Deserter.class, 2.0);
        }});

        HorsemenManager.addInstance(side);
    }
    
    @Override
    protected void moveAction()
    {
        ArrayList<Entity> nearestDeserterAndHorsemen = new ArrayList<>() {{
            add(getNearestEnemyDeserter());
            add(getNearestAllyHorsemen());
        }};

        MovableEntity target = (MovableEntity) getNearestEntity(nearestDeserterAndHorsemen);
        if (target == null) // no enemy deserter or ally horsemen on the board
        {
            return;
        }

        HorsemenManager.addOneSafeHorsemen(getSide());
        
        // if the Horsemen ally is closer than the enemy Deserter
        if (target instanceof Horsemen)
        {

            // if the distance between this Horsemen and the Horsement ally is not respected
            if (Board.getDistance(this.position, target.position) + 1 < HorsemenManager.getSafetyDistance(getSide()))
            {
                // MoveAway from Horsemen ally
                runAwayFrom(target);
            }
            else
            {
                // Go to Horsemen ally
                goToEntity(target);
            }
        }
        else if (target instanceof Deserter)
        {

            if (!this.isAdjacentToEntity(target))
            {
                goToEntity(target);
            }
        }
    }

    @Override
    protected void fightRandomAdjacentUnity() {
        HorsemenManager.addFightingHorsemen(getSide());
        super.fightRandomAdjacentUnity();
    }

    @Override
    public void destroy() {
        HorsemenManager.removeInstance(getSide());
        super.destroy();
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

    @Override
    public String toString() {
        return "| " + getClass().getName() + " | " + super.toString() + " | safety distance : " + HorsemenManager.getSafetyDistance(getSide()) + " |";
    }
}
