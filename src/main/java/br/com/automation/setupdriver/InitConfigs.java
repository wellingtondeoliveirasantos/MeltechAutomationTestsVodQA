package br.com.automation.setupdriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Variáveis Globais de inicialização
 */
public class InitConfigs extends Initializer {

    private Properties properties;
    public static Path root = FileSystems.getDefault().getPath("").toAbsolutePath();

    public static String APPIUM_SERVER;
    public static String PLATFORM_NAME;
    public static String UDID_EMULATOR;
    public static String APP_PACKAGE;
    public static String APP_ACTIVITY;
    public static String ARTEFATO_NOME;
    public static String AUTOMATION_DRIVER_NAME;
    public static String APP_INIT;

    // Defina o appNome como "vodqa" fixo (ou pode parametrizar se quiser depois)
    private static final String appNome = "vodqa";

    public InitConfigs() throws IOException {
        properties = new Properties();
        InputStream input;
        try {
            input = new FileInputStream(root + "/src/main/resources/globalParameters.properties"); // execução em máquinas locais
            properties.load(input);
        } catch (FileNotFoundException e) {
            input = new FileInputStream("src/main/resources/globalParameters.properties"); // path local para execução via CI/CD
            properties.load(input);
        } catch (Exception e) {
            System.out.println("Erro ao tentar carregar o arquivo de propriedades. " + e.getMessage());
        }

        APPIUM_SERVER = properties.getProperty("AppiumServer");
        PLATFORM_NAME = properties.getProperty("PlatformName");
        UDID_EMULATOR = properties.getProperty("AndroidUDID");
        AUTOMATION_DRIVER_NAME = properties.getProperty("AutomationDriver");

        // Apenas VodQA agora
        if ("vodqa".equalsIgnoreCase(appNome)) {
            APP_INIT = properties.getProperty("VodQAInit");
            APP_PACKAGE = properties.getProperty("VodQAAppPackage");
            APP_ACTIVITY = properties.getProperty("VodQAAppActivity");
            ARTEFATO_NOME = properties.getProperty("VodQAArtefatoNome");
        }
    }
}
