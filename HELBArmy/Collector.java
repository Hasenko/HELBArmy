public class Collector extends MovableEntity {
    private final int MAX_LOG = 25;
    private final int DAMAGE_TO_TREE = 1;
    
    private int currentLog;
    private Position logDepositPosition;

    public Collector(Position position, String side, HELBArmy gameBoard) {
        super(position, side, "assets/unity/" + side + "_collector.png", gameBoard, 50, 5);
        this.currentLog = 0;
        this.logDepositPosition = new Position(gameBoard.citiesMap.get(getSide()).getLogsDepositX(), gameBoard.citiesMap.get(getSide()).getLogsDepositY());
    }

    @Override
    protected void play()
    {
        if (currentLog == MAX_LOG) // collector is full
        {
            if (!isInPosition(this.logDepositPosition))
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
                System.out.println(getSide() + " collector : no tree available");
                return;
            }

            if (!isCloseToEntity(nearestTree)) // collector is not in a position to hit the tree
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
        System.out.println(getSide() + " collector : cutting : " + tree);
        currentLog += tree.decreaseLog(DAMAGE_TO_TREE);
    }

    private void dropLogInLogDeposit()
    {
        System.out.println(getSide() + " collector : dropping logs ...");
        gameBoard.citiesMap.get(getSide()).dropLogs(currentLog);
        currentLog = 0;
    }
}