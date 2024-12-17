public class Pikemen extends MovableEntity {
    public static int sharedVision = 0;
    private final Position positionToHold;
    public Pikemen(Position position, String side, HELBArmy gameBoard)
    {
        super(position, side, "assets/unity/" + side + "_pikemen.png", gameBoard, 175, 15, 3.0);
        positionToHold = getUniquePosition();
        sharedVision++;
    }

    @Override
    public void destroy() {
        sharedVision--;
        super.destroy();
    }

    private boolean isInPositionToHold()
    {
        return this.position.equals(positionToHold);
    }

    @Override
    protected void moveAction()
    {

        if (hasEnemyInRadius(sharedVision))
        {
            MovableEntity target = getNearestEnemy();
            goToEntity(target);
        }
        else if (!isInPositionToHold())
            goToPosition(positionToHold);
    }

    @Override
    public String toString() {
        return "| " + getClass().getName() + " | " + super.toString() + " position to hold : " + positionToHold + " actual vision : " + sharedVision + " |";
    }
    
}