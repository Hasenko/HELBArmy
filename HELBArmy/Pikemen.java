import java.util.ArrayList;

public class Pikemen extends MovableEntity {
    private static int vision = 0;
    private final Position positionToHold;
    public Pikemen(Position position, String side, HELBArmy gameBoard)
    {
        super(position, side, "assets/unity/" + side + "_pikemen.png", gameBoard, 175, 15, 3.0);
        positionToHold = getUniquePosition();
        vision++;
    }

    @Override
    public ArrayList<Position> getAdjacentPositions() {
        ArrayList<Position> resultList = new ArrayList<Position>();

        for(int i = this.position.x-vision ;  i <= this.position.x+vision ; i++){
            for(int j = this.position.y-vision ;  j <= this.position.y+vision ; j++){
                if(!(i == this.position.x && j == this.position.y)){
                    resultList.add(new Position(i, j));
                }
            }     
        }

        return resultList;
    }
    
    @Override
    public void destroy() {
        vision--;
        super.destroy();
    }
    /*
        Method to know is a position in already taken by an entity on the board.
        iterate trough entityList and treesList
    */
    private boolean isPositionTakenByEntity(Position pos)
    {
        boolean response = false;

        for (Entity entity : gameBoard.entityList) {
            if (entity.position.equals(pos))
            {
                response = true;
                break;
            }
        }

        for (Tree tree : gameBoard.treesList)
        {
            if (tree.position.equals(pos))
            {
                response = true;
                break;
            }
        }

        return response;

    }
    /*
        Method to get a position taken by no entities (tree, movable entities, cities, ...)
    */
    private Position getUniquePosition() {
        Position pos = new Position((int) (Math.random() * HELBArmy.ROWS), (int) (Math.random() * HELBArmy.COLUMNS));

        // generate random position that is not in city area
        while (gameBoard.isPositionInCity(pos) || isPositionTakenByEntity(pos))
        {
            pos = new Position((int) (Math.random() * HELBArmy.ROWS), (int) (Math.random() * HELBArmy.COLUMNS));
        }

        return pos;
    }

    private boolean isInPositionToHold()
    {
        return this.position.equals(positionToHold);
    }
    @Override
    protected void moveAction()
    {
        if (!isInPositionToHold())
            goToPosition(positionToHold);
    }

    @Override
    public String toString() {
        return "| " + getClass().getName() + " | " + super.toString() + " position to hold : " + positionToHold + " actual vision : " + vision + " |";
    }
    
}