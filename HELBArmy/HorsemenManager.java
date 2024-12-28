import java.util.HashMap;
/*
    Class to handle safety distance of Horsemen
*/
public class HorsemenManager {
    // Map to store safety distance by sides (ex : 'north' -> 9, if Horsemen from north side are not fighting for 9 ticks)
    public static HashMap<String, Integer> safetyDistanceHorsemenMap = new HashMap<>();

    // Map to store number of Horsemen created by sides (ex : 'north' -> 4, if there are 2 Horsemen on north side)
    public static HashMap<String, Integer> instanceOfHorsemenMap = new HashMap<>();

    // Map to store number of Horsemen that are not fighting in current tick (ex : 'north' -> 4, if there are 4 Horsemen on north side that are not fighting in the current tick)
    private static HashMap<String, Integer> safeCounterHorsemenMap = new HashMap<>();

    // Called when the class is loaded by the JVM
    static {
        // Put default value on map to avoid null pointer exception
        safetyDistanceHorsemenMap.put("north", 1);
        instanceOfHorsemenMap.put("north", 0);
        safeCounterHorsemenMap.put("north", 0);

        safetyDistanceHorsemenMap.put("south", 1);
        instanceOfHorsemenMap.put("south", 0);
        safeCounterHorsemenMap.put("south", 0);
    }

    // Called on Horsemen contructor
    public static void addInstance(String side)
    {
        instanceOfHorsemenMap.put(side, instanceOfHorsemenMap.get(side) + 1);
    }

    // Called when an Horsemen die
    public static void removeInstance(String side)
    {
        instanceOfHorsemenMap.put(side, instanceOfHorsemenMap.get(side) - 1);
    }

    // Method to get safety distance for Horsemen of a side
    public static int getSafetyDistance(String side)
    {
        return safetyDistanceHorsemenMap.get(side);
    }

    /*
        Method called by Horsemen when they are fighting
        -> put safety distance of caller side to 1
    */
    public static void addFightingHorsemen(String side)
    {
        safetyDistanceHorsemenMap.put(side, 1);
    }

    /*
        Method called by Horsemen when they are not fighting during a tick
        -> increment a counter for there side that is used to know if all Horsemn of his side are safe (so they can increment there safety distance)
    */
    public static void addOneSafeHorsemen(String side)
    {
        safeCounterHorsemenMap.put(side, safeCounterHorsemenMap.get(side) + 1);

        if (areHorsemenFromSideSafe(side)) // Check if all horsemen a the side are safe (so they can increment there safety distance)
        {
            increaseSafetyDistanceByOne(side);
            safeCounterHorsemenMap.put(side, 0);
        }
    }

    // Method to know every Horsemen from a specified side are safe (not fighting)
    private static boolean areHorsemenFromSideSafe(String side)
    {
        return safeCounterHorsemenMap.get(side) >= instanceOfHorsemenMap.get(side);
    }

    // increment safety distance by 1
    private static void increaseSafetyDistanceByOne(String side)
    {
        safetyDistanceHorsemenMap.put(side, safetyDistanceHorsemenMap.get(side) + 1);
    }
}
