public class Deserter extends MovableEntity{

    public Deserter(Position position, String side, HELBArmy gameBoard) {
        super(position, side, "assets/unity/" + side + "_deserter.png", gameBoard, 125, 10, 1.5);
    }

    @Override
    protected void moveAction()
    {
        MovableEntity target = getNearestEnemy();
                
        if (target == null) // no enemy on the board
        {
            return;
        }

        if (target instanceof Collector)
        {
            if (!this.isAdjacentToEntity(target))
            {
                goToEntity(target);
            }
        }
        else
        {
            runAwayFrom(target);
        }    
    }
    
    @Override
    public String toString() {
        return "| " + getClass().getName() + " | " + super.toString() + " |";
    }
}
