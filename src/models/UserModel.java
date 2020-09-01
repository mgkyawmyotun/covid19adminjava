package models;

import controllers.Main;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Helper;

import java.io.IOException;

public class UserModel {
    public static final String URI = "https://stark-crag-00731.herokuapp.com/user";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient okHttpClient;

    JSONObject user;
    String token;

    public UserModel() {
        okHttpClient = new OkHttpClient();
    }

    public JSONObject getUser() {
        Request request = new Request.Builder()
                .url(URI)
                .addHeader("Authorization", "Bearer " + Helper.getToken())
                .build();
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
        Request request = new Request.Builder()
                .url(URI + "/register")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + Helper.getToken())
                .build();
        String response = null;
        try {
            response = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(response);
    }

    public JSONObject loginUser(String json) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(URI + "/login").post(requestBody).build();
        String response = null;

        try {
            Response response1 = okHttpClient.newCall(request).execute();


            if (response1.code() >= 400) {
                new JSONObject().put("error", "Invalid Email or Password");
            }
            ;

            response = response1.body().string();
        } catch (IOException e) {
            throw new Error();
        }
        System.out.println(response);
        this.user = new JSONObject(response);
        System.out.println(response);
        return new JSONObject(response);
    }

    public JSONObject changeUser(String json) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(URI).put(requestBody)
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();
        String response;
        try {
            Response response1 = okHttpClient.newCall(request).execute();


            if (response1.code() >= 400) {
                new JSONObject().put("error", "Invalid Email or Password");
            }
            ;

            response = response1.body().string();
        } catch (IOException e) {
            throw new Error();
        }

        return new JSONObject(response);


    }

    public JSONObject changePassword(String json) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(URI + "/change").put(requestBody)
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();
        String response;
        try {
            Response response1 = okHttpClient.newCall(request).execute();
            response = response1.body().string();
        } catch (IOException e) {
            throw new Error();
        }

        return new JSONObject(response);

    }
    public JSONArray getAllUsers(){

        Request request =new Request.Builder().url(URI+"/all")
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();
        String response;
        try {
            Response response1 = okHttpClient.newCall(request).execute();
            response = response1.body().string();
        } catch (IOException e) {
            throw new Error();
        }

        return new JSONArray(response);

    }

    public void saveToken() {

        Main.preferences.put("token", this.user.getString("token"));


    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return this.user.getString("username");
    }

    public String getEmail() {
        return this.user.getString("email");
    }

    ;

}
