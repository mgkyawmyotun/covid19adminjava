package models;

import com.sun.javaws.exceptions.ErrorCodeResponseException;
import controllers.Main;
import okhttp3.*;
import org.json.JSONObject;
import utils.Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class UserModel {
    public static final String URI = "https://stark-crag-00731.herokuapp.com/user/";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient okHttpClient;

    JSONObject user;
    String token;
    public UserModel() {
        okHttpClient = new OkHttpClient();
    }
    public JSONObject getUser() {
        Request request = new Request.Builder().url(URI)
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();
        String response = null;
        try {
            response = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject(response);

    }

    public JSONObject createUser(String json) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(URI + "register").post(requestBody).build();
        String response = null;
        try {
            response = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(response);
    }

    public  JSONObject loginUser(String json){
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(URI + "login").post(requestBody).build();
        String response = null;

        try {
          Response response1=  okHttpClient.newCall(request).execute();


          if(response1.code()>=400) {
                new JSONObject().put("error","Invalid Email or Password");
          };

          response = response1.body().string();
        } catch (IOException e) {
            throw new Error();
        }
        System.out.println(response);
        this.user =new JSONObject(response);
        System.out.println(response);
        return new JSONObject(response);
    }
    public void saveToken(){

        Main.preferences.put("token",this.user.getString("token"));
//        Path filePath = Paths.get("src/token.txt");
//        if(!Files.exists(filePath)){
//            try {
//            Files.createFile(filePath);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            Files.write(filePath,this.user.getString("token").getBytes());
//             String res =Files.readAllLines(filePath).stream().collect(Collectors.joining());
//               this.token =res;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    public  String getToken(){
        return  token;
    }
    public  String getUserName(){
        return  this.user.getString("username");
    }
    public  String getEmail(){
        return  this.user.getString("email");
    };

}
