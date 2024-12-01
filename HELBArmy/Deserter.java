import java.util.ArrayList;

public class Deserter extends MovableEntity{

    public Deserter(Position position, String side, HELBArmy gameBoard) {
        super(position, side, "assets/unity/" + side + "_deserter.png", gameBoard, 125, 10, 1.5);
    }

    @Override
    protected void moveAction()
    {
        MovableEntity target = getNearestEnemy();
                
        if (target == null)
        {
            return;
        }

        if (target instanceof Collector)
        {
            if (!isCloseToEntity(target))
            {
                goToEntity(target);
            }
        }
        else
        {
            runAwayFrom(target);
        }    
    }

    private MovableEntity getNearestEnemy()
    {
        ArrayList<Entity> availableEnemy = new ArrayList<>();

        for (MovableEntity unity : gameBoard.unityList)
        {
            if (!unity.getSide().equals(this.getSide()))
            {
                availableEnemy.add(unity);
            }
        }
        
        return (MovableEntity) getNearestEntity(availableEnemy);
    }
    
    @Override
    public String toString() {
        return "| " + getClass().getName() + " | " + super.toString() + " |";
    }
}
