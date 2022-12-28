package control;

public interface ChefLoginController {

    public boolean attemptChefLogin(ChefBean credentials);
    public boolean registerChef(ChefBean chefInfo);

}
