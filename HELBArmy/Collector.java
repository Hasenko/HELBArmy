import java.util.ArrayList;

public class Collector extends MovableEntity {
    private final int MAX_LOG = 25;
    private final int DAMAGE_TO_TREE = 1;
    
    private int currentLog;
    private Position logDepositPosition;

    public Collector(Position position, String side, HELBArmy gameBoard) {
        super(position, side, "assets/unity/" + side + "_collector.png", gameBoard, 50, 5);
        this.currentLog = 0;
        this.logDepositPosition = new Position(getCity().getLogsDepositX(), getCity().getLogsDepositY());
    }

    @Override
    protected void moveAction()
    {
        if (currentLog == MAX_LOG) // collector is full
        {
            if (!this.logDepositPosition.equals(this.position))
            {
                goToPosition(this.logDepositPosition);
            }
            else
            {
                dropLogInLogDeposit();
            }
        }
        else // collector is not full
        {
            Tree nearestTree = getNearestTree();

            if (nearestTree == null) // no tree available to go
            {
                return;
            }

            if (!this.isAdjacentToEntity(nearestTree)) // collector is not in a position to hit the tree
            {
                goToEntity(nearestTree); // got to a position to hit the tree
            }
            else
            {
                cutTree(nearestTree);
            }

        }
    }

    private void cutTree(Tree tree)
    {
        currentLog += tree.decreaseLog(DAMAGE_TO_TREE);
    }

    private void dropLogInLogDeposit()
    {
        this.getCity().dropLogs(currentLog);
        currentLog = 0;
    }

    private Tree getNearestTree()
    {
        ArrayList<Entity> availableTree = new ArrayList<>();

        for (Tree tree : gameBoard.treesList) {
            if(tree.exist && tree.getAccessibleAdjacentPositions().size() > 0) availableTree.add(tree);
        }

        return (Tree) getNearestEntity(availableTree);
    }
    
    @Override
    public String toString() {
        return "| " + getClass().getName() + " | " + super.toString() + " | current log : " + currentLog + " |";
    }
}