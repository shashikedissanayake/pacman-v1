import java.util.ArrayList;
import java.util.Random;

/**
 * Created by thz on 4/2/17.
 */
public class GameGrid {

    final  int SIZE=45;
    private int[][] grid;
    private int DOT_COUNT = 450;  //Random generated dot count
    private Random random = new Random();
    private char colors[] = {'R', 'G', 'B'};  //Coors of dots
    private int chance=0;  //Who has the chance to play ?

    private ArrayList<Player> players_temp =new ArrayList<>();
    private ArrayList<Player> players =new ArrayList<>(); //Players array


    public synchronized int addPlayer(){ //Add a person to the grid object
        players.add(players_temp.get(players.size()));
        return players.size()-1; //return the players ID
    }

    public synchronized boolean chanseRound(int playerid){ //Validate the move can or can't make a move
        if(chance == playerid){
            chance=(chance+1)%4;
            return true;
        }else {
            return false;
        }
    }
    
    public int playersCount(){
        return players.size();
    }

    public GameGrid() {

        //SIZExSIZE grid
        grid = new int[SIZE][SIZE];

        //Temp players
        players_temp.add(new Player("P1", 0, 0, 0,'a'));
        players_temp.add(new Player("P2", 0, 0, grid.length - 1,'b'));
        players_temp.add(new Player("P3", 0, grid.length - 1, 0,'c'));
        players_temp.add(new Player("P4", 0, grid.length - 1, grid.length - 1,'d'));

        getRandomGrid(); //Randomly generate points

        //Put the players in to the grid.
        grid[0][0]= players_temp.get(0).personChar;
        grid[0][grid.length-1]= players_temp.get(1).personChar;
        grid[grid.length-1][0]= players_temp.get(2).personChar;
        grid[grid.length-1][grid.length-1]= players_temp.get(3).personChar;

    }

    private void getRandomGrid(){

        for (int i = 0; i < DOT_COUNT; i++) {
            int x = random.nextInt(SIZE-1), y = random.nextInt(SIZE-1);
            if (x == 0 && y == 0 || x == 0 && y == grid.length - 1 || x == grid.length - 1 && y == grid.length - 1 || y == 0 && x == grid.length - 1) {
                i--; //corner cases are ignored players are at corners
            } else {
                grid[x][y] = random.nextInt(1000) % 3 + 1;
            }
        }
    }


    public void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public ArrayList<Coordinates> gridtoCoordinates() {
        //JSON string for current grid.
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                int c;
                if ((c = grid[i][j]) != 0 && (c < 4)) {
                    coordinates.add(new Coordinates(String.valueOf(colors[c - 1]), i, j));
                }
            }
        }
        return coordinates;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    } //Return players in the grid

    public synchronized boolean moveonGrid(int person,int diraction){
        if(person > 3) return false; //ID check
        Player player= players.get(person); //Get the respective player
        int x=player.x; //Get initial position
        int y=player.y;
        int x_n=0,y_n=0;

        if(diraction == 38){  //u
            x_n=x-1;y_n=y;
            if(x_n < 0) x_n=grid.length-1; //Go around the grid
        }else if(diraction == 40) { //d
            x_n = x+1;y_n = y;
            if (x_n > grid.length - 1) x_n = 0; //Go around the grid
        }else if(diraction == 37) {  //l
            x_n =x;y_n = y-1;
            if (y_n < 0) y_n = grid.length-1; //Go around the grid
        }else if(diraction == 39) { //r
            x_n = x;y_n = y+1;
            if (y_n > grid.length -1) y_n = 0; //Go around the grid
        }
        return updateGrid(x, y, x_n, y_n, player);
    }

    public boolean gameOver(){
        return DOT_COUNT==0;
    }

    private boolean updateGrid(int x,int y,int x_n,int y_n,Player player){
        if(grid[x_n][y_n] <= 4){  //Score
            grid[x][y]=0; //Update the grid
            player.x=x_n; //Update x,y coordinates
            player.y=y_n;
            player.updateScore(grid[x_n][y_n]);
            grid[x_n][y_n]=97+players.indexOf(player); //Put the person on the grid
            return true;
        }else {//Collide
            player.updateScore(-3);
        }
        return false;
    }




}
