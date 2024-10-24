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
import java.util.Random;

// Controler
public class HELBArmy {
    private final int CITY_DEFAULT_WIDTH = 5;
    private final int CITY_NUMBERS = 2;
    private final int TREE_NUMBERS = new Random().nextInt(19) + 2; // 2 - 20 both include

    private final int PROTECTED_SPACE_BEYOND_CITY = 2;
    
    public ArrayList<Entity> entityList = new ArrayList<>(); // ArrayList to store entity

    public City[] citiesList = new City[CITY_NUMBERS]; // Array to store city
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

        for (City city : citiesList) {
            city.generateUnity();
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
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
    }

    /*
        Add content of array to the entity list
    */
    private void addEntity(Entity[] list)
    {
        for (Entity entity : list)
        {
            entityList.add(entity);
        }
    }

    /*
        Return true if the given coordinate (x; y) is the position of a city (north or south) or 2 squares around the city
    */
    private boolean isInCity(int x, int y)
    {
        return (x >= citiesList[0].x - PROTECTED_SPACE_BEYOND_CITY && x < citiesList[0].x + PROTECTED_SPACE_BEYOND_CITY + citiesList[0].getWidth() && y >= citiesList[0].y && y <= citiesList[0].y + citiesList[0].getWidth() + 1)
            || (x >= citiesList[1].x - PROTECTED_SPACE_BEYOND_CITY && x < citiesList[1].x + PROTECTED_SPACE_BEYOND_CITY + citiesList[1].getWidth() && y >= citiesList[1].y - PROTECTED_SPACE_BEYOND_CITY && y < citiesList[1].y + citiesList[1].getWidth());

    }

    /*
        Generate City and add them on the entity list
        > North City : top - center
        > South City : bottom - center
    */
    private void generateCity() 
    {
        citiesList[0] = new City(view.ROWS / 2 - CITY_DEFAULT_WIDTH / 2, 0, "north", this);
        citiesList[1] = new City(view.ROWS / 2 - CITY_DEFAULT_WIDTH / 2, view.COLUMNS - CITY_DEFAULT_WIDTH, "south", this);

        System.out.println("north city : " + citiesList[0]);
        System.out.println("south city : " + citiesList[1]);
        addEntity(citiesList);
    }

    /*
        Generate Tree and add them on the entity list
        > All position are differents
    */
    private void generateTree() 
    {
        int x, y;
        for (int i = 0; i < TREE_NUMBERS; i++) {
            while(true)
            {
                x = (int) (Math.random() * view.ROWS);
                y = (int) (Math.random() * view.COLUMNS);

                while (isInCity(x, y))
                {
                    x = (int) (Math.random() * view.ROWS);
                    y = (int) (Math.random() * view.COLUMNS);
                }


                boolean canContinue = true;

                for (int j = 0; j < i; j++) {
                    if((treesList[j].x == x && treesList[j].y == y))
                    {
                        canContinue = false;
                        System.out.println("Random detected ! " + i + " on " + j);
                        System.out.println("x : " + x);
                        System.out.println("y : " + y);
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
            
            treesList[i] = new Tree(x, y, this);
        }
        
        addEntity(treesList);
    }
}
