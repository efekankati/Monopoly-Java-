import org.json.simple.parser.ParseException;

import java.io.IOException;
public class Main {
    public static void main(String[] args)throws IOException, ParseException {
        jsonread read = new jsonread();
        read.propertyreader("./property.json");
        read.listreader("./list.json");
        read.commandreader(args[0]);
    }
}