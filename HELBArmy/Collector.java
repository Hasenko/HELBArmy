public class Collector extends MovableEntity {
    private int currentLog;

    public Collector(int x, int y, String side, HELBArmy gameBoard) {
        super(x, y, side, "assets/unity/" + side + "_collector.png", gameBoard, 50, 5);
        goToNearestTree();
    }
    
    public void goToNearestTree()
    {
        // √((x_2-x_1)²+(y_2-y_1)²)

        double[] distancesFromTreesAndThisCollector = new double[gameBoard.treesList.length];
        
        for (int i = 0; i < gameBoard.treesList.length; i++) {
            distancesFromTreesAndThisCollector[i] = Math.sqrt((Math.pow(gameBoard.treesList[i].x - this.x, 2) + (Math.pow(gameBoard.treesList[i].y - this.y, 2))));
        }

        System.out.println("------------------------");

        for (int i = 0; i < gameBoard.treesList.length; i++) {
            System.out.println("Tree : " + gameBoard.treesList[i] + " | Distance from " + getSide() +" collector : " + distancesFromTreesAndThisCollector[i]);
        }

    }

    public void goToLogDeposite()
    {

    }

    public void dropLogInDeposite()
    {

    }

    @Override
    protected void hit(Entity entity) {
        super.hit(entity);
    }
}
