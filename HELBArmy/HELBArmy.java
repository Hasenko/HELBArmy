import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Controler
public class HELBArmy {
    protected final int ROWS = 50;
    protected final int COLUMNS = ROWS;
    protected final int SQUARE_SIZE;

    private final double FRAME_RATE = 250.0;

    private final int CITY_DEFAULT_WIDTH = 5;
    private final int MAX_TREE_NUMBERS = 100;
    private final int MIN_TREE_NUMBERS = 10;

    private final int TREE_NUMBERS = new Random().nextInt(MAX_TREE_NUMBERS - MIN_TREE_NUMBERS) + MIN_TREE_NUMBERS;
    // private final int TREE_NUMBERS = new Random().nextInt(19) + 2; // 2 - 20 both include
    // private final int TREE_NUMBERS = 1;
    // private final int TREE_NUMBERS = 100;

    private final int PROTECTED_SPACE_BEYOND_CITY = 2;
    
    public ArrayList<Entity> entityList = new ArrayList<>(); // ArrayList to store entity
    public ArrayList<MovableEntity> unityList = new ArrayList<>();
    private ArrayList<Entity> unityToDestroy = new ArrayList<>(); // ArrayList to store unity to destroy after next iteration

    public HashMap<String, City> citiesMap = new HashMap<>(); // HashMap to store city
    public Tree[] treesList = new Tree[TREE_NUMBERS]; // Array to store tree

    private final View VIEW;
    private GraphicsContext gc;

    private long currentTime = 0;

    /*
        Constructor
        > Setup base of the window 
        > Generate city and tree
    */
    public HELBArmy(Stage primaryStage) {
        VIEW = new View(this);
        SQUARE_SIZE = VIEW.WIDTH / ROWS;
        primaryStage.setTitle("HELBArmy");
        Group root = new Group();
        Canvas canvas = new Canvas(VIEW.WIDTH, VIEW.HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();

        generateCity();
        generateTree();
                
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(FRAME_RATE), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        setKeyEvent(scene);
    }

    /*
        Call view to draw element on window
    */
    private void run(GraphicsContext gc)
    {
        currentTime = new Date().getTime();
        VIEW.drawBackground(gc);
        VIEW.drawEntity(gc, entityList);

        for (MovableEntity unity : unityList) {
            unity.play();
        }

        /*
        boolean isEveryHorsemenSafe = true;
        int instanceOfHorsemen = 0;
        for (MovableEntity unity : unityList) {
            if (unity instanceof Horsemen)
            {
                instanceOfHorsemen++;
                if (unity.isAdjacentToAnEntity())
                {
                    isEveryHorsemenSafe = false;
                    break;
                }
            }
        }

        if (isEveryHorsemenSafe && instanceOfHorsemen > 1)
        {
            Horsemen.safetyDistance++;
        }
        */
        
        for (Map.Entry<String, City> entry : citiesMap.entrySet())
        {
            entry.getValue().generateUnity(currentTime); // Edit to give timestamp parameter and call than on run
        }

        for (Entity entity : unityToDestroy) {
            this.entityList.remove(entity);
            this.unityList.remove(entity);
        }

        for (Tree tree : treesList) {
            if (!tree.exist)
            {
                if (currentTime >= tree.respawnTime && !tree.hasCollision())
                {
                    tree.revive();
                    entityList.add(tree);
                }
            }
        }

        //System.out.println("Safety distance : " + Horsemen.safetyDistance);
    }

    // ECAMPUS
    public boolean isPositionInBoard(Position pos){
        return(pos.x >= 0 && pos.y >= 0 && pos.x < COLUMNS && pos.y < ROWS); 
    }

    /*
        Return true if the given coordinate (x; y) is the position of a city (north or south) or 'potectedSpace' squares around the city
    */
    public boolean isInCity(Position pos, int potectedSpace)
    {
        return (pos.x >= citiesMap.get("north").position.x - potectedSpace && pos.x < citiesMap.get("north").position.x + potectedSpace + citiesMap.get("north").getWidth() && pos.y >= citiesMap.get("north").position.y && pos.y <= citiesMap.get("north").position.y + citiesMap.get("north").getWidth() + 1)
            || (pos.x >= citiesMap.get("south").position.x - potectedSpace && pos.x < citiesMap.get("south").position.x + potectedSpace + citiesMap.get("south").getWidth() && pos.y >= citiesMap.get("south").position.y - potectedSpace && pos.y < citiesMap.get("south").position.y + citiesMap.get("south").getWidth());
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
        for (int i = 0; i < TREE_NUMBERS; i++) {
            Position pos = getUniquePosition();
            treesList[i] = new Tree(pos, this);
            entityList.add(treesList[i]);
        }
    }

    public Position getUniquePosition() {
        Position pos;
        while (true) {
            pos = new Position((int) (Math.random() * ROWS), (int) (Math.random() * COLUMNS));

            while (isInCity(pos, PROTECTED_SPACE_BEYOND_CITY))
            {
                pos = new Position((int) (Math.random() * ROWS), (int) (Math.random() * COLUMNS));
            }

            boolean canContinue = true;

            for (Entity entity : entityList) {
                if (entity.position.equals(pos))
                {
                    canContinue = false;
                }
            }

            for (Tree tree : treesList)
            {
                if (tree == null)
                    continue;
                if (tree.position.equals(pos))
                {
                    canContinue = false;
                }
            }

            if (canContinue)
            {
                break;
            }
        }

        return pos;
    }

    public void removeNext(Entity entity) {
        this.unityToDestroy.add(entity);
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
