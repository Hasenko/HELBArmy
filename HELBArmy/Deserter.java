import java.util.ArrayList;

public class Deserter extends MovableEntity{

    public Deserter(Position position, String side, HELBArmy gameBoard) {
        super(position, side, "assets/unity/" + side + "_deserter.png", gameBoard, 125, 10, 1.5);
    }

    @Override
    protected void play() {
        if (!isFighting())
        {
            Collector nearestEnemyCollector = getNearestEnemyCollector();

            if (nearestEnemyCollector == null)
            {
                return;
            }

            ArrayList<Entity> nearestCollectorAndHorsemen = new ArrayList<>() {{
                    add(getNearestEnemyHorsemen());
                    add(getNearestEnemyCollector());
            }};

            MovableEntity target = (MovableEntity) getNearestEntity(nearestCollectorAndHorsemen);
                
            if (target instanceof Collector)
            {
                if (!isCloseToEntity(target))
                {
                    goToEntity(target);
                }
            }
            else if (target instanceof Horsemen)
            {
                System.out.println(this + " running away from " + target);
                runAwayFrom(target);
            }
        }
        else
        {
            System.out.println(this + " is fighting");
            fightRandomAdjacentUnity();
        }
    }

    private Collector getNearestEnemyCollector()
    {
        ArrayList<Entity> availableCollector = new ArrayList<>();

        for (MovableEntity unity : gameBoard.unityList)
        {
            if (unity instanceof Collector)
            {
                if (!unity.getSide().equals(this.getSide()))
                {
                    availableCollector.add(unity);
                }
            }
        }
        
        return (Collector) getNearestEntity(availableCollector);
    }

    private Horsemen getNearestEnemyHorsemen()
    {
        ArrayList<Entity> availableHorsemen = new ArrayList<>();

        for (MovableEntity unity : gameBoard.unityList)
        {
            if (unity instanceof Horsemen)
            {
                if (!unity.getSide().equals(this.getSide()))
                {
                    availableHorsemen.add(unity);
                }
            }
        }
        
        return (Horsemen) getNearestEntity(availableHorsemen);
    }
    
    @Override
    public String toString() {
        return getClass().getName() + " | " + super.toString();
    }
}
