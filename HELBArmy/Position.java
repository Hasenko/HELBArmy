public class Position {
    private final static int VOID_X = -10;
    private final static int VOID_Y = -10;
    public int x;
    public int y;

    public Position()
    {
        this(VOID_X, VOID_Y);
    }

    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    /*
        inspiration taken from ecampus "Cours 4 - Les fondamentaux de la POO" page 98
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position other = (Position) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + x +y;
    }

    @Override
    public String toString() {
        return "[x : " + x + "; y : " + y + "]";
    }

}
