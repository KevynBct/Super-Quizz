package fr.diginamic.formation.superquizz.database;

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
    private static final String KEY_QUESTION_ID = "id";
    private static final String ENTITLE = "title";
    private static final String ANSWER_1 = "answer_1";
    private static final String ANSWER_2 = "answer_2";
    private static final String ANSWER_3 = "answer_3";
    private static final String ANSWER_4 = "answer_4";
    private static final String CORRECT_ANSWER = "correct_answer";
    private static final String AUTHOR = "author";
    private final String url = "http://192.168.10.204:3000/questions/";

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
                        Question question = new Question(json.getJSONObject(i).getString(ENTITLE));
                        question.addProposition(json.getJSONObject(i).getString(ANSWER_1));
                        question.addProposition(json.getJSONObject(i).getString(ANSWER_2));
                        question.addProposition(json.getJSONObject(i).getString(ANSWER_3));
                        question.addProposition(json.getJSONObject(i).getString(ANSWER_4));
                        question.setId(json.getJSONObject(i).getInt(KEY_QUESTION_ID));
                        question.setGoodAnswer(question.getProposition(json.getJSONObject(i).getInt(CORRECT_ANSWER)-1));
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ENTITLE,question.getEntitle());
            jsonObject.put(ANSWER_1, question.getProposition(0));
            jsonObject.put(ANSWER_2, question.getProposition(1));
            jsonObject.put(ANSWER_3, question.getProposition(2));
            jsonObject.put(ANSWER_4, question.getProposition(3));
            jsonObject.put(CORRECT_ANSWER, question.getGoodAnswerNumber());
            jsonObject.put(AUTHOR, "Kevyn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //result.onFailure(e);
                Log.i("REQUEST_QUESTION", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("REQUEST_QUESTION", "Question ajoutée au serveur");
            }
        });
    }

    public void deleteQuestion(Question question) {
        Request request = new Request.Builder().url(url + question.getId()).delete().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //result.onFailure(e);
                Log.i("REQUEST_QUESTION", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("REQUEST_QUESTION", "Question "+String.valueOf(question.getId())+ "supprimée du serveur");
            }
        });


    }

    public void updateQuestion(Question question, int id){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ENTITLE,question.getEntitle());
            jsonObject.put(ANSWER_1, question.getProposition(0));
            jsonObject.put(ANSWER_2, question.getProposition(1));
            jsonObject.put(ANSWER_3, question.getProposition(2));
            jsonObject.put(ANSWER_4, question.getProposition(3));
            jsonObject.put(CORRECT_ANSWER, question.getGoodAnswerNumber());
            jsonObject.put(AUTHOR, "Kevyn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .url(url+id)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //result.onFailure(e);
                Log.i("REQUEST_QUESTION", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("REQUEST_QUESTION", "Question modifée sur le serveur");
            }
        });
    }

    public interface APIResult<T> {
        void onFailure(IOException e);
        void OnSuccess(T object) throws IOException;
    }
}