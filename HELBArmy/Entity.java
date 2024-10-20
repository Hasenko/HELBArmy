public abstract class Entity {
    public int x;
    public int y;
    private String side;

    public Entity(int x, int y, String side)
    {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String getSide()
    {
        return side;
    }

    @Override
    public String toString()
    {
        return "[x : " + x + "; y : " + y;
    }
}
