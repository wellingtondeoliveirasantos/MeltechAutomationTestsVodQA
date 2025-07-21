package br.com.automation.features.login;

import br.com.automation.utils.ActionsBase;

public class LoginActions extends ActionsBase {

    LoginMap loginMap = new LoginMap();
    LoginTexts loginTexts = new LoginTexts();

    public void clicarBtnLogin() throws Exception {
        clicarElemento(loginMap.btnLogin, "LOG IN");
        Thread.sleep(10000); // Sleep pertinente a esteira on Cloud, loading gigante
    }

}
