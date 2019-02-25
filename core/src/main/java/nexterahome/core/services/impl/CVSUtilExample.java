package nexterahome.core.services.impl;


import java.io.FileWriter;
import java.util.Arrays;

public class CVSUtilExample {

    public static void main(String[] args) throws Exception {

        String csvFile = "C:/Users/THASR03/Desktop/aem/part-time/abc.csv";
        FileWriter writer = new FileWriter(csvFile);


        //custom separator + quote
        CSVUtils.writeLine(writer, Arrays.asList("tes;iydus", "bb,b", "cc,c"), ',', '"');

        //custom separator + quote

        //double-quotes


        writer.flush();
        writer.close();

    }

}