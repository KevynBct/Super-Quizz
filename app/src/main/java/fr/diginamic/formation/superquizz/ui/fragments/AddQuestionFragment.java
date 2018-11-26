package fr.diginamic.formation.superquizz.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import fr.diginamic.formation.superquizz.R;
import fr.diginamic.formation.superquizz.model.Question;
import fr.diginamic.formation.superquizz.model.TypeQuestion;

public class AddQuestionFragment extends Fragment {
    private static final String ARG_QUESTION = "question";
    private static final String ARG_EDIT = "edit";
    private Question edit_question;
    private Boolean edit = false;

    private AddQuestionListener mListener;

    public AddQuestionFragment() {
        // Required empty public constructor
    }

    public static AddQuestionFragment newInstance(Question question, Boolean edit) {
        AddQuestionFragment fragment = new AddQuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_QUESTION, question);
        args.putBoolean(ARG_EDIT, edit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            edit_question = getArguments().getParcelable(ARG_QUESTION);
            edit = getArguments().getBoolean(ARG_EDIT, false);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_question, container, false);
        view.findViewById(R.id.radio_button_1).setOnClickListener(this::radioButtonCheck);
        view.findViewById(R.id.radio_button_2).setOnClickListener(this::radioButtonCheck);
        view.findViewById(R.id.radio_button_3).setOnClickListener(this::radioButtonCheck);
        view.findViewById(R.id.radio_button_4).setOnClickListener(this::radioButtonCheck);

        view.findViewById(R.id.add_question_button).setOnClickListener(v -> addQuestion());

        if(edit){
            ((TextView) view.findViewById(R.id.edit_question_intitule)).setText(edit_question.getEntitle());
            ((TextView) view.findViewById(R.id.edit_answer_1)).setText(edit_question.getProposition(0));
            ((TextView) view.findViewById(R.id.edit_answer_2)).setText(edit_question.getProposition(1));
            ((TextView) view.findViewById(R.id.edit_answer_3)).setText(edit_question.getProposition(2));
            ((TextView) view.findViewById(R.id.edit_answer_4)).setText(edit_question.getProposition(3));

            if(edit_question.getGoodAnswer().equals(edit_question.getProposition(0))){
                ((RadioButton) view.findViewById(R.id.radio_button_1)).setChecked(true);
            }else if(edit_question.getGoodAnswer().equals(edit_question.getProposition(1))){
                ((RadioButton) view.findViewById(R.id.radio_button_2)).setChecked(true);
            }else if (edit_question.getGoodAnswer().equals(edit_question.getProposition(2))){
                ((RadioButton) view.findViewById(R.id.radio_button_3)).setChecked(true);
            }else if (edit_question.getGoodAnswer().equals(edit_question.getProposition(3))){
                ((RadioButton) view.findViewById(R.id.radio_button_4)).setChecked(true);
            }

            ((FloatingActionButton) view.findViewById(R.id.add_question_button)).setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_white_24dp));
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddQuestionListener) {
            mListener = (AddQuestionListener) context;
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
                question.setGoodAnswer(((TextView) getView().findViewById(R.id.edit_answer_1)).getText().toString());
            } else if (((RadioButton) getView().findViewById(R.id.radio_button_2)).isChecked()){
                question.setGoodAnswer(((TextView) getView().findViewById(R.id.edit_answer_2)).getText().toString());
            }else if(((RadioButton) getView().findViewById(R.id.radio_button_3)).isChecked()){
                question.setGoodAnswer(((TextView) getView().findViewById(R.id.edit_answer_3)).getText().toString());
            }else if(((RadioButton) getView().findViewById(R.id.radio_button_4)).isChecked()){
                question.setGoodAnswer(((TextView) getView().findViewById(R.id.edit_answer_4)).getText().toString());
            }

            question.setType(TypeQuestion.SIMPLE);

            if(edit){
                mListener.editQuestion(question, edit_question.getId());
                Toast.makeText(getContext(), "Question modifiée", Toast.LENGTH_SHORT).show();
            }else{
                mListener.saveQuestion(question);
                Toast.makeText(getContext(), "Question ajoutée", Toast.LENGTH_SHORT).show();
            }

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

        if(intitule.getText().toString().isEmpty()
        || answer1.getText().toString().isEmpty()
        || answer2.getText().toString().isEmpty()
        || answer3.getText().toString().isEmpty()
        || answer4.getText().toString().isEmpty()){
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

    public interface AddQuestionListener {
        void saveQuestion(Question question);
        void editQuestion(Question question, int id);
    }
}
