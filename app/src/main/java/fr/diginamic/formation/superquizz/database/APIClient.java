package fr.diginamic.formation.superquizz.database;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import fr.diginamic.formation.superquizz.model.Question;
import fr.diginamic.formation.superquizz.model.TypeQuestion;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIClient {

    private final OkHttpClient client = new OkHttpClient();
    private final String url = "http://192.168.10.38:3000/questions/";

    private static APIClient sInstance;

    public static synchronized APIClient getInstance() {
        if (sInstance == null) {
            sInstance = new APIClient();
        }
        return sInstance;
    }

    public void getQuestions(final APIResult<ArrayList<Question>> result) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.onFailure(e);
                Log.i("REQUEST_QUESTION", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Question> questions = new ArrayList<>();

                try {
                    String responseData = response.body().string();
                    JSONArray json = new JSONArray(responseData);

                    for (int i = 0; i < json.length(); i++) {
                        Question question = new Question(json.getJSONObject(i).getString("title"));
                        question.addProposition(json.getJSONObject(i).getString("answer_1"));
                        question.addProposition(json.getJSONObject(i).getString("answer_2"));
                        question.addProposition(json.getJSONObject(i).getString("answer_3"));
                        question.addProposition(json.getJSONObject(i).getString("answer_4"));
                        question.setId(json.getJSONObject(i).getInt("id"));
                        question.setGoodAnswer(question.getProposition(json.getJSONObject(i).getInt("correct_answer")-1));
                        question.setType(TypeQuestion.SIMPLE);

                        questions.add(question);
                    }
                } catch (JSONException e) {

                }
                result.OnSuccess(questions);
            }
        });
    }

    public void addQuestion(Question question){
//        Log.i("REQUEST_QUESTION", "addQuestion - Step 1 BEGIN");
//        JSONObject jsonObject = new JSONObject();
//        try {
//            Log.i("REQUEST_QUESTION", "addQuestion - Step 2");
//
//            jsonObject.put("title",question.getEntitle());
//            jsonObject.put("answer_1", question.getProposition(0));
//            jsonObject.put("answer_2", question.getProposition(1));
//            jsonObject.put("answer_3", question.getProposition(2));
//            jsonObject.put("answer_4", question.getProposition(3));
//            jsonObject.put("correct_answer", question.getGoodAnswerNumber());
//            jsonObject.put("author", "Kevyn");
//
//            Log.i("REQUEST_QUESTION", "addQuestion - Step 3");
//
//            Log.i("REQUEST_QUESTION", jsonObject.toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.i("REQUEST_QUESTION", e.getMessage());
//        }
//
//        Log.i("REQUEST_QUESTION", "addQuestion - Step 4");
//
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        // put your json here
//        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
//        Request request = new Request.Builder()
//                .header("Content-Type", "application/json")
//                .url(url)
//                .post(body)
//                .build();
//
//        Log.i("REQUEST_QUESTION", "addQuestion - Step 5");
//
//        Response response = null;
//        try {
//
//            Log.i("REQUEST_QUESTION", "addQuestion - Step 6");
//            response = client.newCall(request).execute();
//            String resStr = response.body().string();
//            Log.i("REQUEST_QUESTION", resStr);
//
//            Log.i("REQUEST_QUESTION", "addQuestion - Step 7");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Log.i("REQUEST_QUESTION", "addQuestion - Step 8  END");

    }

    public void deleteQuestion(final APIResult<Question> result, Question question) {

        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");


        Request request = new Request.Builder().url(url + question.getId()).delete().build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to delete quetion with id:" + question.getId());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //TODO : Faire un update

    public interface APIResult<T> {
        void onFailure(IOException e);
        void OnSuccess(T object) throws IOException;
    }
}