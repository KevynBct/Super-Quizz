package fr.diginamic.formation.superquizz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Question implements Parcelable {
    private String intitule;
    private ArrayList<String> propositions;
    private int bonneReponse;
    private TypeQuestion type;

    public Question(String intitule) {
        this.intitule = intitule;
        this.propositions = new ArrayList<String>(4);
    }

    protected Question(Parcel p) {
        intitule = p.readString();
        propositions = p.createStringArrayList();
        bonneReponse = p.readInt();
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

    public Boolean verifierReponse(int reponse){
        if (this.bonneReponse == reponse)
            return true;
        else
            return false;
    }

    public void addProposition(String proposition) {
        this.propositions.add(proposition);
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public ArrayList<String> getPropositionsList() {
        return propositions;
    }

    public String getProposition(int index){
        return propositions.get(index);
    }

    public void setPropositions(ArrayList<String> propositions) {
        this.propositions = propositions;
    }

    public int getBonneReponse() {
        return bonneReponse;
    }

    public void setBonneReponse(int bonneReponse) {
        this.bonneReponse = bonneReponse;
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
        return this.intitule+listeReponse;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(intitule);
        dest.writeStringList(propositions);
        dest.writeInt(bonneReponse);
    }
}
