import java.util.HashMap;

public class HorsemenManager {
    public static HashMap<String, Integer> safetyDistanceHorsemen = new HashMap<>();
    public static HashMap<String, Integer> instanceOfHorsemen = new HashMap<>();
    private static HashMap<String, Integer> safeCounterHorsemen = new HashMap<>();

    static {
        safetyDistanceHorsemen.put("north", 1);
        instanceOfHorsemen.put("north", 0);
        safeCounterHorsemen.put("north", 0);

        safetyDistanceHorsemen.put("south", 1);
        instanceOfHorsemen.put("south", 0);
        safeCounterHorsemen.put("south", 0);
    }

    public static int getSafetyDistanceHorsemen(String side)
    {
        Integer nb = safetyDistanceHorsemen.get(side);
        if (nb == null)
            return 0;
        return nb;
    }

    public static int getInstanceOfHorsemen(String side)
    {
        Integer nb = instanceOfHorsemen.get(side);
        if (nb == null)
            return 0;
        return nb;
    }

    public static void removeHorsemen(String side)
    {
        instanceOfHorsemen.put(side, getInstanceOfHorsemen(side) - 1);
    }

    public static void fight(String side)
    {
        safetyDistanceHorsemen.put(side, 1);
    }

    public static void safe(String side)
    {
        safeCounterHorsemen.put(side, getSafeCounterHorsemen(side) + 1);

        if (getSafeCounterHorsemen(side) >= getInstanceOfHorsemen(side))
        {
            incrementSafetyDistanceByOne(side);
            safeCounterHorsemen.put(side, 0);
        }
    }

    public static int getSafeCounterHorsemen(String side)
    {
        Integer nb = safeCounterHorsemen.get(side);
        if (nb == null)
            return 0;
        return nb;
    }

    private static void incrementSafetyDistanceByOne(String side)
    {
        safetyDistanceHorsemen.put(side, getSafetyDistanceHorsemen(side) + 1);
    }
}
