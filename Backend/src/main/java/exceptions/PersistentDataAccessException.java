package exceptions;

/*

Eccezione pensata per eseguire il chaining di IOException e SQLException, lanciate in caso di problemi di accesso ai dati in persistenza. Questi problemi interni sono propagati
agli strati esterni del sistema attraverso il wrapping in questa nuova eccezione comprensibile all'utente.

*/

public class PersistentDataAccessException extends Throwable{

    public PersistentDataAccessException(Throwable e){
        super("A technical problem has occurred, please try again later.", e);
    }

}
