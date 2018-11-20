package fr.diginamic.formation.superquizz.dao;

import java.util.ArrayList;

import fr.diginamic.formation.superquizz.model.Question;
import fr.diginamic.formation.superquizz.model.TypeQuestion;

public class QuestionMemDao implements QuestionDao {
    public ArrayList<Question> listeQuestions;

    public QuestionMemDao(){
        listeQuestions = new ArrayList<Question>();
        this.init();
    }

    @Override
    public ArrayList<Question> findAll() {
        return listeQuestions;
    }

    @Override
    public void save(Question question) {
        this.listeQuestions.add(question);
    }

    @Override
    public void delete(Question question) {
        this.listeQuestions.remove(question);

    }

    public ArrayList<Question> getListeQuestions() {
        return listeQuestions;
    }

    public void setListeQuestions(ArrayList<Question> listeQuestions) {
        this.listeQuestions = listeQuestions;
    }

    public int getMaxPoint() {
        int maxPoint = 0;
        for(Question question : this.listeQuestions) {
            if(question.getType() == TypeQuestion.DOUBLE)
                maxPoint += 2;
            else
                maxPoint++;
        }
        return maxPoint;
    }

    public void init() {
        Question q1 = new Question("Quel est la capitale de la France ?");
        q1.addProposition("Marseille");
        q1.addProposition("Paris");
        q1.addProposition("Lille");
        q1.addProposition("Nantes");
        q1.setBonneReponse(2);
        q1.setType(TypeQuestion.SIMPLE);

        Question q2 = new Question("Quel est la capitale de l'Espagne ?");
        q2.addProposition("Madrid");
        q2.addProposition("Barcelone");
        q2.addProposition("Valence");
        q2.addProposition("Bilbao");
        q2.setBonneReponse(1);
        q2.setType(TypeQuestion.DOUBLE);

        Question q3 = new Question("Quel est la capitale du Japon ?");
        q3.addProposition("Kyoto");
        q3.addProposition("Nara");
        q3.addProposition("Osaka");
        q3.addProposition("Tokyo");
        q3.setBonneReponse(4);
        q3.setType(TypeQuestion.SIMPLE);

        this.listeQuestions.add(q1);
        this.listeQuestions.add(q2);
        this.listeQuestions.add(q3);
    }
}
