import java.util.ArrayList;

public class Deserter extends MovableEntity{

    public Deserter(Position position, String side, HELBArmy gameBoard) {
        super(position, side, "assets/unity/" + side + "_deserter.png", gameBoard, 125, 10, 1.5);
    }

    @Override
    protected void play() {
        if (!isFighting())
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

    private void runAwayFrom(MovableEntity target)
    {
        Position positionToGoTo = new Position(this.position.x, this.position.y);

        if (target.position.x > this.position.x)
            positionToGoTo.x = this.position.x - 1;
        
        if (target.position.x < this.position.x)
            positionToGoTo.x = this.position.x + 1;

        if (target.position.y > this.position.y)
            positionToGoTo.y = this.position.y - 1;
        
        if (target.position.y < this.position.y)
            positionToGoTo.y = this.position.y + 1;

        goToPosition(positionToGoTo);
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
        return getClass().getName() + " | " + super.toString();
    }
}
