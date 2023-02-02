//Stefano Colamartini
//Matricola: 0278902

/*
N.B. : Per il corretto funzionamento del test potrebbe essere necessario modificare la working directory nella configurazione di lancio di junit, altrimenti il sistema calcolerà male
       i path relativi dei file ".ser".

       In intellij:
       Run -> Edit Configurations -> JUnit -> *classe di test*.*metodo di test* -> Working directory (Selezionare la directory generale del progetto, ossia "EverydayChef") -> Apply
*/

import beans.ChefBean;
import beans.RecipeBean;
import beans.RecipeBrowsingTableBean;
import beans.RecipeIngredientBean;
import control.LoginController;
import control.RecipeSearchingApplicativeController;
import control.RecipeSharingApplicativeController;
import exceptions.RecipeIngredientQuantityException;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestRecipeSearchingApplicativeController {

    /* Il seguente metodo si occupa di testare il corretto funzionamento l'operazione "retrieveSearchResult" del controller applicativo "RecipeSearchingApplicativeController",
        la quale si occupa di cercare ricette in memoria in base all'input dell'utente. Prima di lanciare il metodo, viene salvata una ricetta di test (con il relativo chef),
        per fare in modo che il risultato della ricerca contenga almeno un valore da confrontare. Infatti, nel caso in cui si riceva un risultato vuoto,
        non si può stabilire se il metodo abbia funzionato o se semplicemente non sono presenti ricette corrispondenti alla ricerca data in input.
    */
    @Test
    public void testBasicRecipeSearch(){

        //registrazione chef di test per la ricetta
        ChefBean testChef = new ChefBean();
        testChef.setName("testName");
        testChef.setSurname("testSurname");
        testChef.setEmail("testEmail@gmail.com");
        try {
            testChef.setBirthDate("01/01/2000");
        } catch (ParseException ignored) {
            assert(true);
        }
        testChef.setUsername("testChef");
        testChef.setPassword("password");
        testChef.setInfo("testInfo");
        LoginController loginController = new LoginController();
        loginController.registerChef(testChef);

        //creazione e salvataggio della ricetta
        RecipeBean testRecipe = new RecipeBean();
        testRecipe.setName("Pasta al ragù");
        testRecipe.setDifficulty("Easy");
        testRecipe.setPreparationTime("15 min");
        testRecipe.setServings("4");
        testRecipe.setPreparationProcedure("*procedura pasta al ragù*");
        testRecipe.setChefUsername("testChef");

        //Essendo un test non legato alla lista degli ingredienti possiamo evitare di salvarli tutti, uno può bastare
        List<RecipeIngredientBean> ingredientList = new ArrayList<>();
        RecipeIngredientBean pasta = new RecipeIngredientBean();
        pasta.setName("Pasta");
        try {
            pasta.setQuantity("0.32",false);
            pasta.setMeasureUnit("Kg");
        } catch (ParseException | RecipeIngredientQuantityException ignored) {
            assert(true);
        }

        testRecipe.setIngredientList(ingredientList);

        RecipeSharingApplicativeController recipeSharingApplicativeController = new RecipeSharingApplicativeController();
        recipeSharingApplicativeController.shareRecipe(testRecipe);
        recipeSharingApplicativeController.saveChanges();

        //esecuzione del test, la variabile "testPassed" viene settata a false se viene trovata una ricetta che non soddisfa i presupposti della ricerca
        boolean testPassed = true;
        String searchInput = "Ragù";

        List<RecipeBrowsingTableBean> searchResult = (new RecipeSearchingApplicativeController()).retrieveSearchResult(searchInput);
        for(RecipeBrowsingTableBean r: searchResult){
            System.out.println(r.getName());
            if(!(r.getName().contains(searchInput) || searchInput.contains(r.getName()))){
                testPassed = false;
                break;
            }
        }

        assertTrue(testPassed);

    }

}