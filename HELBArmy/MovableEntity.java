import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class MovableEntity extends Entity {
    private HashMap<Class<? extends MovableEntity>, Double> attackMultiplicatorMap = new HashMap<>(); // hashmap to store attack multiplicator from units
    private boolean isInvincible = false;
    private double hp;
    private double damage;
    private City city;

    public static HashMap<Class<? extends MovableEntity>, Boolean> unitsAllowedToMove = new HashMap<>(); // used to know if an unit is allowed to move (defaut = true)

    public MovableEntity(Position position, String side, String imagePath, HELBArmy gameBoard, double hp, double damage)
    {
        super(position, side, imagePath, gameBoard);
        this.hp = hp;
        this.damage = damage;
        this.city = gameBoard.citiesMap.get(side);
    }

    public double getHp()
    {
        return hp;
    }

    // increase hp of a units
    public void increaseHp(double hpToIncrease)
    {
        hp += hpToIncrease;
    }

    // make a units invincible
    public void makeInvincible()
    {
        this.hp = Double.MAX_VALUE;
        this.isInvincible = true;
    }

    protected City getCity()
    {
        return city;
    }

    // called by units that have a multiplicator attack on constructor of caller
    protected void setAttackMultiplicator(HashMap<Class<? extends MovableEntity>, Double> attackMultiplicatorMap)
    {
        this.attackMultiplicatorMap = attackMultiplicatorMap;
    }

    // method to perform action of a movable entity
    public void play()
    {
        if (!unitsAllowedToMove.get(this.getClass()))
            return;

        if (!isAdjacentToAnEntity())
        {
            for (Collectable collectable : gameBoard.collectablesList) {
                if (this.position.equals(collectable.position))
                {
                    collectable.onCollectAction(this);
                }
            }

            if (Flag.isFlagOnMap)
            {
                goToEntity(gameBoard.getFlag());

                if (this.position.equals(gameBoard.getFlag().position))
                {
                    gameBoard.getFlag().onCollectAction(this);
                }

                return;
            }

            moveAction();
        }
        else
        {
            fightRandomAdjacentUnity();
        }
    }

    protected abstract void moveAction();

    protected void fightRandomAdjacentUnity()
    {
        ArrayList<MovableEntity> adjacentEnemyUnity = getAdjacentEnemyUnits();
        int enemyToHitIndex = new Random().nextInt(adjacentEnemyUnity.size());

        hit(adjacentEnemyUnity.get(enemyToHitIndex));
    }

    // caller go to entity position
    protected void goToEntity(Entity entity)
    {
        goToPosition(entity.position);
    }


    protected void goToPosition(Position posTarget)
    {
        Position nextPosition = getNextPositionForTarget(posTarget);
        if (Board.isPositionInBoard(nextPosition))
            this.position = nextPosition;
    }

    // caller go to the oposite direction of target
    protected void runAwayFrom(MovableEntity target)
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

    // method to get the nearest entity from caller from a list given
    protected Entity getNearestEntity(ArrayList<Entity> entityList)
    {
        if (entityList.size() == 0) return null;

        Entity nearestEntity = entityList.get(0);
        double minDistance = Double.MAX_VALUE;

        for (Entity entity : entityList)
        {
            if (entity != null)
            {
                double currentDistance = Board.getDistance(entity.position, this.position);

                if (currentDistance <= minDistance)
                {
                    minDistance = currentDistance;
                    nearestEntity = entity;
                }
            }
        }
        return nearestEntity;
    }

    protected MovableEntity getNearestEnemy()
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

    // method to know if an enemy is in the vision of caller
    protected boolean hasEnemyInRadius(int vision)
    {
        return getEnemyUnitsInRadius(vision).size() > 0;
    }

    // get best position to go from a position A to position B 
    private Position getNextPositionForTarget(Position pos)
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

        // no position available, stay still
        return this.position;
    }

    // get enemy units in vision of caller
    private ArrayList<MovableEntity> getEnemyUnitsInRadius(int visionRange) {
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

    // get enemy units that are adjacent to caller
    private ArrayList<MovableEntity> getAdjacentEnemyUnits()
    {
        return getEnemyUnitsInRadius(this.DEFAULT_ADJACENT_RADIUS);
    }
    
    // method to perform damage to an unity
    private void hit(MovableEntity unity)
    {
        double attackMultiplicator = 1.0;
        if (attackMultiplicatorMap.containsKey(unity.getClass()))
        {
            attackMultiplicator = attackMultiplicatorMap.get(unity.getClass());
        }

        unity.decreaseHp(damage * attackMultiplicator);
    }

    private void decreaseHp(double hpToDecrease)
    {
        if (isInvincible)
            return;

        hp -= hpToDecrease;

        if (hp <= 0 && !gameBoard.unityToDestroyList.contains(this))
        {
            destroy();
        }
    }

    // method to know if caller has enemy in his adjacent position
    private boolean isAdjacentToAnEntity()
    {
        return getAdjacentEnemyUnits().size() > 0;
    }
}