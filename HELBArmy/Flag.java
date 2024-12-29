import java.util.Date;

public class Flag extends Collectable {
    private final int HP_BONUS_RATIO = 50;

    public static final int DEFAULT_RESPAWN_TIME = 120000;
    
    public static long flagRespawnTime = 0;
    public static boolean isFlagOnMap = false;

    public Flag(Position position, HELBArmy gameBoard)
    {
        super(position, "assets/special/flag.png", gameBoard);
    }

    @Override
    protected void onCollectAction(MovableEntity collectorUnity) {
        // Give bonus to all units from collector side
        for (MovableEntity unity : gameBoard.unityList) {
            if (unity.getSide().equals(collectorUnity.getSide()))
            {
                unity.increaseHp(unity.getHp() * HP_BONUS_RATIO/100);
                System.out.println("increasing hp of " + unity + " to : " + unity.getHp());
            }
        }

        flagRespawnTime = new Date().getTime() + DEFAULT_RESPAWN_TIME;
        isFlagOnMap = false;
        super.destroy();
    }
}
