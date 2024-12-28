import java.util.HashMap;

public class PikemenManager {
        public static HashMap<String, Integer> sharedVisionPikemenMap = new HashMap<>();

        static {
            sharedVisionPikemenMap.put("north", 0);
            sharedVisionPikemenMap.put("south", 0);
        }

        public static int getVision(String side)
        {
            return sharedVisionPikemenMap.get(side);
        }

        public static void increaseVisionByOne(String side)
        {
            sharedVisionPikemenMap.put(side, sharedVisionPikemenMap.get(side) + 1);
        }

        public static void decreaseVisionByOne(String side)
        {
            sharedVisionPikemenMap.put(side, sharedVisionPikemenMap.get(side) - 1);
        }

}
