import java.util.Random;

public class Tree extends Entity{
    private int log;

    public Tree(int x, int y, HELBArmy gameBoard)
    {
        super(x, y, "assets/special/tree.png", gameBoard);
        this.log = new Random().nextInt(101);
    }

    public void decreaseLog(int nb)
    {
        log -= nb;
    }

    public int getLog()
    {
        return log;
    }

    @Override
    public String toString()
    {
        return super.toString() + "; log : " + log + "]";
    }


}
