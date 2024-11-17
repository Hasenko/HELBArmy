import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class City extends Entity {
    /*
        Collecteur : 0 bois - 5 secondes
        Déserteur : 50 bois - 10 secondes
        Cavalier : 100 bois - 15 secondes
        Piquier : 75 bois - 5 secondes
    */

    private final int COLLECTOR_LOGS_COST = 0;
    private final int DESERTER_LOGS_COST = 50;
    private final int PIKEMEN_LOGS_COST = 75;
    private final int HORSEMEN_LOGS_COST = 100;

    private final int COLLECTOR_TIME_COST = 5000; // ms
    private final int DESERTER_TIME_COST = 10000; // ms
    private final int PIKEMEN_TIME_COST = 5000; // ms
    private final int HORSEMEN_TIME_COST = 15000; // ms

    private final int[][] UNITS_COSTS = new int[][]
    {
        {COLLECTOR_LOGS_COST, COLLECTOR_TIME_COST},
        {DESERTER_LOGS_COST, DESERTER_TIME_COST},
        {PIKEMEN_LOGS_COST, PIKEMEN_TIME_COST},
        {HORSEMEN_LOGS_COST, HORSEMEN_TIME_COST}
    };

    private int totalLogs;
    private ArrayList<Position> positions;

    private long nextGeneratorUpdate;
    private MovableEntity newUnits = null;

    public City(Position position, String side, HELBArmy gameBoard)
    {
        super(position, side, 5, "assets/city/" + side + "_city.png", gameBoard);

        totalLogs = 0;
        setPositions();

        nextGeneratorUpdate = 0;
    }

    public void generateUnity(long currentTime)
    {
        if (currentTime >= nextGeneratorUpdate) // generate new units
        {
            if (newUnits != null)
            {
                gameBoard.entityList.add(newUnits);
                gameBoard.unityList.add(newUnits);
                newUnits = null;
            }

            choseUnitsToGenerate();
        }
        else
        {
            // System.out.println("Can't generate units now | current time : " + currentTime + " | nex generator update : " + nextGeneratorUpdate);
        }
    }

    private void generateCollector()
    {
        System.out.println("---------------------");
        System.out.println("Generating Collector !");
        System.out.println("---------------------");

        newUnits = new Collector(new Position(getUnityExitX(), getUnityExitY()), this.getSide(), gameBoard);
    }

    private void generateDeserter()
    {
        System.out.println("---------------------");
        System.out.println("Generating Deserter !");
        System.out.println("---------------------");
        newUnits = new Deserter(new Position(getUnityExitX(), getUnityExitY()), this.getSide(), gameBoard);
    }

    private void generateHorsemen()
    {
        System.out.println("---------------------");
        System.out.println("Generating Horsemen !");
        System.out.println("---------------------");
        newUnits = new Horsemen(new Position(getUnityExitX(), getUnityExitY()), this.getSide(), gameBoard);
    }
    
    private void generatePikemen()
    {
        System.out.println("---------------------");
        System.out.println("Generating Pikemen !");
        System.out.println("---------------------");
    }

    public void choseUnitsToGenerate() {
        int possibility = 0;

        for (int[] cost : UNITS_COSTS)
        {
            int logsCost = cost[0];
            int timeCost = cost[1];

            if (totalLogs >= logsCost) // if we can generat the current units
            {
                possibility++;
            }
        }

        int unitsIndex = new Random().nextInt(possibility);

        /*
            unitsIndex
                0 -> Collector
                1 -> Deserter
                2 -> Pikemen
                3 -> Horsemen
        */

        if (unitsIndex == 0)
        {
            generateCollector();
        }
        else if (unitsIndex == 1)
        {
            generateDeserter();
        }
        else if (unitsIndex == 2)
        {
            generatePikemen();
        }
        else if (unitsIndex == 3)
        {
            generateHorsemen();
        }

        nextGeneratorUpdate = new Date().getTime() + UNITS_COSTS[unitsIndex][1];
        totalLogs -= UNITS_COSTS[unitsIndex][0];

        System.out.println("Total logs : " + totalLogs);
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
