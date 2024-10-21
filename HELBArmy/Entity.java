public abstract class Entity {
    public int x;
    public int y;
    private String side;
    private String imagePath;

        
    public Entity(int x, int y, String side)
    {
        this(x, y, side, "assets/");
    }
    
    public Entity(int x, int y, String side, String imagePath)
    {
        this.x = x;
        this.y = y;
        this.side = side;
        setImagePath(imagePath);
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

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        if (!imagePath.startsWith("assets/"))
        {
            throw new IllegalArgumentException(imagePath + " | Base directory must be : assets/");
        }
        this.imagePath = imagePath;
    }
}
