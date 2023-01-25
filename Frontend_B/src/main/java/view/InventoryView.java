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
            System.out.println("2) To remove an ingredient");
            System.out.println("3) To save changes");
            int answer = InputReusableUtilities.getAnswer(this.sc, 0, 3);

            switch (answer){
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
                case 2 -> this.manageIngredientRemoving();
                default -> {
                    applController.saveCurrentInventory();
                    System.out.println("Save completed, press enter to continue");
                    this.sc.nextLine();
                }
            }
        }
    }

    private String measureUnitInput(){
        while(true) {
            System.out.println("Measure Unit - press:");
            System.out.println("1) For Kg");
            System.out.println("2) For L");
            System.out.println("3) For not specified");
            int answer = InputReusableUtilities.getAnswer(this.sc, 1, 3);
            switch (answer) {
                case -1:
                    continue;
                case 1:
                    return "Kg";
                case 2:
                    return "L";
                default:
                    return "";
            }
        }
    }

    private void manageIngredientAdd() {
        IngredientBean newIngredient = new IngredientBean();
        while(true){
            try {
                System.out.print("Name: ");
                newIngredient.setName(sc.nextLine());
                System.out.print("Quantity: ");
                newIngredient.setQuantity(sc.nextLine());
                newIngredient.setMeasureUnit(this.measureUnitInput());
                System.out.print("Expiration date: ");
                newIngredient.setExpirationDate(this.sc.nextLine());
                System.out.println("Notes (do not press enter to start a new line!): ");
                newIngredient.setNotes(sc.nextLine());
            } catch (IllegalArgumentException | ParseException e) {
                System.out.println("Invalid value, press enter to continue");
                this.sc.nextLine();
                continue;
            }
            break;
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
