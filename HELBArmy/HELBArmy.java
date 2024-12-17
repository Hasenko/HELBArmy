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
    public static final int ROWS = 50;
    public static final int COLUMNS = ROWS;
    protected final int SQUARE_SIZE;

    private final double FRAME_RATE = 250.0;

    private final int PROTECTED_SPACE_BEYOND_CITY = 2;
    private final int CITY_DEFAULT_WIDTH = 5;

    private final double TREE_RATIO = 10; // each cell has [TREE_RATIO]% chance to spawn a tree

    
    public ArrayList<Entity> entityList = new ArrayList<>(); // ArrayList to store entity
    public ArrayList<MovableEntity> unityList = new ArrayList<>();
    public ArrayList<Entity> unityToDestroyList = new ArrayList<>(); // ArrayList to store unity to destroy after next iteration

    public HashMap<String, City> citiesMap = new HashMap<>(); // HashMap to store city
    public ArrayList<Tree> treesList = new ArrayList<>(); // ArrayList to store tree

    private final View view;
    private GraphicsContext gc;

    private long currentTime = 0;

    /*
        Constructor
        > Setup base of the window 
        > Generate city and tree
    */
    public HELBArmy(Stage primaryStage) {
        view = new View(this, primaryStage);
        SQUARE_SIZE = view.WIDTH / ROWS;
        gc = view.getGraphicsContext();

        generateCity();
        generateTree();
                
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
        view.drawBackground(gc);
        view.drawEntity(gc, entityList);

        for (MovableEntity unity : unityList) {
            unity.play();
        }
        
        for (Map.Entry<String, City> entry : citiesMap.entrySet())
        {
            entry.getValue().generateUnity(currentTime); // Edit to give timestamp parameter and call than on run
        }

        for (Entity entity : unityToDestroyList) {
            this.entityList.remove(entity);
            this.unityList.remove(entity);
        }

        for (Tree tree : treesList) {
            if (!tree.isAvailable())
            {
                if (currentTime >= tree.respawnTime && !tree.hasCollisionWithAnEntity())
                {
                    tree.revive();
                    entityList.add(tree);
                }
            }
        }
        /*
        System.out.println("---");
        System.out.println("Instance of Horsemen north : " + HorsemenManager.getInstanceOfHorsemen("north"));
        System.out.println("Safety distance of Horsemen north : " + HorsemenManager.getSafetyDistanceHorsemen("north"));
        System.out.println("Safe counter Horsemen north : " + HorsemenManager.getSafeCounterHorsemen("north"));
        System.out.println("---");
        System.out.println("Instance of Horsemen south : " + HorsemenManager.getInstanceOfHorsemen("south"));
        System.out.println("Safety distance of Horsemen south : " + HorsemenManager.getSafetyDistanceHorsemen("south"));
        System.out.println("Safe counter Horsemen south : " + HorsemenManager.getSafeCounterHorsemen("south"));
        */

    }

    /*
        Return true if the given coordinate (x; y) is the position of a city (north or south) or 'potectedSpace' squares around the city
        
        *not in board class, because it's only used on controller*
    */
    public boolean isPositionInCity(Position pos)
    {
        return (pos.x >= citiesMap.get("north").position.x - PROTECTED_SPACE_BEYOND_CITY && pos.x < citiesMap.get("north").position.x + PROTECTED_SPACE_BEYOND_CITY + citiesMap.get("north").getWidth() && pos.y >= citiesMap.get("north").position.y && pos.y <= citiesMap.get("north").position.y + citiesMap.get("north").getWidth() + 1)
            || (pos.x >= citiesMap.get("south").position.x - PROTECTED_SPACE_BEYOND_CITY && pos.x < citiesMap.get("south").position.x + PROTECTED_SPACE_BEYOND_CITY + citiesMap.get("south").getWidth() && pos.y >= citiesMap.get("south").position.y - PROTECTED_SPACE_BEYOND_CITY && pos.y < citiesMap.get("south").position.y + citiesMap.get("south").getWidth());
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
        > All position are differents
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

    public void removeNext(Entity entity) {
        this.unityToDestroyList.add(entity);
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
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.Z)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.E)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.R)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.W)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.X)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.C)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.V)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.J)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.K)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.L)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.M)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.U)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.I)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.O)
                {
                    System.out.println("Still under developpement");
                }
                else if (code == KeyCode.P)
                {
                    System.out.println("Still under developpement");
                }

            }
               
        });
   }
    
}
