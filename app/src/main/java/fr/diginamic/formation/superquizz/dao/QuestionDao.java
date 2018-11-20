package fr.diginamic.formation.superquizz.dao;

import java.util.ArrayList;

import fr.diginamic.formation.superquizz.model.Question;

public interface QuestionDao {

    ArrayList<Question> findAll();
    void save(Question question);
    void delete(Question question);
}