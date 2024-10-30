import java.util.ArrayList;
import java.util.HashMap;

public abstract class MovableEntity extends Entity {
    /*
        Collecteur PV 50 Attaque 5
        Déserteur PV 125 Attaque 10 x1,5 contre les piquiers
        Cavalier PV 200 Attaque 10 x2 contre les déserteurs
        Piquier PV 175 Attaque 15 x3 contre les cavaliers
    */

    private int hp;
    private int damage;
    private int attackMultiplicator;

    public MovableEntity(Position position, String side, String imagePath, HELBArmy gameBoard, int hp, int damage)
    {
        this(position, side, imagePath, gameBoard, hp, damage, 1);
    }

    public MovableEntity(Position position, String side, String imagePath, HELBArmy gameBoard, int hp, int damage, int attackMultiplicator)
    {
        super(position, side, imagePath, gameBoard);
        this.hp = hp;
        this.damage = damage;
        this.attackMultiplicator = attackMultiplicator;
    }
    
    public int getHp()
    {
        return hp;
    }

    public int getDamage()
    {
        return damage;
    }

    public int getAttackMultiplicator()
    {
        return attackMultiplicator;
    }

    public void decreaseHp(int nb)
    {
        hp -= nb;

        if (hp <= 0)
        {
            destroy();
        }
    }

    private void destroy() {
        gameBoard.entityList.remove(this);
        gameBoard.unityList.remove(this);
    }

    protected void goToPosition(Position posTarget)
    {
        this.position = getNextPositionForTarget(posTarget);
    }

    protected void goToEntity(Entity entity)
    {
        goToPosition(entity.position);
    }

    public Position getNextPositionForTarget(Position pos)
    {
        HashMap<Double, Position> positionDistanceMap = new HashMap<>();
        ArrayList<Position> positions = getAccessibleAdjacentPositions();

        if (positions.size() > 0)
        {
            double minDistance = getDistance(pos, positions.get(0));

            for (Position possiblePosition : positions) {
                double currentDistance = getDistance(pos, possiblePosition);
                if (currentDistance < minDistance)
                {
                    minDistance = currentDistance;
                }
    
                positionDistanceMap.put(currentDistance, possiblePosition);
            }

            return positionDistanceMap.get(minDistance);

        }

        System.out.println("no position available");
        return this.position;
    }

    /*
        method to perform action of a movable entity (specific for every mv entity)
    */
    protected abstract void play();

    /*
        method to hit an unity
    */
    protected void hit(MovableEntity unity)
    {
        unity.decreaseHp(damage);
    }
}