package fr.diginamic.formation.superquizz.ui.thread;

import android.os.AsyncTask;
import android.os.SystemClock;

public class QuestionTask extends AsyncTask<Void, Integer, String> {
    int count = 0;
    private QuestionTaskListener listener;


    public  QuestionTask(QuestionTaskListener listener){
        super();
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(Void... params) {
        while (count < 100) {
            if(isCancelled()){
                break;
            }
            SystemClock.sleep(20);
            count++;
            publishProgress(count);
        }
        return "Complete";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        listener.onProgressQuestionTask(count);
    }

    public interface QuestionTaskListener{
        void onProgressQuestionTask(int count);
    }
}