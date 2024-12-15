public class Board {
    
    // ECAMPUS
    public static boolean isPositionInBoard(Position pos){
        return(pos.x >= 0 && pos.y >= 0 && pos.x < HELBArmy.COLUMNS && pos.y < HELBArmy.ROWS); 
    }

    /*
        get distance between two position
    */
    public static double getDistance(Position pos1, Position pos2)
    {
        // √((x_2-x_1)²+(y_2-y_1)²)

        return Math.sqrt((Math.pow(pos1.x - pos2.x, 2) + (Math.pow(pos1.y - pos2.y, 2))));
    }
}
