package beans;

/*
bean che incapsula informazioni di base da visualizzare nella tabella per le istanze di ricette. Si porta dietro anche l'username dello chef per associare a ogni ricetta nella tabella
un riferimento univoco allo chef stesso.
 */

public class RecipeBrowsingTableBean {

    private String name;
    private String chefCompleteName;
    private String chefUsername;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChefCompleteName() {
        return chefCompleteName;
    }

    public void setChefCompleteName(String chefCompleteName) {
        this.chefCompleteName = chefCompleteName;
    }

    public String getChefUsername() {
        return chefUsername;
    }

    public void setChefUsername(String chefUsername) {
        this.chefUsername = chefUsername;
    }

}
