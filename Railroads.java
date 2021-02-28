import java.text.DecimalFormat;
import java.util.HashMap;
public class Railroads extends Properties {
    DecimalFormat df = new DecimalFormat("###.#");
    public int railroadsell(Users playerfirst, Users playersecond, String[] arrOfStr, int where_at, Users player1, Users player2, HashMap Places, HashMap railroads, HashMap Place_Status, Users banker){
        if (playerfirst.money >= Double.parseDouble(String.valueOf(railroads.get(Places.get(playerfirst.position))))) {
            Place_Status.put(Places.get(where_at), arrOfStr[0]);
            playerfirst.money = playerfirst.money - Double.parseDouble(String.valueOf(railroads.get(Places.get(playerfirst.position))));
            banker.money = banker.money + Double.parseDouble(String.valueOf(railroads.get(Places.get(playerfirst.position))));
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + playerfirst.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + playerfirst.name + " bought " + Places.get(where_at));
            return 0;
        }
        else {
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + playerfirst.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " goes bankrupt");
            return 1;
        }
    }
    public int railroadrent(Users playerfirst,Users playersecond){
        if(playersecond.money >= (25 * playerfirst.railroadcount)) {
                playersecond.money -= (25 * playerfirst.railroadcount);
                playerfirst.money += (25 * playerfirst.railroadcount);
            }
        else{
            return 1;
        }
        return 0;
    }
}