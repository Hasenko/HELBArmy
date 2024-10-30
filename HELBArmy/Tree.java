import java.util.Random;

public class Tree extends Entity{
    private final int DEFAULT_LOG;
    private int log;
    public boolean exist;

    public Tree(Position position, HELBArmy gameBoard)
    {
        super(position, "assets/special/tree.png", gameBoard);
        this.log = new Random().nextInt(101);
        this.DEFAULT_LOG = log;
        exist = true;
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
            exist = false;
            this.gameBoard.entityList.remove(this);
        }
        return baseLog - log;
    }

    public int getLog()
    {
        return log;
    }

    public void revive()
    {
        /*
            method called when the tree respawn after 30 sec
            should get his base initial log and position.
        */

        log = DEFAULT_LOG;
        exist = true;
    }

    @Override
    public String toString()
    {
        return super.toString() + "; log : " + log + "]";
    }


}
