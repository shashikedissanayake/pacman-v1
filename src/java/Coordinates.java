public class Coordinates {

    String color;
    int x,y;

    public Coordinates(String color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return  "[ \"" + color + "\"" + ", " + x + ", "+ y + "]";
    }
}
