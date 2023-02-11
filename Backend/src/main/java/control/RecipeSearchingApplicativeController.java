package control;

import beans.RecipeBrowsingTableBean;
import dao.RecipesBrowsingDAO;
import model.RecipeBase;

import java.util.ArrayList;
import java.util.List;

//controller applicativo per la gestione delle ricerche globali di ricette da parte dell'utente

public class RecipeSearchingApplicativeController implements RecipeSearchingController{

    //il testo della ricerca digitata dall'utente viene capitalizzato in ogni parola, per evitare ambiguità nella ricerca
    private String toTitleCase(String givenSearch) {
        String[] arr = givenSearch.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String s : arr) {
            sb.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    /*
    Metodo che effettua la ricerca, incapsula i dati nei bean e restituisce il tutto alla UI. La ricerca consiste in una semplice query al DAO di navigazione
     */
    @Override
    public List<RecipeBrowsingTableBean> retrieveSearchResult(String givenSearch) {
        String search = toTitleCase(givenSearch);
        RecipesBrowsingDAO browsingDAO = new RecipesBrowsingDAO();
        List<RecipeBase> searchResult = browsingDAO.getSearchResult(search);
        List<RecipeBrowsingTableBean> resultList = new ArrayList<>();
        if (searchResult.isEmpty()) return resultList;
        for (RecipeBase r : searchResult) {
            RecipeBrowsingTableBean recipeBean = new RecipeBrowsingTableBean();
            recipeBean.setName(r.getName());
            recipeBean.setChefCompleteName(r.getChef().getName() + " " + r.getChef().getSurname());
            recipeBean.setChefUsername(r.getChef().getUsername());
            resultList.add(recipeBean);
        }
        return resultList;
    }

}
