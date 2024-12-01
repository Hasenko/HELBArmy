public class Pikemen extends MovableEntity {
    private int range = 1;
    private final Position positionToHold;
    public Pikemen(Position position, String side, HELBArmy gameBoard)
    {
        super(position, side, "assets/unity/" + side + "_pikemen.png", gameBoard, 175, 15, 3.0);
        positionToHold = gameBoard.getUniquePosition();
    }
    @Override
    protected void play() {
        if (!isInPosition(positionToHold))
            goToPosition(positionToHold);
    }

    @Override
    protected void moveAction()
    {

    }

    @Override
    public String toString() {
        return "| " + getClass().getName() + " | " + super.toString() + " position to hold : " + positionToHold + " actual range : " + range + " |";
    }
    
}