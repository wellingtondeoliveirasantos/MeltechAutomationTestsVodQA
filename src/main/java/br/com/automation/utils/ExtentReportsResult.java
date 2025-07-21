package br.com.automation.utils;

import br.com.automation.setupdriver.InitConfigs;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Classe responsável por gerar e de configuracão do relatório de teste apresentado
 */
public class ExtentReportsResult implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    protected static ExtentReports extent = new ExtentReports();
    public static ExtentTest test;


    /**
     * Antes do teste, inicializa  e configura o relatório
     * @param context Informa/Get o contexto
     * @throws Exception
     */
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        try {
            LocalDateTime data = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = data.format(formatter);
            ExtentSparkReporter sparkReport = new ExtentSparkReporter("output/report_android_" + InitConfigs.APP_INIT + "_" + formattedDate + ".html");
            extent.attachReporter(sparkReport);
            test = extent.createTest("[" +context.getTestClass().get().getSimpleName().toUpperCase().replace("_", " ") + "]" + " - " + context.getDisplayName())
                    .assignCategory(String.valueOf(context.getTags()));
            sparkReport.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a");
            sparkReport.config().setTimelineEnabled(false);
            sparkReport.config().setTheme(Theme.STANDARD);
        } catch (Exception e) {
            System.out.println("Erro ao inicializar e configurar o relatório de teste: " + e.getMessage());
        }
    }

    /**
     * Após o teste, registra o resultado final
     * @param context Informa/Get o contexto
     * @throws Exception
     */
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        try{
            if (context.getExecutionException().isEmpty()) {
                test.log(Status.PASS, MarkupHelper.createLabel("SUCESSO ao executar jornada", ExtentColor.GREEN));
            } else {
                test.log(Status.FAIL, MarkupHelper.createLabel("FALHA ao executar jornada", ExtentColor.RED));
            }
        } catch (Exception e){
            System.out.println("Erro ao registrar o resultado final do relatório de teste: " + e.getMessage());
            test.log(Status.INFO, MarkupHelper.createLabel("Erro ao processar após execução do teste", ExtentColor.RED));
        } finally {
            extent.flush();
        }

    }


    /* REGISTROS DE LOG NO RELATORIO DE TESTES */
    /**
     * Efetua e captura o Print da tela apresentada
     * @param driver
     * @return Imagem capturada
     * @throws InterruptedException
     */
    public Media takeScreenshot(AndroidDriver driver) throws Exception {
        Thread.sleep(3000);
        try {
            String image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            return MediaEntityBuilder.createScreenCaptureFromBase64String(image).build();
        } catch (Exception e) {
            System.out.println("Erro ao capturar imagem: " + e.getMessage());
        }

        return null;
    }

    /**
     * Registra um log/evento que informa o step obteve sucesso
     * @param texto Texto deseja para ser apresentado no relatório
     */
    public void registroLogPassou(String texto) {
        ExtentReportsResult.test.log(Status.PASS, texto);
    }

    /**
     * Registra algo evento do tipo informação no relatorio
     * @param texto Texto deseja para ser apresentado no relatório
     */
    public void registroLogInfo(String texto) {
        ExtentReportsResult.test.log(Status.INFO, texto);
    }

    /**
     * Registra um log/evento de alerta
     * @param texto Texto deseja para ser apresentado no relatório
     */
    public void registroLogAtencao(String texto) {
        ExtentReportsResult.test.log(Status.WARNING, texto);
    }

    /**
     * Registra que o step foi pulado
     * @param texto Texto deseja para ser apresentado no relatório
     */
    public void registroLogPulou(String texto) {
        ExtentReportsResult.test.log(Status.SKIP, texto);
    }

    /**
     * Registra um log/evento que informa o step obteve falha
     * @param texto Texto deseja para ser apresentado no relatório
     */
    public void registroLogFalha(String texto, AndroidDriver driver) throws Exception {
        ExtentReportsResult.test.log(Status.FAIL,texto);
        ExtentReportsResult.test.log(Status.INFO, "Print da tela, Local Falha: ", takeScreenshot(driver));
    }

}
