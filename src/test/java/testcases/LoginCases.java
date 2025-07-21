package testcases;

import br.com.automation.features.login.LoginActions;

import br.com.automation.setupdriver.Initializer;

public class LoginCases extends Initializer {

    LoginActions login = new LoginActions();

    public void validaSucessoAcessoApp() throws Exception {
         login.clicarBtnLogin();
    }


}
