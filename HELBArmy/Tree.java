import java.util.Date;
import java.util.Random;

public class Tree extends Entity {
    public long respawnTime = 0;

    private final int MAX_LOG = 100;
    private final int DEFAULT_LOG;
    private final int DEFAULT_RESPAWN_TIME = 30000;

    private int log;

    public Tree(Position position, HELBArmy gameBoard)
    {
        super(position, "assets/special/tree.png", gameBoard);
        this.log = new Random().nextInt(MAX_LOG + 1);
        // this.log = 100;
        this.DEFAULT_LOG = log;
    }

    /*
        decreaseLog(int nb)
            -> remove nb from log and return how many log was retrieved
            ex :
                log = 58, nb = 15
                return 58 - (58 - 15) = 15;
                
    */
    public int decreaseLog(int nb)
    {
        int baseLog = log;
        log -= nb;

        if (log <= 0)
        {
            log = 0;
            this.gameBoard.entityList.remove(this);
            respawnTime = new Date().getTime() + DEFAULT_RESPAWN_TIME;
        }
        return baseLog - log;
    }

    /*
        method called when the tree respawn after 30 sec
        should get his base initial log and position.
    */
    public void revive()
    {
        log = DEFAULT_LOG;
    }

    public boolean hasCollisionWithAnEntity()
    {
        for (Entity entity : gameBoard.entityList) {
            if (this.position.equals(entity.position))
                return true;
        }
        return false;
    }

    public boolean isAvailable()
    {
        return log > 0;
    }

    @Override
    public String toString()
    {
        return "| " + getClass().getName() + " | pos : " + super.toString() + " | log : " + log + " |";
    }

}
