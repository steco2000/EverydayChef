package control;

import beans.ChefBean;

//Interfaccia del controller applicativo "LoginController" esposta alla UI dell'utente chef

public interface ChefLoginController {

    boolean attemptChefLogin(ChefBean credentials);
    boolean registerChef(ChefBean chefInfo);

}
