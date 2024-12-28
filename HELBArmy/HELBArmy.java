import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.util.Duration;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Controller
public class HELBArmy {
    private final double FRAME_RATE = 1000.0;

    private final int PROTECTED_SPACE_BEYOND_CITY = 2;
    private final int CITY_DEFAULT_WIDTH = 5;

    private final double TREE_RATIO = 10; // each cell has [TREE_RATIO]% chance to spawn a tree
    private final int PHILOSOPHERS_STONE_NUMBER = 2;

    private final View view;
    private GraphicsContext gc;

    private long currentTime = 0; // used for timestamp

    private Flag flag;

    protected final int SQUARE_SIZE;

    public static final int ROWS = 50;
    public static final int COLUMNS = ROWS;

    public ArrayList<Entity> entityList = new ArrayList<>(); // ArrayList to store entity
    public ArrayList<MovableEntity> unityList = new ArrayList<>(); // ArrayList to store movable entities
    public ArrayList<Entity> unityToDestroyList = new ArrayList<>(); // ArrayList to store unity to destroy after next iteration
    public ArrayList<Collectable> collectablesList = new ArrayList<>(); // ArrayList to store collectables, such as Flag and Philosophers Stone
    public HashMap<String, City> citiesMap = new HashMap<>(); // HashMap to store city by side
    public ArrayList<Tree> treesList = new ArrayList<>(); // ArrayList to store tree

    /*
        Constructor
        > Setup base of the window
        > Generate city and tree
    */
    public HELBArmy(Stage primaryStage) {
        view = new View(this, primaryStage);
        SQUARE_SIZE = view.getWidth() / ROWS;
        gc = view.getGraphicsContext();

        initGame();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(FRAME_RATE), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        setKeyEvent(view.getScene());
    }

    /*
        Call view to draw element on window
    */
    private void run(GraphicsContext gc)
    {
        currentTime = new Date().getTime();

        // draw map + entity
        view.drawBackground(gc);
        view.drawEntity(gc, entityList);

        // entity play
        for (MovableEntity unity : unityList) {
            unity.play();
        }
        
        // generation of units
        for (Map.Entry<String, City> entry : citiesMap.entrySet())
        {
            entry.getValue().generateUnity(currentTime);
        }

        // remove entity that where destroy
        for (Entity entity : unityToDestroyList) {
            this.entityList.remove(entity);
            this.unityList.remove(entity);
            this.collectablesList.remove(entity);
        }

        // tree respawn
        for (Tree tree : treesList) {
            if (!tree.isAvailable())
            {
                if (currentTime >= tree.getRespawnTime() && !tree.hasCollisionWithAnEntity())
                {
                    tree.revive();
                    entityList.add(tree);
                }
            }
        }

        // check if flag can spawn
        if (!Flag.isFlagOnMap && currentTime >= Flag.flagRespawnTime)
            generateFlag();
    }

    // Initialize list, map, generate city - tree - philosophers stone, ...
    private void initGame()
    {
        entityList.clear();
        killAllUnits();
        unityList.clear();
        unityToDestroyList.clear();
        collectablesList.clear();

        citiesMap.clear();
        treesList.clear();

        generateCity();
        generateTree();
        
        for (int i = 0; i < PHILOSOPHERS_STONE_NUMBER; i++) {
            generatePhilosophersStone();
        }

        Flag.flagRespawnTime = new Date().getTime() + Flag.DEFAULT_RESPAWN_TIME;
        Flag.isFlagOnMap = false;

        // allow all units to move
        MovableEntity.unitsAllowedToMove.put(Collector.class, true);
        MovableEntity.unitsAllowedToMove.put(Deserter.class, true);
        MovableEntity.unitsAllowedToMove.put(Horsemen.class, true);
        MovableEntity.unitsAllowedToMove.put(Pikemen.class, true);
    }

    /*
        Method to get a position taken by no entities (tree, movable entities, cities, ...)
    */
    public Position getUniquePosition() {
        Position pos = new Position((int) (Math.random() * HELBArmy.ROWS), (int) (Math.random() * HELBArmy.COLUMNS));

        // generate random position that is not in city area
        while (isPositionInCity(pos) || isPositionTakenByEntity(pos))
        {
            pos = new Position((int) (Math.random() * HELBArmy.ROWS), (int) (Math.random() * HELBArmy.COLUMNS));
        }

        return pos;
    }

    /*
        get : entity to destroy after loop
    */
    public void removeNext(Entity entity) {
        this.unityToDestroyList.add(entity);
    }

    public Flag getFlag()
    {
        return flag;
    }

    // Return true if the given position is the position of a city (north or south) or 'potectedSpace' squares around the city
    private boolean isPositionInCity(Position pos)
    {
        return (pos.x >= citiesMap.get("north").position.x - PROTECTED_SPACE_BEYOND_CITY && pos.x < citiesMap.get("north").position.x + PROTECTED_SPACE_BEYOND_CITY + citiesMap.get("north").getWidth() && pos.y >= citiesMap.get("north").position.y && pos.y <= citiesMap.get("north").position.y + citiesMap.get("north").getWidth() + 1)
            || (pos.x >= citiesMap.get("south").position.x - PROTECTED_SPACE_BEYOND_CITY && pos.x < citiesMap.get("south").position.x + PROTECTED_SPACE_BEYOND_CITY + citiesMap.get("south").getWidth() && pos.y >= citiesMap.get("south").position.y - PROTECTED_SPACE_BEYOND_CITY && pos.y < citiesMap.get("south").position.y + citiesMap.get("south").getWidth());
    }

