package control;

import beans.ChefBean;

public interface ChefLoginController {

    public boolean attemptChefLogin(ChefBean credentials);
    public boolean registerChef(ChefBean chefInfo);

}
