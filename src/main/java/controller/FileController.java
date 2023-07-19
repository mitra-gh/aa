package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileController {
    public static void updateFile(String text, String address){
        try {
            FileWriter myWriter = new FileWriter(address);
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String readFile(String address){
        try {

            File file = new File(address);
            Scanner myReader = new Scanner(file);
            StringBuilder output = new StringBuilder();

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                output.append(data);
            }
            myReader.close();

            return output.toString();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "";
    }
}