    /*
        Method to know is a position in already taken by an entity on the board.
        iterate trough entityList and treesList.
    */
    private boolean isPositionTakenByEntity(Position pos)
    {
        for (Entity entity : entityList) {
            if (entity.position.equals(pos))
            {
                return true; // pos is taken by an entity
            }
        }

        for (Tree tree : treesList)
        {
            if (tree.position.equals(pos))
            {
                return true; // pos is taken by a tree
            }
        }

        return false; // pos is taken by no entity or tree
    }

    /*
        Generate City and add them on the entity list
        > North City : top - center
        > South City : bottom - center
    */
    private void generateCity() 
    {
        City northCity = new City(new Position(ROWS / 2 - CITY_DEFAULT_WIDTH / 2, 0), "north", this);
        City southCity = new City(new Position(ROWS / 2 - CITY_DEFAULT_WIDTH / 2, COLUMNS - CITY_DEFAULT_WIDTH), "south", this);

        citiesMap.put("north", northCity);
        citiesMap.put("south", southCity);

        entityList.add(northCity);
        entityList.add(southCity);
    }

    /*
        Generate Tree and add them on the entity list
        iterate trough all cell of the board
    */
    private void generateTree() 
    {
        double trees = 0, cells = 0;
        
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Position pos = new Position(i, j);

                if (!isPositionInCity(pos))
                {
                    cells++;
                    double nb = new Random().nextDouble() * 100;

                    if (nb < TREE_RATIO)
                    {
                        trees++;
                        Tree tree = new Tree(pos, this);
                        treesList.add(tree);
                        entityList.add(tree);
                    }
                }
            }
        }

        System.out.println("nb of tree : " + trees);
        System.out.println("cell available : " + cells);
        System.out.println("wanted average : " + TREE_RATIO);
        System.out.println("true average : " + trees / cells * 100);
    }

    // generate a philosophersStone
    private void generatePhilosophersStone()
    {
        PhilosophersStone philosophersStone = new PhilosophersStone(getUniquePosition(), this);
        collectablesList.add(philosophersStone);
        entityList.add(philosophersStone);
    }
    
    // generate a flag
    private void generateFlag()
    {
        this.flag = new Flag(getUniquePosition(), this);
        entityList.add(flag);
        collectablesList.add(flag);

        Flag.isFlagOnMap = true;
    }

    // kill all units (Movables Entities) on map
    private void killAllUnits() {
        for (MovableEntity unity : unityList) {
            unity.destroy();
        }
    }

    private void setKeyEvent(Scene scene)
    {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
           @Override
           public void handle(KeyEvent event)
           {
                KeyCode code = event.getCode();
                if (code == KeyCode.A)
                {
                    citiesMap.get("north").generateUnity(Collector.class);
                }
                else if (code == KeyCode.Z)
                {
                    citiesMap.get("north").generateUnity(Deserter.class);
                }
                else if (code == KeyCode.E)
                {
                    citiesMap.get("north").generateUnity(Horsemen.class);
                }
                else if (code == KeyCode.R)
                {
                    citiesMap.get("north").generateUnity(Pikemen.class);
                }
                else if (code == KeyCode.W)
                {
                    citiesMap.get("south").generateUnity(Collector.class);
                }
                else if (code == KeyCode.X)
                {
                    citiesMap.get("south").generateUnity(Deserter.class);
                }
                else if (code == KeyCode.C)
                {
                    citiesMap.get("south").generateUnity(Horsemen.class);
                }
                else if (code == KeyCode.V)
                {
                    citiesMap.get("south").generateUnity(Pikemen.class);
                }
                else if (code == KeyCode.J)
                {
                    MovableEntity.unitsAllowedToMove.put(Collector.class, !MovableEntity.unitsAllowedToMove.get(Collector.class));
                }
                else if (code == KeyCode.K)
                {
                    MovableEntity.unitsAllowedToMove.put(Deserter.class, !MovableEntity.unitsAllowedToMove.get(Deserter.class));
                }
                else if (code == KeyCode.L)
                {
                    MovableEntity.unitsAllowedToMove.put(Horsemen.class, !MovableEntity.unitsAllowedToMove.get(Horsemen.class));
                }
                else if (code == KeyCode.M)
                {
                    MovableEntity.unitsAllowedToMove.put(Pikemen.class, !MovableEntity.unitsAllowedToMove.get(Pikemen.class));
                }
                else if (code == KeyCode.U)
                {
                    killAllUnits();
                }
                else if (code == KeyCode.I)
                {
                    if (!Flag.isFlagOnMap)
                        generateFlag();
                }
                else if (code == KeyCode.O)
                {
                    initGame();
                }
                else if (code == KeyCode.P)
                {
                    generatePhilosophersStone();
                }

            }
               
        });
   }
    
}
