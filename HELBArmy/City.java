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
    private ArrayList<Position> positions; // ArrayList to store all position taken by the city

    private long nextGeneratorUpdate; // timestamp for when the city can generate an units
    private MovableEntity newUnits; // new units that will be generate
    private boolean canGenerateUnits = false; // to know if the city can generate units

    private Class<? extends MovableEntity> classNameUnitsToGenerate; // to know what movable entity will be generate

    public City(Position position, String side, HELBArmy gameBoard)
    {
        super(position, side, 5, "assets/city/" + side + "_city.png", gameBoard);

        totalLogs = 0;
        setPositions();

        nextGeneratorUpdate = 0;
    }

    // Method called by controller to call generation logics
    public void generateUnity(long currentTime)
    {
        if (currentTime >= nextGeneratorUpdate) // if time has passed
        {
            if (canGenerateUnits) // to avoid null pointer will className
            {
                generateUnity(classNameUnitsToGenerate);
                canGenerateUnits = false;
            }

            choseUnitsToGenerate();
        }
    }

    /*
        Method to generate a new units and add it in entiies list.
        > get : className | find constructor and generate the units
    */
    public void generateUnity(Class<? extends MovableEntity> className)
    {
        try {
            newUnits = className.getConstructor(Position.class, String.class, HELBArmy.class)
                                .newInstance(
                                    new Position(getUnityExitX(), getUnityExitY()),
                                    this.getSide(),
                                    gameBoard
                                );

            System.out.println("-------------------------------------------------");
            System.out.println(toString() + " has generate " + newUnits.getClass().getName());
            System.out.println("-------------------------------------------------");
            gameBoard.entityList.add(newUnits);
            gameBoard.unityList.add(newUnits);
        } catch (Exception  e) {
            e.printStackTrace();
        }
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

    public ArrayList<Position> getPositions()
    {
        return positions;
    }

    // Called when a collector drop his log inventory
    public void dropLogs(int nb)
    {
        this.totalLogs += nb;
    }

    // Method to chose the units that will be generated when possible (based on total logs of city)
    private void choseUnitsToGenerate() {
        canGenerateUnits = true;
        int possibility = 0;

        for (int[] cost : UNITS_COSTS)
        {
            int logsCost = cost[0];

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
            classNameUnitsToGenerate = Collector.class;
        }
        else if (unitsIndex == 1)
        {
            classNameUnitsToGenerate = Deserter.class;
        }
        else if (unitsIndex == 2)
        {
            classNameUnitsToGenerate = Pikemen.class;
        }
        else if (unitsIndex == 3)
        {
            classNameUnitsToGenerate = Horsemen.class;
        }

        nextGeneratorUpdate = new Date().getTime() + UNITS_COSTS[unitsIndex][1];
        totalLogs -= UNITS_COSTS[unitsIndex][0];
    }

    // calculate positions of the current city
    private void setPositions()
    {
        positions = new ArrayList<>();

        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                positions.add(new Position(this.position.x + i, this.position.y + j));
            }
        }

    }

    private int getUnityExitX()
    {
        return this.position.x + this.getWidth() / 2;
    }

    private int getUnityExitY()
    {
        if (getSide().equals("north"))
            return this.position.y + this.getWidth();
        return this.position.y - 1;
    }

    @Override
    public String toString()
    {
        return "| " + getSide() + " city | logs : " + totalLogs + " |";
    }

}
