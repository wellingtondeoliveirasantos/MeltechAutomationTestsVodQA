package br.com.automation.setupdriver;

import br.com.automation.utils.ExtentReportsResult;
import br.com.automation.utils.VideoResult;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Classe Launcher responsável por iniciar, executar e encerrar o driver
 */
@ExtendWith(ExtentReportsResult.class)
public class Initializer extends MobileAndroid {

    public static String appNome;
    public static boolean fecharAplicativo;
    public static AndroidDriver driver;
    public static VideoResult videoResult = new VideoResult();

    @BeforeEach
    public void setup() throws Exception {
        new InitConfigs();
        createDriver(fecharAplicativo);
        driver = getDriver();
        if (fecharAplicativo){
            MobileAndroid.reabrirApp();
            Thread.sleep(3000);
        }
        driver.startRecordingScreen();
    }

    @AfterEach()
    public void teardown(TestInfo result) throws InterruptedException {
        videoResult.saveScreenRecording(result.getDisplayName(), getDriver());
        killDriver();
        Thread.sleep(2000);
    }
}
