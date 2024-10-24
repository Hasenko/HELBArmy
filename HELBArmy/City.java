public class City extends Entity {
    private int totalLogs;

    public City(int x, int y, String side, HELBArmy gameBoard)
    {
        super(x, y, side, 5, "assets/city/" + side + "_city.png", gameBoard);

        totalLogs = 0;
    }

    public void generateUnity()
    {
        gameBoard.entityList.add(new Collector(getUnityExitX(), getUnityExitY(), this.getSide(), gameBoard));
    }

    public int getUnityExitX()
    {
        return this.x + this.getWidth() / 2;
    }

    public int getUnityExitY()
    {
        if (getSide().equals("north"))
            return this.y + this.getWidth();
        return this.y - 1;
    }

    public int getLogsDepositX()
    {
        if (getSide().equals("north"))
            return this.x + this.getWidth();
        return this.x - 1;
    }

    public int getLogsDepositY()
    {
        return this.y + this.getWidth() / 2;
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
