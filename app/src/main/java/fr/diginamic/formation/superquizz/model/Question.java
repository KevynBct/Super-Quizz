package fr.diginamic.formation.superquizz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Question implements Parcelable {

    private int id;
    private String entitle;
    private ArrayList<String> propositions;
    private String goodAnswer;
    private TypeQuestion type;

    public Question(String entitle) {
        this.entitle = entitle;
        this.propositions = new ArrayList<String>(4);
    }

    protected Question(Parcel p) {
        entitle = p.readString();
        propositions = p.createStringArrayList();
        goodAnswer = p.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public void addProposition(String proposition) {
        this.propositions.add(proposition);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntitle() {
        return entitle;
    }

    public String getProposition(int index){
        return propositions.get(index);
    }

    public String getGoodAnswer() {
        return goodAnswer;
    }

    public void setGoodAnswer(String goodAnswer) {
        this.goodAnswer = goodAnswer;
    }

    public int getGoodAnswerNumber(){
        if(goodAnswer.equals(getProposition(0))){
            return 1;
        }else if (goodAnswer.equals(getProposition(1))){
            return 2;
        }else if (goodAnswer.equals(getProposition(2))){
            return 3;
        }else if((goodAnswer.equals(getProposition(3)))){
            return 4;
        }else {
            return -1;
        }
    }

    public TypeQuestion getType() {
        return type;
    }

    public void setType(TypeQuestion type) {
        this.type = type;
    }

    public int getPoint(){
        if (this.type == TypeQuestion.DOUBLE){
            return 2;
        }else{
            return 1;
        }
    }

    public String toString() {
        String listeReponse ="\n";
        int index = 0;
        for(String rep : propositions) {
            index++;
            listeReponse += "\t"+index+" - "+rep+"\n";
        }
        return this.entitle +listeReponse;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entitle);
        dest.writeStringList(propositions);
        dest.writeString(goodAnswer);
    }
}
