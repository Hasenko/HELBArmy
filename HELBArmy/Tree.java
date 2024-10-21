import java.util.Random;

public class Tree extends Entity{
    private int log;

    public Tree(int x, int y)
    {
        super(x, y, "neutral", "assets/special/tree.png");
        this.log = new Random().nextInt(101);
    }

    public void retrieveLog(int nb)
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
