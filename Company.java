import java.text.DecimalFormat;
import java.util.HashMap;
public class Company extends Properties {
    DecimalFormat df = new DecimalFormat("###.#");
    public int companysell(Users playerfirst, Users playersecond, String[] arrOfStr, int where_at, Users player1, Users player2, HashMap Places, HashMap company, HashMap Place_Status, Users banker){
        if (playerfirst.money >= Double.parseDouble(String.valueOf(company.get(Places.get(where_at))))) {
            Place_Status.put(Places.get(where_at), arrOfStr[0]);
            playerfirst.money = playerfirst.money - Double.parseDouble(String.valueOf(company.get(Places.get(where_at))));
            banker.money = banker.money + Double.parseDouble(String.valueOf(company.get(Places.get(where_at))));
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + playerfirst.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + playerfirst.name + " bought " + Places.get(where_at));
            return 0;
        }
        else {
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " goes bankrupt");
            return 1;
        }
    }
    public int companyrent(Users playerfirst, Users playersecond,String[] arrOfStr){
        if(playersecond.money >= (4 * Integer.valueOf(arrOfStr[1]))) {
            playersecond.money -= (4 * Integer.valueOf(arrOfStr[1]));
            playerfirst.money += (4 * Integer.valueOf(arrOfStr[1]));
        }
        else{
            return 1;
        }
        return 0;
    }
}