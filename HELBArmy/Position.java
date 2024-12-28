public class Position {
    public int x;
    public int y;

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
        return super.hashCode() + x + y;
    }

    @Override
    public String toString() {
        return "[x : " + x + "; y : " + y + "]";
    }

}
