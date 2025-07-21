package br.com.automation.utils;

import br.com.automation.setupdriver.InitConfigs;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Classe responsável por qualquer configuracoes pertinente ao video de execução
 * do respetivo teste
 */
public class VideoResult {

    /**
     * Salva o video da execucao doteste
     * @param testName Nome do teste
     * @param driver Driver tipo IOS
     */
    public void saveScreenRecording(String testName, AndroidDriver driver) {
        String videoRecording = driver.stopRecordingScreen();

        try {
            byte[] decode = Base64.getDecoder().decode(videoRecording);
            String path = "output/" + InitConfigs.APP_INIT + "/video/";

            File theDir = new File(path);
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            String filePath = path + testName + ".mp4";

            FileOutputStream file = new FileOutputStream(filePath,true);
            file.write(decode);
            file.close();

        } catch (IOException e) {
            System.out.printf("Erro ao salvar o video do teste: " + testName + " - " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
