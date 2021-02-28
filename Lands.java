import java.text.DecimalFormat;
import java.util.HashMap;
public class Lands extends Properties {
    DecimalFormat df = new DecimalFormat("###.#");
    public int landsell(Users playerfirst, Users playersecond, String[] arrOfStr, int where_at, Users player1, Users player2, HashMap Places, HashMap lands, HashMap Place_Status, Users banker){
            if (playerfirst.money >= Double.parseDouble(String.valueOf(lands.get(Places.get(where_at))))) {
                Place_Status.put(Places.get(where_at), arrOfStr[0]);
                playerfirst.money = playerfirst.money - Double.parseDouble(String.valueOf(lands.get(Places.get(where_at))));
                banker.money = banker.money + Double.parseDouble(String.valueOf(lands.get(Places.get(where_at))));
                return 0;
            }
            else {
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " goes bankrupt");
                return 1;
            }
        }
    public int landrent(Users playerfirst, Users playersecond,int where_at, HashMap Places, HashMap lands) {
        if (Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) <= 2000) {
            if(playersecond.money >= Double.parseDouble(String.valueOf(lands.get(Places.get(where_at))))) {
                playersecond.money -= Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) * 4 / 10;
                playerfirst.money += Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) * 4 / 10;
            }
            else {
                return 1;
            }
        }
        if (Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) > 2000 && Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) <= 3000) {
            if(playersecond.money >= Double.parseDouble(String.valueOf(lands.get(Places.get(where_at))))) {
                playersecond.money -= Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) * 3 / 10;
                playerfirst.money += Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) * 3 / 10;
            }
            else {
                return 1;
            }
        }
        if (Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) > 3000 && Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) <= 4000) {
            if(playersecond.money >= Double.parseDouble(String.valueOf(lands.get(Places.get(where_at))))) {
                playersecond.money -= Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) * 35 / 100;
                playerfirst.money += Double.parseDouble(String.valueOf(lands.get(Places.get(where_at)))) * 35 / 100;
            }
            else{
                return 1;
            }
        }
        return 0;
    }
}