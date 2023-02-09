package exceptions;

//eccezione lanciata quando si tenta di salvare un ingrediente della ricetta con quantità nulla

public class RecipeIngredientQuantityException extends Exception{

    public RecipeIngredientQuantityException(){ super(); }

}
