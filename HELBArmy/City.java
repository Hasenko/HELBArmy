public class City extends Entity {
    private int totalLogs;
    private final String[] IMAGE_PATHS = {"assets/city/north_city.png", "assets/city/south_city.png"};

    public City(int x, int y, String side)
    {
        super(x, y, side);
        if (side.equals("north"))
        {
            setImagePath(IMAGE_PATHS[0]);
        }
        else if (side.equals("south"))
        {
            setImagePath(IMAGE_PATHS[1]);
        }
        else
        {
            throw new IllegalArgumentException("side must be : north - south");
        }

        totalLogs = 0;
    }

    public void generateUnity()
    {

    }

    public int getUnityExitX()
    {
        return super.getX() + 2;
    }

    public int getUnityExitY()
    {
        return super.getY();
    }

    public int getLogsDepositX()
    {
        return super.getX();
    }

    public int getLogsDepositY()
    {
        return super.getY() + 2;
    }

    @Override
    public String toString()
    {
        return super.toString() 
            + "; uX : " + getUnityExitX() 
            + "; uY : " + getUnityExitY() 
            + "; lX : " + getLogsDepositX() 
            + "; lY : " + getLogsDepositY() 
            + "; totalLogs : " + totalLogs
        + "]";
    }

}
