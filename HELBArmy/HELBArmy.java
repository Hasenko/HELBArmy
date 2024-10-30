import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Controler
public class HELBArmy {
    private final int CITY_DEFAULT_WIDTH = 5;

    // private final int TREE_NUMBERS = new Random().nextInt(19) + 2; // 2 - 20 both include
    private final int TREE_NUMBERS = 1;
    // private final int TREE_NUMBERS = 100;

    private final int PROTECTED_SPACE_BEYOND_CITY = 2;
    
    public ArrayList<Entity> entityList = new ArrayList<>(); // ArrayList to store entity
    public ArrayList<MovableEntity> unityList = new ArrayList<>();

    public HashMap<String, City> citiesMap = new HashMap<>(); // HashMap to store city
    public Tree[] treesList = new Tree[TREE_NUMBERS]; // Array to store tree

    private View view = new View();
    private GraphicsContext gc;

    /*
        Constructor
        > Setup base of the window 
        > Generate city and tree
    */
    public HELBArmy(Stage primaryStage) {
        primaryStage.setTitle("HELBArmy");
        Group root = new Group();
        Canvas canvas = new Canvas(view.WIDTH, view.HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();


        generateCity();
        generateTree();

        
        for (Map.Entry<String, City> entry : citiesMap.entrySet())
        {
            entry.getValue().generateUnity(); // Edit to give timestamp parameter and call than on run
        }
                
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    /*
        Call view to draw element on window
    */
    private void run(GraphicsContext gc)
    {
        view.drawBackground(gc);
        view.drawEntity(gc, entityList);

        for (MovableEntity unity : unityList) {
            unity.play();
        }
    }

    // ECAMPUS
    public boolean isPositionInBoard(Position pos){
        return(pos.x >= 0 && pos.y >= 0 && pos.x < view.COLUMNS && pos.y < view.ROWS); 
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
        City northCity = new City(new Position(view.ROWS / 2 - CITY_DEFAULT_WIDTH / 2, 0), "north", this);
        City southCity = new City(new Position(view.ROWS / 2 - CITY_DEFAULT_WIDTH / 2, view.COLUMNS - CITY_DEFAULT_WIDTH), "south", this);

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
        Position pos;
        for (int i = 0; i < TREE_NUMBERS; i++) {
            while(true)
            {
                pos = new Position((int) (Math.random() * view.ROWS), (int) (Math.random() * view.COLUMNS));

                while (isInCity(pos, PROTECTED_SPACE_BEYOND_CITY))
                {
                    pos = new Position((int) (Math.random() * view.ROWS), (int) (Math.random() * view.COLUMNS));
                }


                boolean canContinue = true;

                for (int j = 0; j < i; j++) {
                    if((treesList[j].position.equals(pos)))
                    {
                        canContinue = false;
                        System.out.println("Random detected ! " + i + " on " + j);
                        System.out.println(pos);
                        System.out.println("-----------------------");
                        System.out.println(treesList[j]);
                        System.out.println();
                    }
                }

                if (canContinue)
                {
                    break;
                }

            }
            
            treesList[i] = new Tree(pos, this);
            entityList.add(treesList[i]);
        }
    }

}
