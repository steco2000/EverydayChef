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

public class TestRecipeInfoRetrievingApplicativeController {

    /*
        IMPORTANTE: Per il corretto funzionamento del test potrebbe essere necessario modificare la working directory nella configurazione di lancio di junit, altrimenti il sistema
        calcolerà male i path assoluti dei file ".ser".

        In intellij:
        Run -> Edit Configurations -> JUnit -> *classe di test*.*metodo di test* -> Working directory (Selezionare la directory generale del progetto, ossia "EverydayChef") -> Apply
    */

    /*
    Il seguente metodo si occupa di testare il funzionamento dell'operazione "RetrieveMissingIngredients" del controller applicativo "RecipeInfoRetrievingApplicativeController".
    Il metodo testato si occupa di recuperare le informazioni sugli ingredienti mancanti nell'inventario dell'utente per la realizzazione di una ricetta, quando selezionata.
    Nel test viene creato un utente di prova, e inserito un ingrediente nell'inventario, da confrontare con quello di una ricetta. Viene poi registrato uno chef con una ricetta
    di prova, la quale richiede lo stesso ingrediente dell'inventario ma con una quantità maggiorata. In questo modo il test può capire se la quantità mancante calcolata è conforme
    con quanto atteso. Dopodiché, viene simulata l'interazione dell'utente con il sistema. La ricetta verrà selezionata tra le consigliate dato che contiene l'ingrediente di prova,
    ma, ovviamente, questo apparirà tra gli ingredienti mancanti insieme agli altri, proprio perché è stato salvato con una quantità minore di quella richiesta dalla ricetta.
     */

    @Test
    public void testRetrieveMissingIngredients() throws ParseException, PersistentDataAccessException {

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
        IngredientBean inventoryIngredient = new IngredientBean();
        inventoryIngredient.setName("Farina");
        inventoryIngredient.setQuantity("0.2");
        inventoryIngredient.setMeasureUnit("Kg");
        inventoryIngredient.setExpirationDate("14/02/2023");
        inventoryIngredient.setNotes("*test*");
        inventoryController.addIngredient(inventoryIngredient);
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
        testRecipe.setName("Pasta all'uovo");
        testRecipe.setDifficulty("Easy");
        testRecipe.setPreparationTime("15 min");
        testRecipe.setServings("4");
        testRecipe.setPreparationProcedure("*procedura pasta uovo test*");
        testRecipe.setChefUsername("testChef");

        //la ricetta richiederà una quantità di farina superiore a quella nell'inventario, più delle uova, che non sono presenti.
        //Il metodo dovrebbe quindi trovare degli ingredienti mancanti
        List<RecipeIngredientBean> ingredientList = new ArrayList<>();
        RecipeIngredientBean farina = new RecipeIngredientBean();
        farina.setName("Farina");
        try {
            farina.setQuantity("0.4",false);
            farina.setMeasureUnit("Kg");
        } catch (ParseException | RecipeIngredientQuantityException ignored) {
            assert(true);
        }
        RecipeIngredientBean uovo = new RecipeIngredientBean();
        uovo.setName("Uovo");
        try {
            uovo.setQuantity("4",false);
            uovo.setMeasureUnit("");
        } catch (ParseException | RecipeIngredientQuantityException ignored) {
            assert(true);
        }

        ingredientList.add(farina);
        ingredientList.add(uovo);
        testRecipe.setIngredientList(ingredientList);

        RecipeSharingApplicativeController recipeSharingApplicativeController = new RecipeSharingApplicativeController();
        recipeSharingApplicativeController.shareRecipe(testRecipe);
        recipeSharingApplicativeController.saveChanges();

        //per lanciare il metodo abbiamo bisogno di un bean contenente i dati che il metodo stesso si aspetta, cioè quelli che provengono dalla tabella
        RecipeBrowsingTableBean recipeTableBean = new RecipeBrowsingTableBean();
        recipeTableBean.setName(testRecipe.getName());
        recipeTableBean.setChefUsername(testChef.getUsername());
        recipeTableBean.setChefCompleteName(testChef.getName()+" "+testChef.getSurname());

        //recuperiamo la lista degli ingredienti dell'inventario per il successivo confronto con i risultati
        InventoryTableDataBean inventoryDataBean = InventoryTableDataBean.getSingletonInstance();
        List<IngredientBean> currentInventoryList = inventoryDataBean.getTableData();

        /*
        Nelle seguenti righe di codice si simula l'interazione dell'utente con il sistema, per fare in modo che tutte gli oggetti che concorrono alla corretta esecuzione del metodo
        testato siano correttamente instanziati.
        */
        BrowseRecipeApplicativeController browseController = new BrowseRecipeApplicativeController();
        browseController.retrieveSuggestedRecipe();
        RecipeInfoRetrievingApplicativeController recipeInfoController = new RecipeInfoRetrievingApplicativeController();
        RecipeBean recipeSelected = recipeInfoController.retrieveRecipeInfo(recipeTableBean);

        //esecuzione del metodo
        List<RecipeIngredientBean> missingIngredients = recipeInfoController.retrieveMissingIngredients(recipeTableBean);

        //esecuzione del test, la variabile "testPassed" viene settata a false se viene riscontrato un problema nei risultati della ricerca
        boolean testPassed = true;
        if(missingIngredients.isEmpty()) testPassed = false; //per costruzione del test ci aspettiamo che la lista degli ingredienti mancanti non sia vuota

        /*
          Per ogni ingrediente mancante trovato dal metodo, confrontiamo la quantità mancante calcolata con quella disponibile nell'inventario, se presente.
          Se questa quantità fosse maggiore a quella richiesta dalla ricetta il metodo avrebbe restituito risultati errati, dato che avrebbe inserito nella lista degli ingredienti
          mancanti un ingrediente che invece è presente in quantità sufficiente nell'inventario.
        */

        for(RecipeIngredientBean missingIngr: missingIngredients){
            //si selezionano le informazioni sull'ingrediente mancante dalla lista degli ingredienti della ricetta
            RecipeIngredientBean missingRecipeIngredientInfo = recipeSelected.getIngredientList().stream().filter(o -> o.getName().equals(missingIngr.getName())).findAny().orElse(null);

            //se negli ingredienti mancanti c'è un ingrediente non presente nella lista della ricetta il metodo non ha funzionato
            if(missingRecipeIngredientInfo == null) {
                System.err.println("Error: inconsistent data returned from tested method");
                testPassed = false;
                break;
            }
            //altrimenti viene confrontata la quantità presente nell'inventario con quella richiesta dalla ricetta
            else {
                for (IngredientBean inventoryIngr : currentInventoryList) {
                    if (inventoryIngr.getName().equals(missingRecipeIngredientInfo.getName())) {
                        if (inventoryIngr.getQuantity() > missingRecipeIngredientInfo.getQuantity()) {
                            testPassed = false; //se si verifica la situazione spiegata precedentemente il test è fallito
                            break;
                        }
                    }
                }
            }
        }

        assertTrue(testPassed);
    }

}
