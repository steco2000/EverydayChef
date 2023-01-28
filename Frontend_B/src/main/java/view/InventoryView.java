package view;

import beans.IngredientBean;
import beans.InventoryTableDataBean;
import code_reuse.InputReusableUtilities;
import control.InventoryController;
import factories.InventoryControllerFactory;

import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class InventoryView {

    private Scanner sc;
    private InventoryTableDataBean dataBean;
    private InventoryController applController;
    private List<IngredientBean> ingredientList;

    public InventoryView(){
        this.sc = new Scanner(System.in);
        InventoryControllerFactory controllerFactory = new InventoryControllerFactory();
        applController = controllerFactory.createInventoryController();
        dataBean = InventoryTableDataBean.getSingletonInstance();
    }

    public void display(){
        while(true) {
            System.out.println();
            System.out.println("Ingredients Inventory");
            System.out.println();

            this.displayInventoryTable();

            System.out.println();
            System.out.println("Press:");
            System.out.println("0) To go back home");
            System.out.println("1) To add an ingredient");
            System.out.println("2) To update an ingredient");
            System.out.println("3) To remove an ingredient");
            System.out.println("4) To save changes");
            int answer = InputReusableUtilities.getAnswer(this.sc, 0, 4);

            switch (answer){
                case -1 -> {
                    assert(true); //errore nella risposta non faccio nulla
                }
                case 0 -> {
                    applController.saveCurrentInventory();
                    UserHomeView userHomeView = new UserHomeView();
                    userHomeView.display();
                }
                case 1 -> {
                    System.out.println();
                    System.out.println("Ingredient adding");
                    this.manageIngredientAdd();
                }
                case 2 -> this.manageIngredientUpdating();
                case 3 -> this.manageIngredientRemoving();
                default -> {
                    applController.saveCurrentInventory();
                    System.out.println("Save completed, press enter to continue");
                    this.sc.nextLine();
                }
            }
        }
    }

    private void manageIngredientAdd() {
        IngredientBean newIngredient = new IngredientBean();
        boolean dataAcquired = false;
        while(true){
            if(dataAcquired) break;
            try {
                System.out.print("Name: ");
                newIngredient.setName(sc.nextLine());
                System.out.print("Quantity (only value, without measure unit): ");
                newIngredient.setQuantity(sc.nextLine());
                newIngredient.setMeasureUnit(InputReusableUtilities.measureUnitInput(this.sc));
                System.out.print("Expiration date: ");
                newIngredient.setExpirationDate(this.sc.nextLine());
                System.out.println("Notes (do not press enter to start a new line!): ");
                newIngredient.setNotes(sc.nextLine());
                dataAcquired = true;
            } catch (IllegalArgumentException | ParseException e) {
                System.out.println("Invalid value, press enter to continue");
                this.sc.nextLine();
            }
        }
        System.out.println("Are you sure to proceed? (y/n)");
        if(InputReusableUtilities.yes(this.sc)) {
            if (!applController.addIngredient(newIngredient)) {
                System.out.println("Ingredient already present in the inventory, please update it instead of adding a new one. Press enter to continue");
                this.sc.nextLine();
            }
            this.display();
        }
    }

    private void manageIngredientUpdating() {
        System.out.println();
        System.out.print("Digit the index of the ingredient you want to update: ");
        int answer = InputReusableUtilities.getAnswer(this.sc, 1, this.ingredientList.size());
        if (answer == -1){
            return;
        }

        IngredientBean toUpdate = this.ingredientList.get(answer - 1);
        IngredientBean updates = new IngredientBean();
        updates.setName(toUpdate.getName());
        try {
            updates.setName(toUpdate.getName());
            updates.setQuantity(String.valueOf(toUpdate.getQuantity()));
            updates.setExpirationDate(toUpdate.getStringExpDate());
            updates.setMeasureUnit(toUpdate.getMeasureUnit());
            updates.setNotes(toUpdate.getNotes());
        } catch (ParseException e) {
            assert(true); //eccezione ignorata, dati da memoria già validati
        }

        while(true) {
            System.out.println();
            System.out.println("You selected:");
            System.out.println(updates.getName()+", "+updates.getQuantity()+" "+updates.getMeasureUnit());
            System.out.println("Expiration date: "+updates.getStringExpDate());
            System.out.println("Notes:");
            System.out.println(updates.getNotes());
            System.out.println();
            System.out.println("Press:");
            System.out.println("0) To cancel");
            System.out.println("1) To modify the quantity");
            System.out.println("2) To modify the expiration date");
            System.out.println("3) To modify the notes");
            System.out.println("4) To confirm changes");
            int answer2 = InputReusableUtilities.getAnswer(this.sc,0,4);

            switch (answer2){
                case -1 -> {
                    assert(true); //errore nella risposta, non faccio nulla
                }
                case 0 -> {
                    return;
                }
                case 1 -> {
                    System.out.println();
                    System.out.println("Digit the new quantity below (only value, without measure unit):");
                    try {
                        updates.setQuantity(this.sc.nextLine());
                    } catch (ParseException e) {
                        System.out.println();
                        System.out.println("Invalid quantity, press enter to continue");
                        this.sc.nextLine();
                    }
                    updates.setMeasureUnit(InputReusableUtilities.measureUnitInput(this.sc));
                }
                case 2 -> {
                    System.out.println();
                    System.out.println("Digit the new expiration date below");
                    try {
                        updates.setExpirationDate(this.sc.nextLine());
                    } catch (ParseException e) {
                        System.out.println();
                        System.out.println("Invalid date, press enter to continue");
                        this.sc.nextLine();
                    }
                }
                case 3 -> {
                    System.out.println();
                    System.out.println("Digit the new notes below (do not press enter to start a new line!)");
                    updates.setNotes(this.sc.nextLine());
                }
                default -> {
                    this.applController.updateIngredient(toUpdate.getName(),updates);
                    return;
                }
            }
        }
    }

    private void manageIngredientRemoving() {
        while(true) {
            System.out.println();
            System.out.print("Digit the index of the ingredient you want to remove: ");
            int answer = InputReusableUtilities.getAnswer(this.sc, 1, this.ingredientList.size());
            if (answer == -1) return;
            IngredientBean toRemove = this.ingredientList.get(answer-1);
            applController.removeIngredient(toRemove);
            System.out.println("Ingredient successfully removed, press enter to continue");
            this.sc.nextLine();
            this.display();
        }
    }

    private void displayInventoryTable() {
        try {
            this.ingredientList = dataBean.getTableData();
        } catch (ParseException e) {
            System.out.println("Data corrupted, press enter to continue");
            this.sc.nextLine();
            return;
        }

        System.out.println("These are your ingredients, in order: Name, Quantity, Measure Unit, Expiration Date, Notes");
        int index = 1;

        for(IngredientBean i: this.ingredientList){
            System.out.println(index+") "+i.getName()+", "+i.getQuantity()+" "+i.getMeasureUnit()+", "+i.getStringExpDate()+", "+i.getNotes());
            index++;
        }

    }

}
