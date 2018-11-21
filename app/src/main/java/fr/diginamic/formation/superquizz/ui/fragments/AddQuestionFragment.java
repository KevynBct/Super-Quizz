package fr.diginamic.formation.superquizz.ui.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import fr.diginamic.formation.superquizz.R;
import fr.diginamic.formation.superquizz.model.Question;
import fr.diginamic.formation.superquizz.model.TypeQuestion;
import fr.diginamic.formation.superquizz.ui.activities.MainActivity;
import fr.diginamic.formation.superquizz.ui.activities.QuestionActivity;

public class AddQuestionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddQuestionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddQuestionFragment newInstance(String param1, String param2) {
        AddQuestionFragment fragment = new AddQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_question, container, false);
        view.findViewById(R.id.radio_button_1).setOnClickListener(v -> radioButtonCheck(v));
        view.findViewById(R.id.radio_button_2).setOnClickListener(v -> radioButtonCheck(v));
        view.findViewById(R.id.radio_button_3).setOnClickListener(v -> radioButtonCheck(v));
        view.findViewById(R.id.radio_button_4).setOnClickListener(v -> radioButtonCheck(v));

        view.findViewById(R.id.add_question_button).setOnClickListener(v -> addQuestion());
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Question question) {
        if (mListener != null) {
            mListener.saveQuestion(question);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void radioButtonCheck(View view){
        ((RadioButton) getView().findViewById(R.id.radio_button_1)).setChecked(false);
        ((RadioButton) getView().findViewById(R.id.radio_button_2)).setChecked(false);
        ((RadioButton) getView().findViewById(R.id.radio_button_3)).setChecked(false);
        ((RadioButton) getView().findViewById(R.id.radio_button_4)).setChecked(false);

        ((RadioButton) view).setChecked(true);
    }

    private void addQuestion(){
        if(verifyQuestion()){

            Question question = new Question(((TextView) getView().findViewById(R.id.edit_question_intitule)).getText().toString());
            question.addProposition(((TextView) getView().findViewById(R.id.edit_answer_1)).getText().toString());
            question.addProposition(((TextView) getView().findViewById(R.id.edit_answer_2)).getText().toString());
            question.addProposition(((TextView) getView().findViewById(R.id.edit_answer_3)).getText().toString());
            question.addProposition(((TextView) getView().findViewById(R.id.edit_answer_4)).getText().toString());

            if(((RadioButton) getView().findViewById(R.id.radio_button_1)).isChecked()){
                question.setBonneReponse(1);
            } else if (((RadioButton) getView().findViewById(R.id.radio_button_2)).isChecked()){
                question.setBonneReponse(2);
            }else if(((RadioButton) getView().findViewById(R.id.radio_button_3)).isChecked()){
                question.setBonneReponse(3);
            }else if(((RadioButton) getView().findViewById(R.id.radio_button_4)).isChecked()){
                question.setBonneReponse(4);
            }

            question.setType(TypeQuestion.SIMPLE);

            mListener.saveQuestion(question);

            Toast.makeText(getContext(), "Question ajoutée", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Des champs ne sont pas valides", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verifyQuestion(){
        boolean isOk = true;
        EditText intitule = getView().findViewById(R.id.edit_question_intitule);
        EditText answer1 = getView().findViewById(R.id.edit_answer_1);
        EditText answer2 = getView().findViewById(R.id.edit_answer_2);
        EditText answer3 = getView().findViewById(R.id.edit_answer_3);
        EditText answer4 = getView().findViewById(R.id.edit_answer_4);
        int goodAnswer = 0;

        if(intitule.getText().toString().isEmpty()){
            intitule.setBackgroundColor(Color.RED);
            isOk = false;
        }

        if(answer1.getText().toString().isEmpty()){
            answer1.setBackgroundColor(Color.RED);
            isOk = false;
        }

        if(answer2.getText().toString().isEmpty()){
            answer3.setBackgroundColor(Color.RED);
            isOk = false;
        }

        if(answer3.getText().toString().isEmpty()){
            answer3.setBackgroundColor(Color.RED);
            isOk = false;
        }

        if(answer4.getText().toString().isEmpty()){
            answer4.setBackgroundColor(Color.RED);
            isOk = false;
        }

        if(!((RadioButton) getView().findViewById(R.id.radio_button_1)).isChecked()
                && !((RadioButton) getView().findViewById(R.id.radio_button_2)).isChecked()
                && !((RadioButton) getView().findViewById(R.id.radio_button_3)).isChecked()
                && !((RadioButton) getView().findViewById(R.id.radio_button_4)).isChecked()){
            isOk = false;
        }

        return isOk;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void saveQuestion(Question question);
    }
}
