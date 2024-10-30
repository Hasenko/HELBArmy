import java.util.ArrayList;
import java.util.Map;

public abstract class Entity {
    private final static int DEFAULT_WIDTH = 1;

    protected Position position;
    private String side;
    private int width;
    private String imagePath;

    public HELBArmy gameBoard;
    
    public Entity(Position position, String imagePath, HELBArmy gameBoard)
    {
        this(position, "neutral", DEFAULT_WIDTH, imagePath, gameBoard);
    }

    public Entity(Position position, String side, String imagePath, HELBArmy gameBoard)
    {
        this(position, side, DEFAULT_WIDTH, imagePath, gameBoard);
    }

    public Entity(Position position, String side, int width, String imagePath, HELBArmy gameBoard)
    {
        this.position = position;
        setSide(side);
        this.width = width;
        this.imagePath = imagePath;
        this.gameBoard = gameBoard;
    }

    private void setSide(String side)
    {
        if (side.equals("north") || side.equals("south") || side.equals("neutral"))
            this.side = side;
        else
            throw new IllegalArgumentException("side must be : north - south - neutral");
    }

    public String getSide()
    {
        return side;
    }

    public int getWidth()
    {
        return width;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        if (!imagePath.startsWith("assets/"))
            throw new IllegalArgumentException(imagePath + " | Base directory must be : assets/");
        this.imagePath = imagePath;
    }

    /*
        get an array of accessible position for this
    */
    // ECAMPUS
    private ArrayList<Position> getAdjacentPositions(){

        ArrayList<Position> resultList = new ArrayList<Position>();
        
        for(int i = this.position.x-1 ;  i <= this.position.x+1 ; i++){
            for(int j = this.position.y-1 ;  j <= this.position.y+1 ; j++){
                if(!(i == this.position.x && j == this.position.y)){
                    resultList.add(new Position(i, j));
                }
            }     
        }
        return resultList;
    }
    

    /*
        get distance with an entity and this
    */
    public double getDistance(Position pos1, Position pos2)
    {
        // √((x_2-x_1)²+(y_2-y_1)²)

        return Math.sqrt((Math.pow(pos1.x - pos2.x, 2) + (Math.pow(pos1.y - pos2.y, 2))));
    }

    // ECAMPUS
    public ArrayList<Position> getAccessibleAdjacentPositions(){

        ArrayList<Position> resultList = new ArrayList<Position>();
        ArrayList<Position> adjacentCoordinatesList = getAdjacentPositions();

        for (Position adjacent : adjacentCoordinatesList)
        {
            if(gameBoard.isPositionInBoard(adjacent))
            {
                boolean ok = true;

                for (Map.Entry<String, City> entry : gameBoard.citiesMap.entrySet()) {
                    City city = entry.getValue();

                    if (city.getPositions().contains(adjacent))
                    {
                        ok = false;
                    }
                }

                for (Entity entity : gameBoard.entityList) {
                    if (entity.position.equals(adjacent))
                    {
                        ok = false;
                    }
                }
                if (ok)
                {
                    resultList.add(adjacent);
                }

            }
        }

        return resultList;
    }

    public boolean isCloseToEntity(Entity entity)
    {
        return entity.getAdjacentPositions().contains(this.position);
    }

    public boolean isInPosition(Position position)
    {
        return this.position.equals(position);
    }

    public Entity getNearestEntity()
    {
        return null;
    }

    public Collector getNearestCollector()
    {
        return null;
    }

    public Tree getNearestTree()
    {
        ArrayList<Tree> availableTree = new ArrayList<>();

        for (Tree tree : gameBoard.treesList) {
            if(tree.exist && tree.getAccessibleAdjacentPositions().size() > 0) availableTree.add(tree);
        }

        if (availableTree.size() == 0) return null;

        Tree nearestTree = availableTree.get(0);
        double minDistance = nearestTree.getDistance(nearestTree.position, this.position);

        for (Tree tree : availableTree)
        {
            double currentDistance = getDistance(tree.position, this.position);

            if (currentDistance <= minDistance)
            {
                minDistance = currentDistance;
                nearestTree = tree;
            }

        }

        return nearestTree;
    }
    @Override
    public String toString()
    {
        return position.toString();
    }
}
