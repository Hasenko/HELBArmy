import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class MovableEntity extends Entity {
    /*
        Collecteur PV 50 Attaque 5
        Déserteur PV 125 Attaque 10 x1,5 contre les piquiers
        Cavalier PV 200 Attaque 10 x2 contre les déserteurs
        Piquier PV 175 Attaque 15 x3 contre les cavaliers
    */

    private int hp;
    private int damage;
    private double attackMultiplicator;
    private City city;

    public MovableEntity(Position position, String side, String imagePath, HELBArmy gameBoard, int hp, int damage)
    {
        this(position, side, imagePath, gameBoard, hp, damage, 1.0);
    }

    public MovableEntity(Position position, String side, String imagePath, HELBArmy gameBoard, int hp, int damage, double attackMultiplicator)
    {
        super(position, side, imagePath, gameBoard);
        this.hp = hp;
        this.damage = damage;
        this.attackMultiplicator = attackMultiplicator;
        this.city = gameBoard.citiesMap.get(side);
    }
    
    public int getHp()
    {
        return hp;
    }

    public int getDamage()
    {
        return damage;
    }

    public double getAttackMultiplicator()
    {
        return attackMultiplicator;
    }

    public void decreaseHp(int nb)
    {
        hp -= nb;

        if (hp <= 0 && !gameBoard.unityToDestroyList.contains(this))
        {
            destroy();
        }
    }

    public City getCity()
    {
        return city;
    }

    public void destroy() {
        System.out.println(this + " died");
        gameBoard.removeNext(this);
    }

    protected void goToPosition(Position posTarget)
    {
        Position nextPosition = getNextPositionForTarget(posTarget);
        if (Board.isPositionInBoard(nextPosition))
            this.position = nextPosition;
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
            double minDistance = Board.getDistance(pos, positions.get(0));

            for (Position possiblePosition : positions) {
                double currentDistance = Board.getDistance(pos, possiblePosition);
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

    public MovableEntity getNearestEnemy()
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

    public boolean hasEnemyInRadius(int vision)
    {
        return getEnemyUnitsInRadius(vision).size() > 0;
    }

    public ArrayList<MovableEntity> getEnemyUnitsInRadius(int visionRange) {
        ArrayList<MovableEntity> unitsInVision = new ArrayList<>();
        ArrayList<Position> positionsInVision = getPositionsInRadius(visionRange);

        for (MovableEntity unity : gameBoard.unityList) {
            if (!unity.getSide().equals(this.getSide()))
            {
                if(positionsInVision.contains(unity.position))
                {
                    unitsInVision.add(unity);
                }
            }
        }

        return unitsInVision;
    }
    public ArrayList<MovableEntity> getAdjacentEnemyUnity()
    {
        return getEnemyUnitsInRadius(1);
    }

    /*
        method to perform action of a movable entity (specific for every mv entity)
    */
    protected void play()
    {
        if (!isAdjacentToAnEntity())
        {
            moveAction();
        }
        else
        {
            fightRandomAdjacentUnity();
        }
    }

    protected abstract void moveAction();
    /*
        method to hit an unity
    */
    protected void hit(MovableEntity unity)
    {
        unity.decreaseHp(damage);
    }

    protected boolean isAdjacentToAnEntity()
    {
        return getAdjacentEnemyUnity().size() > 0;
    }

    protected void fightRandomAdjacentUnity()
    {
        ArrayList<MovableEntity> adjacentEnemyUnity = getAdjacentEnemyUnity();
        int enemyToHitIndex = new Random().nextInt(adjacentEnemyUnity.size());

        hit(adjacentEnemyUnity.get(enemyToHitIndex));
    }

    public void runAwayFrom(MovableEntity target)
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
}