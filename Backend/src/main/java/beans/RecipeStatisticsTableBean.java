package beans;

//bean che incapsula i dati relativi a istanze di ricette da visualizzare nella sezione di statistiche dello chef. In questo caso ci basta il nome e le visualizzazioni

public class RecipeStatisticsTableBean {

    private String recipeName;
    private int views;

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
