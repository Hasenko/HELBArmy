public class City extends Entity {
    private int totalLogs;

    public City(int x, int y, String side)
    {
        super(x, y, side);
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
