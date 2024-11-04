public class Deserter extends MovableEntity{

    public Deserter(Position position, String side, HELBArmy gameBoard) {
        super(position, side, "assets/unity/" + side + "_deserter.png", gameBoard, 125, 10, 1.5);
    }

    @Override
    protected void play() {
        if (!isFighting())
        {
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
                hit(nearestEnemyCollector);
            }
        }
        else
        {
            System.out.println(this + " is fighting");
        }
    }
    
    @Override
    public String toString() {
        return getClass().getName() + " | " + super.toString();
    }
}
