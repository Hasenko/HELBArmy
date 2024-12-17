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

    public boolean isAdjacentToEntity(Entity entity)
    {
        return entity.getAdjacentPositions().contains(this.position);
    }

    /*
        Method to know is a position in already taken by an entity on the board.
        iterate trough entityList and treesList
    */
    public boolean isPositionTakenByEntity(Position pos)
    {
        boolean response = false;

        for (Entity entity : gameBoard.entityList) {
            if (entity.position.equals(pos))
            {
                response = true;
                break;
            }
        }

        for (Tree tree : gameBoard.treesList)
        {
            if (tree.position.equals(pos))
            {
                response = true;
                break;
            }
        }

        return response;

    }

    /*
        Method to get a position taken by no entities (tree, movable entities, cities, ...)
    */
    public Position getUniquePosition() {
        Position pos = new Position((int) (Math.random() * HELBArmy.ROWS), (int) (Math.random() * HELBArmy.COLUMNS));

        // generate random position that is not in city area
        while (gameBoard.isPositionInCity(pos) || isPositionTakenByEntity(pos))
        {
            pos = new Position((int) (Math.random() * HELBArmy.ROWS), (int) (Math.random() * HELBArmy.COLUMNS));
        }

        return pos;
    }

    /*
        get an array of position in a certain radius of this
    */
    public ArrayList<Position> getPositionsInRadius(int radius)
    {
        ArrayList<Position> resultList = new ArrayList<>();

        for(int i = this.position.x-radius ;  i <= this.position.x+radius ; i++){
            for(int j = this.position.y-radius ;  j <= this.position.y+radius ; j++){
                if(!(i == this.position.x && j == this.position.y)){
                    resultList.add(new Position(i, j));
                }
            }     
        }

        return resultList;
    }

    /*
        get an array of adjacent position of this
    */
    protected ArrayList<Position> getAdjacentPositions(){
        return getPositionsInRadius(1);
    }

    // ECAMPUS
    public ArrayList<Position> getAccessibleAdjacentPositions(){

        ArrayList<Position> resultList = new ArrayList<Position>();
        ArrayList<Position> adjacentCoordinatesList = getAdjacentPositions();

        for (Position adjacent : adjacentCoordinatesList)
        {
            if(Board.isPositionInBoard(adjacent))
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

    public Entity getNearestEntity(ArrayList<Entity> entityList)
    {
        if (entityList.size() == 0) return null;

        Entity nearestEntity = entityList.get(0);
        // double minDistance = nearestEntity.getDistance(nearestEntity.position, this.position);
        double minDistance = Double.MAX_VALUE;

        for (Entity entity : entityList)
        {
            if (entity != null)
            {
                double currentDistance = Board.getDistance(entity.position, this.position);

                if (currentDistance <= minDistance)
                {
                    minDistance = currentDistance;
                    nearestEntity = entity;
                }
            }
        }
        return nearestEntity;
    }

    @Override
    public String toString()
    {
        return position.toString();
    }
}
