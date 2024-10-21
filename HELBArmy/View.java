import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class View {

    private static final int CITY_NUMBERS = 2;
    private static final int TREE_NUMBERS = 150;
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;

    private ArrayList<Entity> entityList = new ArrayList<>();

    private City[] citiesList = new City[CITY_NUMBERS];
    private Tree[] treesList = new Tree[TREE_NUMBERS];

    private GraphicsContext gc;

    public View(Stage primaryStage)
    {
        primaryStage.setTitle("HELBArmy");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();

        generateCity();
        generateTree();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext gc)
    {
        drawBackground(gc);
        //drawCity(gc);
        //drawTree(gc);
        drawEntity(gc);
    }

    private void generateCity()
    {
        citiesList[0] = new City(ROWS / 2 - 2, 0, "north");
        citiesList[1] = new City(ROWS / 2 - 2, COLUMNS - 5, "south");

        addEntity(citiesList);
    }

    private void addEntity(Entity[] list) {
        for (Entity entity : list) {
            entityList.add(entity);
        }
    }

    private boolean isInCity(int x, int y)
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

        return (x >= citiesList[0].getX() - 2 && x <= citiesList[0].getX() + 6 && y >= citiesList[0].getY() && y <= citiesList[0].getY() + 6)
            || (x >= citiesList[1].getX() - 2 && x <= citiesList[1].getX() + 6 && y >= citiesList[1].getY() - 2 && y <= citiesList[1].getY() + 4);

    }

    private void generateTree()
    {
        int x, y;
        for (int i = 0; i < TREE_NUMBERS; i++) {
            while(true)
            {
                x = (int) (Math.random() * ROWS);
                y = (int) (Math.random() * COLUMNS);

                while (isInCity(x, y))
                {
                    x = (int) (Math.random() * ROWS);
                    y = (int) (Math.random() * COLUMNS);
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

    private void drawCity(GraphicsContext gc)
    {
        gc.drawImage(new Image(citiesList[0].getImagePath()), citiesList[0].getX() * SQUARE_SIZE, citiesList[0].getY() * SQUARE_SIZE, SQUARE_SIZE * 5, SQUARE_SIZE * 5);
        gc.drawImage(new Image(citiesList[1].getImagePath()), citiesList[1].getX() * SQUARE_SIZE, citiesList[1].getY() * SQUARE_SIZE, SQUARE_SIZE * 5, SQUARE_SIZE * 5);
    }

    private void drawTree(GraphicsContext gc)
    {
        for (Tree tree : treesList) {
            gc.drawImage(new Image(tree.getImagePath()), tree.getX() * SQUARE_SIZE, tree.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        }
    }

    private void drawBackground(GraphicsContext gc)
    {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void drawEntity(GraphicsContext gc)
    {
        for (Entity entity : entityList) {
            // draw image here
            gc.drawImage(new Image(entity.getImagePath()), entity.getX() * SQUARE_SIZE, entity.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        }
    }
}
