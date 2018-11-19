package fr.diginamic.formation.superquizz;

import java.util.ArrayList;

public class Question {
    private String intitule;
    private ArrayList<String> propositions;
    private int bonneReponse;
    private TypeQuestion type;

    public Question(String intitule, int nbReponse) {
        this.intitule = intitule;
        this.propositions = new ArrayList<String>(nbReponse);
    }

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

    public int getNbPropositions() {
        return this.propositions.size();
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


}
