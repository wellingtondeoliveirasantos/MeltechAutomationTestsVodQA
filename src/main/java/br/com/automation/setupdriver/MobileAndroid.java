package br.com.automation.setupdriver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;

/**
 * Configurações e manipulações relevantes ao Driver do iOS.
 */
public class MobileAndroid {

    private static AndroidDriver driver;
    public static Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
    public AndroidDriver getDriver() {
        return driver;
    }

    /**
     * Metodo responsável por configurar e iniciar do Driver Appium, comunicacao e configuracoes de sua capabilities.
     * @param noResetApp Recebe um valor booleano que indica se o app deve ter seu dados limpos ou não.
     */
    public void createDriver(boolean noResetApp) {

        try {
            UiAutomator2Options options = new UiAutomator2Options()
                    .setPlatformName(InitConfigs.PLATFORM_NAME)
                    .setAutomationName(InitConfigs.AUTOMATION_DRIVER_NAME)
                    .setUdid(InitConfigs.UDID_EMULATOR)
                    .setAppPackage(InitConfigs.APP_PACKAGE)
                    .setAppActivity(InitConfigs.APP_ACTIVITY)
                    .setEnsureWebviewsHavePages(true)
                    .setAutoGrantPermissions(true)
                    .setNoReset(noResetApp);

            if (Objects.equals(noResetApp, false)) {
                options.setApp(root + "/app/" + InitConfigs.ARTEFATO_NOME);
            }

            URI remoteURL = new URI(InitConfigs.APPIUM_SERVER);
            driver = new AndroidDriver(remoteURL.toURL(), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        } catch (Exception e) {
            System.out.println("Erro ao tentar criar o Driver. Algumas verificacoes podem ser necessarias.  " +
                    "\n     1. Verifique se o Appium esta instalado e inicializado corretamnete " +
                    "\n     2. Verifique se o URL appium esta correta via config APPIUM_SERVER" +
                    "\n     3. Verifique as configuracoes do artefato e se o mesmo foi alocado na pasta correta do projeto" +
                    "\nMENSSAGEM DE ERRO: " + e.getMessage());
        }

    }


    /**
     * Metodo responsável por fechar o Driver/Aplicativo. Utilizado em sua unica instancia.
     */
    public void killDriver() {
        if (driver != null) {
            System.out.println("Fechando o Driver . . .");
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("Erro ao tentar fechar o Driver/Aplicativo. " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }

    /**
     * Fechar o aplicativo e reabrir novamente.
     */
    public static void reabrirApp(){
        try {
            driver.terminateApp(InitConfigs.APP_PACKAGE);
            driver.activateApp(InitConfigs.APP_PACKAGE);
        } catch (Exception e) {
            System.out.println("Erro ao tentar reabrir o aplicativo. " + e.getMessage());
        }

    }

}
