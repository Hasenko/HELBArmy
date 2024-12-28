import java.util.HashMap;

public class Pikemen extends MovableEntity {
    private final Position POSITION_TO_HOLD;

    public Pikemen(Position position, String side, HELBArmy gameBoard)
    {
        super(position, side, "assets/unity/" + side + "_pikemen.png", gameBoard, 175.0, 15.0);
        setAttackMultiplicator(new HashMap<>() {{
            put(Horsemen.class, 3.0);
        }});

        POSITION_TO_HOLD = gameBoard.getUniquePosition();
        PikemenManager.increaseVisionByOne(side);
    }

    @Override
    public void destroy()
    {
        PikemenManager.decreaseVisionByOne(getSide());
        super.destroy();
    }

    @Override
    protected void moveAction()
    {
        if (hasEnemyInRadius(PikemenManager.getVision(getSide()))) // pikemen has enemy in his vision
        {
            MovableEntity target = getNearestEnemy();
            goToEntity(target);
        }
        else if (!isInPositionToHold()) // pikemen is not in his position to hold
            goToPosition(POSITION_TO_HOLD);
    }

    private boolean isInPositionToHold()
    {
        return this.position.equals(POSITION_TO_HOLD);
    }

    @Override
    public String toString() {
        return "| " + getClass().getName() + " | " + super.toString() + " position to hold : " + POSITION_TO_HOLD + " actual vision : " + PikemenManager.getVision(getSide()) + " |";
    }
    
}