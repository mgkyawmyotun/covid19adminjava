package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

public class Helper {

    public  static  void outPutAll(ResultSet res){

        try {
            ResultSetMetaData rsmd =res.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for(int i=1;i<=columnCount;i++){
               System.out.print( rsmd.getColumnName(i)+ "\t");
            }
            System.out.println("\n");
            while (res.next()){
                for(int i=1;i<=columnCount;i++){
                    System.out.print(res.getString(i) + "\t");
                }
                System.out.println();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    public  static  boolean search(String first_value ,String second_value){
             return  first_value.toLowerCase().contains(second_value.toLowerCase());

    }
    public  static  String getToken(){
        Path filePath = Paths.get("src/token.txt");
        String res = null;
        try {
            res = Files.readAllLines(filePath).stream().collect(Collectors.joining());
        } catch (IOException e) {
            return  "";
        }
        return  res;
    }
    public  static  void  deleteToken(){
        Path filePath = Paths.get("src/token.txt");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  static  String formatDate(String s){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(s, inputFormatter);
        String formattedDate = outputFormatter.format(date);
        return  formattedDate;
    }
    public  static  LocalDate formatLocalDate(String s){
        DateTimeFormatter input = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(s, input);

        return  date;
    }
}
