package fr.diginamic.formation.superquizz;

import java.util.ArrayList;

public interface QuestionDao {

    ArrayList<Question> findAll();
    void save(Question question);
    void delete(Question question);
}