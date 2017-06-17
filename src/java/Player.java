
public class Player {

    String personid;
    char personChar;
    int score;
    int x;
    int y;

    public Player(String personid, int score, int x, int y,char pc) {
        this.personid = personid;
        this.score = score;
        this.x = x;
        this.y = y;
        this.personChar=pc;
    }

    public int getScore() {
        return score;
    }

    public void updateScore(int v) {
        this.score =this.score+v;
    }


    @Override
    public String toString() {
        return "[\"" + personid + "\"" + ", "+ score +", "+ x + ", "+ y + "]";
    }
}
