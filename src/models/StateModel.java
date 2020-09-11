package models;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Helper;

import java.io.IOException;

public class StateModel {


    public static final String URI = "https://stark-crag-00731.herokuapp.com/api/";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public JSONArray getStates() {
        return states;
    }

    JSONObject state;
    JSONArray states;
    OkHttpClient okHttpClient;

    public StateModel() {
        okHttpClient = new OkHttpClient();
    }

    public void refreshStates() {
        Request request = new Request.Builder().url(URI + "states").build();
        String response = null;
        try {
            response = okHttpClient.newCall(request).execute().body().string();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        states = new JSONArray(response);
    }

    public void editState(String json, String _id) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "state/" + _id).put(requestBody)
                .addHeader("Authorization", "Bearer " + Helper.getToken()).build();
        String response = null;

        try {
            Response response1 = okHttpClient.newCall(request).execute();


            if (response1.code() >= 400) {
                new JSONObject().put("error", " LOL i don't have time ");
            }

            response = response1.body().string();
        } catch (IOException e) {
            throw new Error();
        }
        System.out.println(response);
        this.state = new JSONObject(response);


    }

    public void deleteState(String _id) {

        Request request = new Request.Builder().url(URI + "state/" + _id).delete()
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

    public void addState(String json) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "state").post(requestBody)
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

    public JSONObject editStateCase(String json, String _id) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "/case/state/" + _id).put(requestBody)
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

    public void addStateCase(String json) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URI + "/case/state").post(requestBody)
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

    public void deleteStateCase(String _id) {

        Request request = new Request.Builder().url(URI + "/case/state/" + _id).delete()
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

    public JSONArray getStateCases() {
        Request request = new Request.Builder().url(URI + "/case/state").build();
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
