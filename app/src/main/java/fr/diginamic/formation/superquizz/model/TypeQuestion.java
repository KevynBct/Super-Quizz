package fr.diginamic.formation.superquizz.model;

public enum TypeQuestion {
    SIMPLE("Simple"),
    DOUBLE("Double");

    private String type;

    private TypeQuestion(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}