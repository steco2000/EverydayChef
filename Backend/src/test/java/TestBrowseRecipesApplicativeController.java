//Stefano Colamartini
//Matricola: 0278902

import beans.*;
import control.*;
import exceptions.PersistentDataAccessException;
import exceptions.RecipeIngredientQuantityException;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestBrowseRecipesApplicativeController {

    /*
        IMPORTANTE: Per il corretto funzionamento del test potrebbe essere necessario modificare la working directory nella configurazione di lancio di junit, altrimenti il sistema
        calcolerà male i path assoluti dei file ".ser".

        In intellij:
        Run -> Edit Configurations -> JUnit -> *classe di test*.*metodo di test* -> Working directory (Selezionare la directory generale del progetto, ossia "EverydayChef") -> Apply
    */

    /*
    Il seguente metodo si occupa di testare l'operazione "retrieveSuggestedRecipe" del controller applicativo "BrowseRecipeApplicativeController". Questa operazione si occupa di
    recuperare le ricette consigliate all'utente in base al contenuto dell'inventario ingredienti. Prima di lanciare il metodo associato all'operazione, viene registrato un utente
    di test con un inventario contenete un ingrediente di prova. Oltre a questo viene registrato uno chef di test con almeno una ricetta che dovrebbe essere selezionata dal metodo
    in base al contenuto dell'inventario costruito. In questo modo si può avere almeno un valore da confrontare. Se ottenessimo un risultato vuoto, infatti, sarebbe impossibile
    stabilire se qualcosa non ha funzionato nell'operazione o se non è stata trovata nessuna ricetta compatibile con l'inventario in analisi.
     */
    @Test
    public void testRetrieveSuggestedRecipes() throws ParseException, PersistentDataAccessException {

        //registrazione utente e "simulazione" del login
        UserCredBean testUser = new UserCredBean();
        testUser.setUsername("testUser");
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("password");
        LoginController loginController = new LoginController();
        loginController.registerUser(testUser);
        loginController.attemptUserLogin(testUser);

        //creazione e salvataggio dell'inventario ingredienti
        InventoryApplicativeController inventoryController = new InventoryApplicativeController();
        IngredientBean ingredient = new IngredientBean();
        ingredient.setName("Farina");
        ingredient.setQuantity("2");
        ingredient.setMeasureUnit("Kg");
        ingredient.setExpirationDate("14/02/2023");
        ingredient.setNotes("*test*");
        inventoryController.addIngredient(ingredient);
        inventoryController.saveCurrentInventory();

        //registrazione chef di test (se già esiste "registerChef" non farà nulla restituendo false)
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
        loginController.registerChef(testChef);

        //creazione e salvataggio della ricetta
        RecipeBean testRecipe = new RecipeBean();
        testRecipe.setName("Pane");
        testRecipe.setDifficulty("Medium");
        testRecipe.setPreparationTime("1 H");
        testRecipe.setServings("8");
        testRecipe.setPreparationProcedure("*procedura pane test*");
        testRecipe.setChefUsername("testChef");

        //basta inserire un solo ingrediente che soddisfi la ricerca
        List<RecipeIngredientBean> ingredientList = new ArrayList<>();
        RecipeIngredientBean farina = new RecipeIngredientBean();
        farina.setName("Farina manitoba");
        try {
            farina.setQuantity("0.3",false);
            farina.setMeasureUnit("Kg");
        } catch (ParseException | RecipeIngredientQuantityException ignored) {
            assert(true);
        }

        ingredientList.add(farina);
        testRecipe.setIngredientList(ingredientList);

        RecipeSharingApplicativeController recipeSharingApplicativeController = new RecipeSharingApplicativeController();
        recipeSharingApplicativeController.shareRecipe(testRecipe);
        recipeSharingApplicativeController.saveChanges();

        //recupero ricette consigliate
        BrowseRecipeApplicativeController browseRecipeApplicativeController = new BrowseRecipeApplicativeController();
        List<RecipeBrowsingTableBean> suggestedRecipes = browseRecipeApplicativeController.retrieveSuggestedRecipe();

        //controllo del risultato, la variabile testPassed viene settata a false se esiste almeno una ricetta che non soddisfa i presupposti della ricerca
        RecipeInfoRetrievingApplicativeController recipeInfoController = new RecipeInfoRetrievingApplicativeController();
        String ingredientKeyWord = "Farina";
        boolean testPassed = true;

        for(RecipeBrowsingTableBean r: suggestedRecipes){
            RecipeBean recipe = recipeInfoController.retrieveRecipeInfo(r);

            //si controlla se almeno un ingrediente della ricetta contiene la keyword dell'ingrediente dell'inventario o viceversa. Se no la variabile testPassed è settata a false
            if(recipe.getIngredientList().stream().noneMatch(o -> o.getName().contains(ingredientKeyWord) || ingredientKeyWord.contains(o.getName()))){
                testPassed = false;
                break;
            }

        }

        assertTrue(testPassed);

    }

}
