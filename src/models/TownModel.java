package models;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Helper;

import java.io.IOException;

public class TownModel {

    public static final String URI = "https://stark-crag-00731.herokuapp.com/api/";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public JSONArray getTowns() {
        return towns;
    }

    JSONObject town;
    JSONArray towns;
    OkHttpClient okHttpClient;

    public TownModel() {
        okHttpClient = new OkHttpClient();
    }

    public void refreshTowns() {
        Request request = new Request.Builder().url(URI + "towns").build();
        String response = null;
        try {
            response = okHttpClient.newCall(request).execute().body().string();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        towns = new JSONArray(response);
    }

    public void editTown(String json, String _id) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "town/" + _id).put(requestBody)
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();
        String response = null;

        try {
            Response response1 = okHttpClient.newCall(request).execute();


            if (response1.code() >= 400) {
                new JSONObject().put("error", " LOL i don't have time ");
            }
            ;

            response = response1.body().string();
        } catch (IOException e) {
            throw new Error();
        }

        this.town = new JSONObject(response);


    }

    public  void deleteTown(String _id){

        Request request = new Request.Builder().url(URI + "town/" + _id).delete()
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();

        String response = null;

        try {
            Response response1 = okHttpClient.newCall(request).execute();


            if (response1.code() >= 400) {
                new JSONObject().put("error", " LOL i don't have time ");
            }
            ;

            response = response1.body().string();
        } catch (IOException e) {
            throw new Error();
        }
    }
    public  void addTown(String json){
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "town" ).post(requestBody)
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();

        String response = null;

        try {
            Response response1 = okHttpClient.newCall(request).execute();


            if (response1.code() >= 400) {
                new JSONObject().put("error", " LOL i don't have time ");
            }
            ;

            response = response1.body().string();
            System.out.println(response);
        } catch (IOException e) {
            throw new Error();
        }
    }
    public  JSONArray getTownName(){

        Request request = new Request.Builder().url(URI + "townsname").build();
        String response = null;
        try {
            response = okHttpClient.newCall(request).execute().body().string();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
       return new JSONArray(response);

    }

    public JSONObject editTownCase(String json, String _id) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "/case/town/" + _id).put(requestBody)
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();
        String response = null;

        try {
            Response response1 = okHttpClient.newCall(request).execute();


            if (response1.code() >= 400) {
                new JSONObject().put("error", " LOL i don't have time ");
            }
            ;

            response = response1.body().string();
            System.out.println(response);
        } catch (IOException e) {
            throw new Error();
        }
        return new JSONObject(response);

    }

    public void addTownCase(String json) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "/case/town").post(requestBody)
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();

        String response = null;

        try {
            Response response1 = okHttpClient.newCall(request).execute();


            if (response1.code() >= 400) {
                new JSONObject().put("error", " LOL i don't have time ");
            }
            ;

            response = response1.body().string();
            System.out.println(response);
        } catch (IOException e) {
            throw new Error();
        }
    }

    public void deleteTownCase(String _id) {

        Request request = new Request.Builder().url(URI + "/case/town/" + _id).delete()
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();

        String response = null;

        try {
            Response response1 = okHttpClient.newCall(request).execute();


            if (response1.code() >= 400) {
                new JSONObject().put("error", " LOL i don't have time ");
            }
            ;

            response = response1.body().string();
        } catch (IOException e) {
            throw new Error();
        }
    }

    public JSONArray getTownCases() {
        Request request = new Request.Builder().url(URI + "/case/town").build();
        String response = null;
        try {
            response = okHttpClient.newCall(request).execute().body().string();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONArray(response);
    }

}
