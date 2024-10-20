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

public class Main extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private static final Image NORTH_CITY_IMAGE = new Image("assets/city/north_city.png");
    private static final Image SOUTH_CITY_IMAGE = new Image("assets/city/south_city.png");
    private City northCity;
    private City southCity;

    private static final int TREE_NUMBERS = 150;
    private Tree[] treesList = new Tree[TREE_NUMBERS];
    private static final Image TREE_IMAGE = new Image("assets/special/tree.png");

    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("HELBArmy");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();

        createCity();
        generateTree();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext gc)
    {
        drawBackground(gc);
        drawCity(gc);
        drawTree(gc);
    }

    private void createCity()
    {
        northCity = new City(ROWS / 2 - 2, 0, "north");
        southCity = new City(ROWS / 2 - 2, COLUMNS - 5, "south");

        System.out.println(northCity);
        System.out.println(southCity);
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

        System.out.println("===========================================================");
        System.out.println("x : " + x);
        System.out.println("y : " + y);
        System.out.println("x >= northCity.getX() : " + (x >= northCity.getX()));
        System.out.println("x <= northCity.getX() + 4 : " + (x <= northCity.getX() + 4));
        System.out.println("y >= northCity.getY() : " + (y >= northCity.getY()));
        System.out.println("y <= northCity.getY() + 4 : " + (y <= northCity.getY() + 4));
        System.out.println("===========================================================");

        return (x >= northCity.getX() - 2 && x <= northCity.getX() + 6 && y >= northCity.getY() && y <= northCity.getY() + 6)
            || (x >= southCity.getX() - 2 && x <= southCity.getX() + 6 && y >= southCity.getY() - 2 && y <= southCity.getY() + 4);

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

        for (int i = 0; i < TREE_NUMBERS; i++) {
            System.out.println(i + " | " + treesList[i]);
        }

    }

    private void drawCity(GraphicsContext gc)
    {
        gc.drawImage(NORTH_CITY_IMAGE, northCity.getX() * SQUARE_SIZE, northCity.getY() * SQUARE_SIZE, SQUARE_SIZE * 5, SQUARE_SIZE * 5);
        gc.drawImage(SOUTH_CITY_IMAGE, southCity.getX() * SQUARE_SIZE, southCity.getY() * SQUARE_SIZE, SQUARE_SIZE * 5, SQUARE_SIZE * 5);
    }

    private void drawTree(GraphicsContext gc)
    {
        for (Tree tree : treesList) {
            gc.drawImage(TREE_IMAGE, tree.getX() * SQUARE_SIZE, tree.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
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

    public static void main(String[] args)
    {
        launch(args);
    }
}
