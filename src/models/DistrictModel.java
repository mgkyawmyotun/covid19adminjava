package models;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Helper;

import java.io.IOException;

public class DistrictModel {
    public static final String URI = "https://stark-crag-00731.herokuapp.com/api/";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public JSONArray getDistricts() {
        return districts;
    }

    JSONObject district;
    JSONArray districts;
    OkHttpClient okHttpClient;

    public DistrictModel() {
        okHttpClient = new OkHttpClient();
    }

    public void refreshDistricts() {
        Request request = new Request.Builder().url(URI + "districts").build();
        String response = null;
        try {
            response = okHttpClient.newCall(request).execute().body().string();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        districts = new JSONArray(response);
    }

    public void editDistrict(String json, String _id) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "district/" + _id).put(requestBody)
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

        this.district = new JSONObject(response);


    }

    public  void deleteDistrict(String _id){

        Request request = new Request.Builder().url(URI + "district/" + _id).delete()
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
    public  void addDistrict(String json){
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "district" ).post(requestBody)
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
    public  JSONArray getDistrictName(){

        Request request = new Request.Builder().url(URI + "districtname").build();
        String response = null;
        try {
            response = okHttpClient.newCall(request).execute().body().string();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray(response);

    }

    public JSONObject editDistrictCase(String json, String _id) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "/case/district/" + _id).put(requestBody)
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

    public void addDistrictCase(String json) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "/case/district").post(requestBody)
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

    public void deleteDistrictCase(String _id) {

        Request request = new Request.Builder().url(URI + "/case/district/" + _id).delete()
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

    public JSONArray getDistrictCases() {
        Request request = new Request.Builder().url(URI + "/case/district").build();
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
