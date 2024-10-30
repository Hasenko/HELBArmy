import java.util.ArrayList;

public class City extends Entity {
    private int totalLogs;
    private ArrayList<Position> positions;

    public City(Position position, String side, HELBArmy gameBoard)
    {
        super(position, side, 5, "assets/city/" + side + "_city.png", gameBoard);

        totalLogs = 0;
        setPositions();
    }

    private void setPositions()
    {
        positions = new ArrayList<>();

        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                positions.add(new Position(this.position.x + i, this.position.y + j));
            }
        }
        
    }

    public ArrayList<Position> getPositions()
    {
        return positions;
    }

    public void generateUnity()
    {
        Collector collector = new Collector(new Position(getUnityExitX(), getUnityExitY()), this.getSide(), gameBoard);
        gameBoard.entityList.add(collector);
        gameBoard.unityList.add(collector);
    }

    public void dropLogs(int nb)
    {
        this.totalLogs += nb;
    }

    public int getUnityExitX()
    {
        return this.position.x + this.getWidth() / 2;
    }

    public int getUnityExitY()
    {
        if (getSide().equals("north"))
            return this.position.y + this.getWidth();
        return this.position.y - 1;
    }

    public int getLogsDepositX()
    {
        if (getSide().equals("north"))
            return this.position.x + this.getWidth();
        return this.position.x - 1;
    }

    public int getLogsDepositY()
    {
        return this.position.y + this.getWidth() / 2;
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
