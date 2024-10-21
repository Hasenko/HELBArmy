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
// Controler
public class HELBArmy {
    View view;
    private static final int CITY_NUMBERS = 2; /* */
    private static final int TREE_NUMBERS = 283; /* */
    private static final int PROTECTED_SPACE_BEYOND_CITY = 2; /* */
    
    private ArrayList<Entity> entityList = new ArrayList<>(); /* */

    private City[] citiesList = new City[CITY_NUMBERS]; /* */
    private Tree[] treesList = new Tree[TREE_NUMBERS]; /* */

    private GraphicsContext gc; /* */

    public HELBArmy(Stage primaryStage) {
        view = new View();

        primaryStage.setTitle("HELBArmy");
        Group root = new Group();
        Canvas canvas = new Canvas(view.WIDTH, view.HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();


        generateCity(); /* */
        generateTree(); /* */

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc))); /* */
        timeline.setCycleCount(Animation.INDEFINITE); /* */
        timeline.play(); /* */
    }


    private void run(GraphicsContext gc)
    {
        view.drawBackground(gc); //
        view.drawEntity(gc, entityList); //
    }


    private void generateCity() /* */
    {
        citiesList[0] = new City(view.ROWS / 2 - 2, 0, "north");
        citiesList[1] = new City(view.ROWS / 2 - 2, view.COLUMNS - 5, "south");

        addEntity(citiesList);
    }

    private void addEntity(Entity[] list) { /* */
        for (Entity entity : list) {
            entityList.add(entity);
        }
    }

    private boolean isInCity(int x, int y) /* */
    {
        /*
         * (10; 1)
         * 
         * north city : (8; 0) - (9; 0) - (10; 0) - (11; 0) - (12; 0)
         *              (8; 1) - (9; 1) - (10; 1) - (11; 1) - (12; 1)
         *              (8; 2) - (9; 2) - (10; 2) - (11; 2) - (12; 2)
         *              (8; 3) - (9; 3) - (10; 3) - (11; 3) - (12; 3)
         *              (8; 4) - (9; 4) - (10; 4) - (11; 4) - (12; 4)
         * 
         * south city : (8; 15) - (9; 15) - (10; 15) - (11; 15) - (12; 15)
         *              (8; 16) - (9; 16) - (10; 16) - (11; 16) - (12; 16)
         *              (8; 17) - (9; 17) - (10; 17) - (11; 17) - (12; 17)
         *              (8; 18) - (9; 18) - (10; 18) - (11; 18) - (12; 18)
         *              (8; 19) - (9; 19) - (10; 19) - (11; 19) - (12; 19)
         */

        return (x >= citiesList[0].getX() - PROTECTED_SPACE_BEYOND_CITY && x < citiesList[0].getX() + PROTECTED_SPACE_BEYOND_CITY + citiesList[0].WIDTH && y >= citiesList[0].getY() && y <= citiesList[0].getY() + citiesList[0].WIDTH)
            || (x >= citiesList[1].getX() - PROTECTED_SPACE_BEYOND_CITY && x < citiesList[1].getX() + PROTECTED_SPACE_BEYOND_CITY + citiesList[1].WIDTH && y >= citiesList[1].getY() - PROTECTED_SPACE_BEYOND_CITY && y < citiesList[1].getY() + citiesList[1].WIDTH);

    }

    private void generateTree() /* */
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
                    if((treesList[j].getX() == x && treesList[j].getY() == y))
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
            
            treesList[i] = new Tree(x, y);
        }
        
        addEntity(treesList);
    }
}
