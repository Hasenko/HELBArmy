public abstract class Entity {
    private final static int DEFAULT_WIDTH = 1;

    protected int x;
    protected int y;
    
    private String side;
    private int width;
    private String imagePath;

    public HELBArmy gameBoard;
    
    public Entity(int x, int y, String imagePath, HELBArmy gameBoard)
    {
        this(x, y, "neutral", DEFAULT_WIDTH, imagePath, gameBoard);
    }

    public Entity(int x, int y, String side, String imagePath, HELBArmy gameBoard)
    {
        this(x, y, side, DEFAULT_WIDTH, imagePath, gameBoard);
    }

    public Entity(int x, int y, String side, int width, String imagePath, HELBArmy gameBoard)
    {
        this.x = x;
        this.y = y;
        setSide(side);
        this.width = width;
        this.imagePath = imagePath;
        this.gameBoard = gameBoard;
    }

    private void setSide(String side)
    {
        if (side.equals("north") || side.equals("south") || side.equals("neutral"))
            this.side = side;
        else
            throw new IllegalArgumentException("side must be : north - south - neutral");
    }

    public String getSide()
    {
        return side;
    }

    public int getWidth()
    {
        return width;
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

    @Override
    public String toString()
    {
        return "[x : " + x + "; y : " + y;
    }
}
