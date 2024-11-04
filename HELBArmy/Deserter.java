public class Deserter extends MovableEntity{

    public Deserter(Position position, String side, HELBArmy gameBoard) {
        super(position, side, "assets/unity/" + side + "_deserter.png", gameBoard, 125, 10, 1.5);
    }

    @Override
    protected void play() {
        Collector nearestEnemyCollector = getNearestEnemyCollector();

        if (nearestEnemyCollector == null)
        {
            return;
        }

        if (!isCloseToEntity(nearestEnemyCollector))
        {
            goToEntity(nearestEnemyCollector);
        }
        else
        {
            System.out.println(this + " is in position to hit collector : " + nearestEnemyCollector);
            hit(nearestEnemyCollector);
        }
    }
    
}
