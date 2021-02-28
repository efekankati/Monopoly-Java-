import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
public class jsonread {
    HashMap<String, Double> lands = new HashMap<String, Double>();
    HashMap<String, Double> company = new HashMap<String, Double>();
    HashMap<String, Double> railroads = new HashMap<String, Double>();
    ArrayList<String> chanceListArrayList = new ArrayList<>();
    ArrayList<String> communityChestListArrayList = new ArrayList<>();
    HashMap<Integer, String> Places = new HashMap<Integer, String>();
    HashMap<String, String> Place_Status = new HashMap<String, String>();
    DecimalFormat df = new DecimalFormat("###.#");
    Properties land = new Lands();
    Properties companysell = new Company();
    Properties railroadsell = new Railroads();
    Players player1 = new Players("Player 1", 1, 15000, 0);
    Players player2 = new Players("Player 2", 1, 15000, 0);
    Banker banker = new Banker(100000);
    boolean leicestersquare = false;
    boolean threespaces = false;

    public void propertyreader(String filename) throws IOException, ParseException { //Read property.json file
        JSONParser parser = new JSONParser();
        Reader reader = new FileReader(filename);
        JSONObject properties = (JSONObject) parser.parse(reader);

        JSONArray landinfo = (JSONArray) properties.get("1");
        JSONArray companyinfo = (JSONArray) properties.get("2");
        JSONArray railroadinfo = (JSONArray) properties.get("3");

        for (int i = 0; i < landinfo.size(); i++) {  //Placing Land's iformations to various hashmaps
            String id = String.valueOf(((JSONObject) landinfo.get(i)).get("id"));
            int ID = Integer.valueOf(id);
            String name = String.valueOf(((JSONObject) landinfo.get(i)).get("name"));
            String cost = String.valueOf(((JSONObject) landinfo.get(i)).get("cost"));
            double Cost = Integer.valueOf(cost);
            lands.put(name, Cost); //Keeps land name and it's cost as <Key,Value> pair
            Places.put(ID, name); //Keeps square number and corresponding name
            Place_Status.put(name, "null"); //Keeps square name and owner status
        }

        for (int j = 0; j < companyinfo.size(); j++) { //Placing Company's iformations to various hashmaps
            String id = String.valueOf(((JSONObject) companyinfo.get(j)).get("id"));
            int ID = Integer.valueOf(id);
            String name = String.valueOf(((JSONObject) companyinfo.get(j)).get("name"));
            String cost = String.valueOf(((JSONObject) companyinfo.get(j)).get("cost"));
            double Cost = Integer.valueOf(cost);
            company.put(name, Cost);  //Keeps company name and it's cost as <Key,Value> pair
            Places.put(ID, name); //Keeps square number and corresponding name
            Place_Status.put(name, "null"); //Keeps square name and owner status
        }

        for (int k = 0; k < railroadinfo.size(); k++) { //Placing Railroad's iformations to various hashmaps
            String id = String.valueOf(((JSONObject) railroadinfo.get(k)).get("id"));
            int ID = Integer.valueOf(id);
            String name = String.valueOf(((JSONObject) railroadinfo.get(k)).get("name"));
            String cost = String.valueOf(((JSONObject) railroadinfo.get(k)).get("cost"));
            double Cost = Integer.valueOf(cost);
            railroads.put(name, Cost); //Keeps railroad name and it's cost as <Key,Value> pair
            Places.put(ID, name); //Keeps square number and corresponding name
            Place_Status.put(name, "null"); //Keeps square name and owner status
        }
    }

    public void listreader(String filename) throws IOException, ParseException { //Read list.json file
        JSONParser parser = new JSONParser();
        Reader reader = new FileReader(filename);

        JSONObject list = (JSONObject) parser.parse(reader);
        JSONArray chancelist = (JSONArray) list.get("chanceList");
        JSONArray communityChestList = (JSONArray) list.get("communityChestList");

        for (int m = 0; m < chancelist.size(); m++) { //Send the information about chance cards and add them into array list
            String item = String.valueOf(((JSONObject) chancelist.get(m)).get("item"));
            chanceListArrayList.add(item);
        }
        for (int n = 0; n < communityChestList.size(); n++) { //Send the information about community chest cards and add them into array list
            String item = String.valueOf(((JSONObject) communityChestList.get(n)).get("item"));
            communityChestListArrayList.add(item);
        }
    }

