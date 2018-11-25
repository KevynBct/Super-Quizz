package fr.diginamic.formation.superquizz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import fr.diginamic.formation.superquizz.model.Question;
import fr.diginamic.formation.superquizz.model.TypeQuestion;

public class QuestionsDatabaseHelper extends SQLiteOpenHelper implements APIClient.APIResult<ArrayList<Question>> {
    private static QuestionsDatabaseHelper questionsDatabaseHelper;
    // Database Info
    private static final String DATABASE_NAME = "questionsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_QCM = "qcm";

    // Post Table Columns
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String ENTITLE = "question";
    private static final String ANSWER_1 = "answer_1";
    private static final String ANSWER_2 = "answer_2";
    private static final String ANSWER_3 = "answer_3";
    private static final String ANSWER_4 = "answer_4";
    private static final String GOOD_ANSWER = "good_answer";
    private static final String USER_ANSWER = "user_answer";
    private static final String QUESTION_TYPE = "question_type";

    public static synchronized QuestionsDatabaseHelper getInstance(Context context) {
        if (questionsDatabaseHelper == null) {
            questionsDatabaseHelper = new QuestionsDatabaseHelper(context.getApplicationContext());
            APIClient.getInstance().getQuestions(questionsDatabaseHelper);
        }
        return questionsDatabaseHelper;
    }

    private QuestionsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QCM +
                "(" +
                KEY_QUESTION_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                ENTITLE + " TEXT" + "," +
                ANSWER_1 + " TEXT" + "," +
                ANSWER_2 + " TEXT" + "," +
                ANSWER_3 + " TEXT" + "," +
                ANSWER_4 + " TEXT" + "," +
                GOOD_ANSWER + " TEXT" + "," +
                USER_ANSWER + " TEXT" + "," +
                QUESTION_TYPE + " TEXT" +
                ")";

        db.execSQL(CREATE_QUESTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QCM);
            onCreate(db);
        }
    }

    public void addQuestion(Question question) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ENTITLE, question.getEntitle());
            values.put(ANSWER_1, question.getProposition(0));
            values.put(ANSWER_2, question.getProposition(1));
            values.put(ANSWER_3, question.getProposition(2));
            values.put(ANSWER_4, question.getProposition(3));
            values.put(GOOD_ANSWER, question.getGoodAnswer());
            values.put(QUESTION_TYPE, question.getType().toString());
            if(question.getId() > 0){
                values.put(KEY_QUESTION_ID, question.getId());
            }

            db.insertOrThrow(TABLE_QCM, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DataBase ERROR", "Error while trying to add question to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Question> getAllQuestions() {

        ArrayList<Question> questions = new ArrayList<>();

        String QCM_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_QCM);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QCM_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Question newQuestion = new Question(cursor.getString(cursor.getColumnIndex(ENTITLE)));
                    newQuestion.addProposition(cursor.getString(cursor.getColumnIndex(ANSWER_1)));
                    newQuestion.addProposition(cursor.getString(cursor.getColumnIndex(ANSWER_2)));
                    newQuestion.addProposition(cursor.getString(cursor.getColumnIndex(ANSWER_3)));
                    newQuestion.addProposition(cursor.getString(cursor.getColumnIndex(ANSWER_4)));
                    newQuestion.setGoodAnswer(cursor.getString(cursor.getColumnIndex(GOOD_ANSWER)));
                    newQuestion.setId(cursor.getInt(cursor.getColumnIndex(KEY_QUESTION_ID)));
                    newQuestion.setType(TypeQuestion.SIMPLE);

                    questions.add(newQuestion);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DataBase ERROR", "Error while trying to get questions from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return questions;
    }

    public int updateQuestion(Question question, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ENTITLE, question.getEntitle());
        values.put(ANSWER_1, question.getProposition(0));
        values.put(ANSWER_2, question.getProposition(1));
        values.put(ANSWER_3, question.getProposition(2));
        values.put(ANSWER_4, question.getProposition(3));
        values.put(GOOD_ANSWER, question.getGoodAnswer());

        return db.update(TABLE_QCM, values, KEY_QUESTION_ID + " = ?",
                new String[] { String.valueOf(id)});
    }

    public void deleteAllQuestions() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_QCM, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("DataBase ERROR", "Error while trying to delete all questions");
        } finally {
            db.endTransaction();
        }
    }

    public void initOnlineQuestions(ArrayList<Question> onlineQuestionsList){
        ArrayList<Integer> localIdList = new ArrayList<>();

        for (Question question : this.getAllQuestions()){
            localIdList.add(question.getId());
        }
        for (Question question : onlineQuestionsList) {
            if(!localIdList.contains(question.getId())){
                addQuestion(question);
            }
        }
    }

    @Override
    public void onFailure(IOException e) {
    }

    @Override
    public void OnSuccess(ArrayList<Question> questionsList) throws IOException {
        initOnlineQuestions(questionsList);
    }
}
