package fr.diginamic.formation.superquizz.database;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import fr.diginamic.formation.superquizz.model.Question;
import fr.diginamic.formation.superquizz.model.TypeQuestion;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIClient {

    private final OkHttpClient client = new OkHttpClient();

    private static APIClient sInstance;

    public static synchronized APIClient getInstance() {
        if (sInstance == null) {
            sInstance = new APIClient();
        }
        return sInstance;
    }

    public void getQuestions(final APIResult<ArrayList<Question>> result) {

        Request request = new Request.Builder()
                .url("http://192.168.10.38:3000/questions")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.onFailure(e);
                Log.i("REQUESTQUESTION", e.getMessage());
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

    //TODO : Faire un update
    //TODO : Faire un delete
    //TODO : Faire un Create

    public interface APIResult<T> {
        void onFailure(IOException e);
        void OnSuccess(T object) throws IOException;
    }
}