    public void commandreader(String filename) throws IOException { //Do actual commands that are readed from the command.txt file
        Places.put(1, "GO"); //Put manually the square number and name pairs into HashMap called "Places"
        Places.put(3, "Community Chest");
        Places.put(5, "Income Tax");
        Places.put(8, "Chance");
        Places.put(11, "Jail");
        Places.put(18, "Community Chest");
        Places.put(21, "Free Parking");
        Places.put(23, "Chance");
        Places.put(31, "Go to jail");
        Places.put(34, "Community Chest");
        Places.put(37, "Chance");
        Places.put(39, "Super Tax");
        File file = new File(filename);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        PrintStream fileOut = new PrintStream("./output.txt");
        System.setOut(fileOut);
        int jailcountp1 = 1;
        int jailcountp2 = 1;
        boolean injailp1 = false;
        boolean injailp2 = false;
        while ((line = br.readLine()) != null) { //Read file line-by-line
            String str = line;
            if (str.contains(";")) { //Check if line has semi-colon in it
                String[] arrOfStr = str.split(";"); //Split with respect to semi colon to get the player name and dice number
                if (arrOfStr[0].equalsIgnoreCase("Player 1")) { //If the name is Player 1 do commands according to that player
                    if (injailp1 && jailcountp1 < 4) { //Check if the player is in jail and count it's round up to 3
                        System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player1.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " in jail (count=" + jailcountp1 + ")");
                        jailcountp1++;
                    } else { //If not in jail continue
                        injailp1 = false;
                        int where_at = player1.position + Integer.valueOf(arrOfStr[1]);
                        if (where_at <= 40) player1.position = where_at; //Calculate if the (player's position + the dice) exceeds the max number of squares
                        else {
                            where_at = where_at - 40;
                            player1.position = where_at;
                            if(player1.position != 1){
                                player1.money += 200;
                                banker.money -= 200;
                            }
                        }
                        if (Place_Status.containsKey(Places.get(where_at))) { //Check if the square that player landed is a property or not
                            if (Place_Status.get(Places.get(where_at)).equalsIgnoreCase("null")) { //Sell if property has no owner
                                sell(player1, player2, arrOfStr, where_at);
                            } else if (Place_Status.get(Places.get(where_at)).equals("Player 2")) { //Let the other player get rent if the player does not own this square
                                playerrent(player2, player1, arrOfStr, where_at);
                            } else if (Place_Status.get(Places.get(where_at)).equals("Player 1")) { //Check if player owns this square if does print a respective message
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player1.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + "Player 1 has " + Places.get(where_at));
                            }
                        } else { //If the square the Player is on is not a property then execute respectively
                            if (Places.get(where_at).equalsIgnoreCase("Community Chest")) {
                                community_chest(player1, player2, arrOfStr, where_at);
                            } else if (Places.get(where_at).equalsIgnoreCase("Chance")) {
                                chance(player1, player2, where_at, arrOfStr);
                            } else if (Places.get(where_at).equalsIgnoreCase("Income Tax") || Places.get(where_at).equals("Super Tax")) {
                                player1.money -= 100; //Player 1 pays money
                                banker.money += 100; //Banker takes money
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player1.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " paid Tax");
                            } else if (Places.get(where_at).equalsIgnoreCase("GO")) {
                                player1.money += 200;
                                banker.money -= 200;
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player1.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " is in GO square");
                            } else if (Places.get(where_at).equalsIgnoreCase("Jail")) { //Check if Player goes to jail if does make Player wait 3 rounds
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player1.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " went to jail");
                                injailp1 = true;
                            } else if (Places.get(where_at).equalsIgnoreCase("Go to jail")) {
                                player1.position = 11;
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player1.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " went to jail");
                                injailp1 = true;
                            } else if (Places.get(where_at).equalsIgnoreCase("Free Parking")) {
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player1.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " is in free parking");
                            }
                        }
                    }
                } else if (arrOfStr[0].equalsIgnoreCase("Player 2")) { //If the name is Player 2 do commands according to that player similar with Player 1
                    int where_at = player2.position + Integer.valueOf(arrOfStr[1]);
                    if (injailp2 && jailcountp2 < 4) {
                        System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player2.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " in jail (count=" + jailcountp2 + ")");
                        jailcountp2++;
                    } else {
                        if (where_at <= 40) player2.position = where_at;
                        else {
                            where_at = where_at - 40;
                            player2.position = where_at;
                            player2.money += 200;
                            banker.money -= 200;
                        }
                        injailp2 = false;
                        if (Place_Status.containsKey(Places.get(where_at))) {
                            if (Place_Status.get(Places.get(where_at)).equalsIgnoreCase("null")) {
                                sell(player2, player1, arrOfStr, where_at);
                            } else if (Place_Status.get(Places.get(where_at)).equals("Player 1")) {
                                playerrent(player1, player2, arrOfStr, where_at);
                            } else if (Place_Status.get(Places.get(where_at)).equals("Player 2")) {
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player2.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + "Player 2 has " + Places.get(where_at));
                            }
                        } else {
                            if (Places.get(where_at).equalsIgnoreCase("Community Chest")) {
                                community_chest(player2, player1, arrOfStr, where_at);
                            } else if (Places.get(where_at).equalsIgnoreCase("Chance")) {
                                chance(player2, player1, where_at, arrOfStr);
                            } else if (Places.get(where_at).equalsIgnoreCase("Income Tax") || Places.get(where_at).equals("Super Tax")) {
                                player2.money -= 100;
                                banker.money += 100;
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player2.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " paid Tax");
                            } else if (Places.get(where_at).equalsIgnoreCase("GO")) {
                                player2.money += 200;
                                banker.money -= 200;
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player2.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " is in GO square");
                            } else if (Places.get(where_at).equalsIgnoreCase("Jail")) {
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player2.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " went to jail");
                                injailp2 = true;
                            } else if (Places.get(where_at).equalsIgnoreCase("Go to jail")) {
                                player2.position = 11;
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player2.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " went to jail");
                                injailp2 = true;
                            } else if (Places.get(where_at).equalsIgnoreCase("Free Parking")) {
                                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + player2.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " is in free parking");
                            }
                        }
                    }
                }
            } else if (str.contains("show()")) { //If line does not contain semicolon call the show function
                show();
            }
        }
        show(); //If file ends call show function
    }

    public void chance(Users playerfirst, Users playersecond, int where_at, String[] arrOfStr) {

        if (chanceListArrayList.get(0).equals("Advance to Go (Collect $200)")) {
            playerfirst.money += 200;
            banker.money -= 200;
            playerfirst.position = 1;
            chanceListArrayList.remove(0);
            chanceListArrayList.add("Advance to Go (Collect $200)");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + 1 + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + "Player 2 draw Chance - Advance to Go (Collect $200)");

        } else if (chanceListArrayList.get(0).equals("Advance to Leicester Square")) {
            if (playerfirst.position < 27) playerfirst.position = 27;
            else {
                playerfirst.money += 200;
                banker.money -= 200;
                playerfirst.position = 27;
            }
            leicestersquare = true;
            if(Place_Status.get("Leicester Square").equals("null")){
                sell(playerfirst,playersecond,arrOfStr,playerfirst.position);
            }
            else if(Place_Status.get("Leicester Square").equals(playerfirst.name)){
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + playerfirst.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Advance to Leicester Square " + arrOfStr[0] + " has Leicester Square");
            }
            else if(Place_Status.get("Leicester Square").equals(playersecond.name)){
                playerrent(playersecond,playerfirst,arrOfStr,playerfirst.position);
            }

            chanceListArrayList.remove(0);
            chanceListArrayList.add("Advance to Leicester Square");
        } else if (chanceListArrayList.get(0).equals("Go back 3 spaces")) {
            playerfirst.position -= 3;
            chanceListArrayList.remove(0);
            chanceListArrayList.add("Go back 3 spaces");
            threespaces = true;
            if (Places.get(playerfirst.position).equals("Income Tax")) {
                playerfirst.money -= 100;
                banker.money += 100;
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + playerfirst.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Go back 3 spaces " + arrOfStr[0] + " paid Tax");
            } else if (Places.get(playerfirst.position).equals("Community Chest")) {
                community_chest(playerfirst, playersecond, arrOfStr, playerfirst.position);
            }
            else if(Places.get(playerfirst.position).equals("Vine Street")){
                if(Place_Status.get("Vine Street").equals("null")){
                    sell(playerfirst,playersecond,arrOfStr,playerfirst.position);
                }
                else if(Place_Status.get("Vine Street").equals(playerfirst.name)){
                    System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + playerfirst.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + "Player 2 draw Chance - Go back 3 spaces " + playerfirst.name + " has Vine Street");
                }
                else if(Place_Status.get("Vine Street").equals(playersecond.name)){
                    playerrent(playersecond,playerfirst,arrOfStr,playerfirst.position);
                }
            }
        } else if (chanceListArrayList.get(0).equals("Pay poor tax of $15")) {
            playerfirst.money -= 15;
            banker.money += 15;
            chanceListArrayList.remove(0);
            chanceListArrayList.add("Pay poor tax of $15");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Pay poor tax of $15");
        } else if (chanceListArrayList.get(0).equals("Your building loan matures - collect $150")) {
            playerfirst.money += 150;
            banker.money -= 150;
            chanceListArrayList.remove(0);
            chanceListArrayList.add("Your building loan matures - collect $150");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Your building loan matures - collect $150");
        } else if (chanceListArrayList.get(0).equals("You have won a crossword competition - collect $100")) {
            playerfirst.money += 100;
            banker.money -= 100;
            chanceListArrayList.remove(0);
            chanceListArrayList.add("You have won a crossword competition - collect $100 ");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - You have won a crossword competition - collect $100");
        }
    }

    public void community_chest(Users playerfirst, Users playersecond, String[] arrOfStr, int where_at) {
        if (communityChestListArrayList.get(0).equals("Advance to Go (Collect $200)")) {
            playerfirst.money += 200;
            banker.money -= 200;
            playerfirst.position = 1;
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("Advance to Go (Collect $200)");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + 1 + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Community Chest - Advance to Go (Collect $200)");
        } else if (communityChestListArrayList.get(0).equals("Bank error in your favor - collect $75")) {
            playerfirst.money += 75;
            banker.money -= 75;
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("Bank error in your favor - collect $75");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Community Chest - Bank error in your favor - collect $75");
        } else if (communityChestListArrayList.get(0).equals("Doctor's fees - Pay $50")) {
            playerfirst.money -= 50;
            banker.money += 50;
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("Doctor's fees - Pay $50");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Doctor's fees - Pay $50");
        } else if (communityChestListArrayList.get(0).equals("It is your birthday Collect $10 from each player")) {
            if (arrOfStr[0].equals("Player 1")) {
                playerfirst.money += 10;
                playersecond.money -= 10;
            } else if (arrOfStr[0].equals("Player 2")) {
                playersecond.money += 10;
                playerfirst.money -= 10;
            }
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("It is your birthday Collect $10 from each player");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - It is your birthday Collect $10 from each player");
        } else if (communityChestListArrayList.get(0).equals("Grand Opera Night - collect $50 from every player for opening night seats")) {
            if (arrOfStr[0].equals("Player 1")) {
                playerfirst.money += 50;
                playersecond.money -= 50;
            } else if (arrOfStr[0].equals("Player 2")) {
                playersecond.money += 50;
                playerfirst.money -= 50;
            }
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("Grand Opera Night - collect $50 from every player for opening night seats");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Grand Opera Night - collect $50 from every player for opening night seats");
        } else if (communityChestListArrayList.get(0).equals("Income Tax refund - collect $20")) {
            playerfirst.money += 20;
            banker.money -= 20;
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("Income Tax refund - collect $20");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Income Tax refund - collect $20");
        } else if (communityChestListArrayList.get(0).equals("Life Insurance Matures - collect $100")) {
            playerfirst.money += 100;
            banker.money -= 100;
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("Life Insurance Matures - collect $100");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Life Insurance Matures - collect $100");
        } else if (communityChestListArrayList.get(0).equals("Pay Hospital Fees of $100")) {
            playerfirst.money -= 100;
            banker.money += 100;
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("Pay Hospital Fees of $100");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Pay Hospital Fees of $100");
        } else if (communityChestListArrayList.get(0).equals("Pay School Fees of $50")) {
            playerfirst.money -= 50;
            banker.money += 50;
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("Pay School Fees of $50");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Pay School Fees of $50");
        } else if (communityChestListArrayList.get(0).equals("You inherit $100")) {
            playerfirst.money += 100;
            banker.money -= 100;
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("You inherit $100");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - You inherit $100");
        } else if (communityChestListArrayList.get(0).equals("From sale of stock you get $50")) {
            playerfirst.money += 50;
            banker.money -= 50;
            communityChestListArrayList.remove(0);
            communityChestListArrayList.add("From sale of stock you get $50");
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - From sale of stock you get $50");
        }

    }

    public void show() {
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        System.out.print("Player 1\t" + df.format(player1.money) + "\t" + "have: ");
        int onecount = 1;
        int twocount = 1;
        for (String i : Place_Status.keySet()) {
            if (Place_Status.get(i) != "null" && Place_Status.get(i).equals("Player 1")) {
                if (onecount == 1) {
                    System.out.print(i);
                    onecount++;
                } else {
                    System.out.print(", " + i);
                }
            }
        }
        System.out.println();
        System.out.print("Player 2\t" + df.format(player2.money) + "\t" + "have: ");
        for (String i : Place_Status.keySet()) {
            if (Place_Status.get(i) != "null" && Place_Status.get(i).equals("Player 2")) {
                if (twocount == 1) {
                    System.out.print(i);
                    twocount++;
                } else {
                    System.out.print(", " + i);
                }
            }
        }
        System.out.println();
        System.out.println("Banker\t" + df.format(banker.money));
        if (player1.money > player2.money) System.out.println("Winner\tPlayer 1");
        else if (player2.money > player1.money) System.out.println("Winner\tPlayer 2");
        else System.out.println("Scoreless");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");

    }

    public void sell(Users playerfirst, Users playersecond, String[] arrOfStr, int where_at) {
        if(Places.get(where_at).equals("Vine Street") && threespaces){
            int a = land.landsell(playerfirst,playersecond,arrOfStr,where_at,player1,player2,Places,lands,Place_Status,banker);
            if(a == 1){
                show();
                System.exit(0);
            }
            else{
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + playerfirst.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + playerfirst.name + " draw Chance - Go back 3 spaces " + playerfirst.name + " bought Vine Street");
            }
        }
        else if(Places.get(where_at).equals("Leicester Square") && leicestersquare){
            int a = land.landsell(playerfirst,playersecond,arrOfStr,where_at,player1,player2,Places,lands,Place_Status,banker);
            if(a == 1) {
                show();
                System.exit(0);
            }
            else{
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + playerfirst.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + playerfirst.name + " draw Chance - Advance to Leicester Square " + playerfirst.name + " bought Leicester Square");
            }
        }
        else if (lands.containsKey(Places.get(where_at))) {
            int a = land.landsell(playerfirst,playersecond,arrOfStr,where_at,player1,player2,Places,lands,Place_Status,banker);
            if(a == 1) {
                show();
                System.exit(0);
            }
            else{
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + playerfirst.position + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + playerfirst.name + " bought " + Places.get(where_at));
            }
        } else if (railroads.containsKey(Places.get(where_at))) {
            int a = railroadsell.railroadsell(playerfirst,playersecond,arrOfStr,where_at,player1,player2,Places,railroads,Place_Status,banker) ;
            if (a == 1){
                show();
                System.exit(0);
            }
        } else if (company.containsKey(Places.get(where_at))) {
            int a = companysell.companysell(playerfirst,playersecond,arrOfStr,where_at,player1,player2,Places,company,Place_Status,banker);
            if (a == 1){
                show();
                System.exit(0);
            }
        }
    }

    public void playerrent(Users playerfirst, Users playersecond, String[] arrOfStr, int where_at) {
        if(Places.get(where_at).equals("Vine Street") && threespaces){
            int x = land.landrent(playerfirst,playersecond,where_at,Places,lands);
            if(x == 1){
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + playersecond.name + " goes bankrupt");
                show();
                System.exit(0);
            }
            else{
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Go back 3 spaces " + arrOfStr[0] + " paid rent for Vine Street");
            }
        }
        else if(Places.get(where_at).equals("Leicester Square") && leicestersquare){
            int x = land.landrent(playerfirst,playersecond,where_at,Places,lands);
            if(x == 1){
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + playersecond.name + " goes bankrupt");
                show();
                System.exit(0);
            }
            else {
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " draw Chance - Advance to Leicester Square " + arrOfStr[0] + " paid rent for Leicester Square");
            }
        }
        else if (lands.containsKey(Places.get(where_at))) {
            int x = land.landrent(playerfirst,playersecond,where_at,Places,lands);
            if(x == 1){
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + playersecond.name + " goes bankrupt");
                show();
                System.exit(0);
            }
            else{
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " paid rent for " + Places.get(where_at));
            }

        } else if (company.containsKey(Places.get(where_at))) {
            int x = companysell.companyrent(playerfirst,playersecond,arrOfStr);
            if(x == 1){
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + playersecond.name + " goes bankrupt");
                show();
                System.exit(0);
            }
            else{
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " paid rent for " + Places.get(where_at));
            }
        } else if (railroads.containsKey(Places.get(where_at))) {
            int x = railroadsell.railroadrent(playerfirst,playersecond);
            if(x == 1){
                System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + playersecond.name + " goes bankrupt");
                show();
                System.exit(0);
            }
            System.out.println(arrOfStr[0] + "\t" + arrOfStr[1] + "\t" + where_at + "\t" + df.format(player1.money) + "\t" + df.format(player2.money) + "\t" + arrOfStr[0] + " paid rent for " + Places.get(where_at));
        }
    }
}