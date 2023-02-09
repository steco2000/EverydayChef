package exceptions;

//eccezione lanciata nel caso in cui si tenti di salvare una ricetta con un nome già usato

public class ExistingRecipeException extends Exception{

    public ExistingRecipeException(){
        super();
    }

}
