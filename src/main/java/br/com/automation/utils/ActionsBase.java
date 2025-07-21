package br.com.automation.utils;

import br.com.automation.setupdriver.Initializer;
import com.aventstack.extentreports.Status;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Classe responsavel por encapsular as ações do selenium ou outras necessidades de acoes para acoes do usuario
 */
public class ActionsBase extends Initializer {

    private LocalCommandLine runCommand;
    ExtentReportsResult extent = new ExtentReportsResult();
    public static Path root = FileSystems.getDefault().getPath("").toAbsolutePath();

    /**
     * Aguarda o elemento estar visível
     * @param element Elemento que deve estar visível
     * @return Elemento
     */
    public WebElement aguardaElementoEstarVisivel(By element) throws Exception {
        try {
            return driver.findElement(element);
        } catch (NoSuchElementException e) {
            extent.registroLogAtencao("Elemento não encontrado: " + element.toString() + " Obs. Verifique se não foi alterado o ID ou se nao ocorreu um possivel Flak");
            throw e;
        } catch (WebDriverException e) {
            extent.registroLogFalha("Erro ao esperar a visibilidade do elemento: " + element.toString() + "\n" + e.getMessage(), driver);
            throw e;
        }
    }

    /**
     * Retorna o texto do elemento visivel na tela/DOM
     * @param element Elemento que deve estar visível
     * @return retorna o texto
     */
    public String retornarTexto(By element) throws Exception {
        WebElement webElement = aguardaElementoEstarVisivel(element);
        try {
            return webElement.getText().trim();
        } catch (StaleElementReferenceException e) {
            extent.registroLogFalha("Erro ao obter o texto do elemento " + element.toString() + ": " + e.getMessage(), driver);
            throw e;
        }
    }

    /**
     * Ação de clicar no elemento
     * @param element Elemento que deve estar visível
     * @param nomeElemento Nome do elemento
     */
    public void clicarElemento(By element, String nomeElemento) throws Exception {
        WebElement mobileElement = aguardaElementoEstarVisivel(element);
        try {
            mobileElement.click();
            extent.registroLogPassou("Clique no elemento \"" + nomeElemento + "\"");
        } catch (WebDriverException e) {
            extent.registroLogFalha("Erro ao clicar no elemento \"" + nomeElemento + "\": " + e.getMessage(), driver);
            throw e;
        }
    }


    /**
     * Digita o valor no elemento selecionado
     * @param element Elemento que deve estar visível
     * @param nomeElemento Nome do elemento
     * @param valor Valor a ser digitado
     */
    public void digitarElemento(By element, String nomeElemento, String valor) throws Exception {
        WebElement mobileElement = aguardaElementoEstarVisivel(element);
        try {
            mobileElement.sendKeys(valor);
            getDriver().hideKeyboard();
            extent.registroLogPassou("Digitou " +valor + " no elemento \"" + nomeElemento + "\"");
        } catch (WebDriverException e) {
            extent.registroLogFalha("Erro ao digitar valor no elemento " + element.toString() + ": " + e.getMessage(), driver);
            throw e;
        }
    }

    /**
     * Esvazia o elemento selecionado
     * @param element Elemento que deve estar visível
     */
    public void limparCampoElemento(By element) throws Exception {
        WebElement mobileElement = aguardaElementoEstarVisivel(element);
        try {
            mobileElement.clear();
        } catch (org.openqa.selenium.WebDriverException e) {
            extent.registroLogFalha("Erro ao limpar o elemento " + element.toString() + ": " + e.getMessage(), driver);
            throw e;
        }
    }

    /**
     * Verifica se o elemento esta com status habilitado
     * @param element Elemento que deve estar visível
     * @param nomeElemento Nome do elemento
     * @return Retorna se o elemento esta habilitado (true ou false)
     */
    public boolean estaHabilitado(By element, String nomeElemento) throws Exception {
        WebElement mobileElement = aguardaElementoEstarVisivel(element);
        if (mobileElement.isEnabled()) {
            extent.registroLogPassou("Elemento \"" + nomeElemento + "\" está habilitado");
        } else {
            extent.registroLogPassou("Elemento \"" + nomeElemento + "\" está desabilitado");
        }
        return mobileElement.isEnabled();
    }

    /**
     * Verifica se o elemento esta com status selecionado
     * @param element Elemento que deve estar visível
     * @param nomeElemento Nome do elemento
     * @return Retorna se o elemento está selecionado (true ou false)
     */
    public boolean estaSelecionado(By element, String nomeElemento) throws Exception {
        WebElement mobileElement = aguardaElementoEstarVisivel(element);
        try {
            if (mobileElement.isSelected()) {
                extent.registroLogPassou("Elemento \""+nomeElemento+"\" está selecionado");
            }else {
                extent.registroLogPassou("Elemento \""+nomeElemento+"\" não está selecionado");
            }
            return mobileElement.isSelected();

        } catch (WebDriverException e){
            extent.registroLogFalha("Erro ao verificar se o elemento " + nomeElemento + " está selecionado: " + e.getMessage(), driver);
            return false;
        }
    }

    /**
     * Verifica se o elemento esta sendo apresentado na tela e com com status apresentado
     * @param element Elemento que deve estar visível
     * @param nomeElemento Nome do elemento
     * @return Retorna se o elemento está apresentado (true ou false)
     */
    public boolean estaApresentado(By element, String nomeElemento) throws Exception {
        WebElement mobileElement = aguardaElementoEstarVisivel(element);
        try {
            if (mobileElement.isDisplayed()) {
                extent.registroLogPassou("Elemento \"" + nomeElemento + "\" está sendo exibido em tela");
            } else {
                extent.registroLogPassou("Elemento \""+nomeElemento+"\" não está exibido em tela");
            }
            return mobileElement.isDisplayed();

        } catch (WebDriverException e){
            extent.registroLogFalha("Erro ao verificar se o elemento " + nomeElemento + " está apresentado: " + e.getMessage(), driver);
            return false;
        }
    }

    /**
     * Varifica se o alerta esta sendo apresentada
     * @param elementFather Elemento pai que deve estar visível
     * @param elementSon Elemento filho que deve estar visível
     * @return Retorna se o elemento pai contem o elemento filho,se contiver retorna true,
     * valdiando que o alerta esteja sendo apresentado
     */
    public boolean alertaApresentado(By elementFather, By elementSon) throws Exception {
        WebElement mobileElement = aguardaElementoEstarVisivel(elementFather);
        try{
            return !mobileElement.findElements(elementSon).isEmpty();
        } catch (StaleElementReferenceException e){
            return true;
        }
    }

    /**
     * Compara o texto esperado com o texto obtido do elemento selecionado
     * @param textoEsperado Texto esperado, de acordo com protótipo do teste
     * @param element Elemento que deve estar visível
     */
    public void comparaTextosEsperadoElement(String textoEsperado, By element) throws Exception {
        String textoObtido = retornarTexto(element);
        try {
            if (!textoEsperado.equals(textoObtido)) {
                extent.registroLogAtencao("Erro na comparação entre o resultado esperado: \""+ textoEsperado +"\" com o resultado retornado \""+textoObtido+"\"");
            }else {
                assertEquals(textoEsperado, textoObtido);
                extent.registroLogPassou("Texto': \"" + textoObtido + "\" validado com sucesso");
            }
        } catch (WebDriverException e){
            extent.registroLogFalha("Erro ao comparar o texto " + textoEsperado + " está apresentado: " + e.getMessage(), driver);
        }
    }

    /**
     * Compara o texto esperado com o texto obtido do elemento selecionado e extraido em um var tipo String
     * Este method é utilizado quando precisamos montar um texto com muitos elementos parciais
     * @param textoEsperado Texto esperado, de acordo com protótipo do teste
     * @param textoObtido Texto obtido a partir do elemento
     */
    public void comparaTextosEsperadoFixos(String textoEsperado, String textoObtido) throws Exception {
        try {
            if (!textoEsperado.equals(textoObtido)) {
                extent.registroLogAtencao("Erro na comparação entre o resultado esperado: \""+ textoEsperado +"\" com o resultado retornado \""+textoObtido+"\"");
            }else {
                assertEquals(textoEsperado, textoObtido);
                extent.registroLogPassou("Texto': \"" + textoObtido + "\" validado com sucesso");
            }
        } catch (WebDriverException e){
            extent.registroLogFalha("Erro ao comparar o texto " + textoEsperado + " está apresentado: " + e.getMessage(), driver);
        }
    }

    /**
     * Fecha o app e reabre o mesmo
     */
    public void reabrirAppSemReset() {
        reabrirApp();
    }


    /**
     * Deleta o QrCode rederizado na camera do emulador, presente na pasta resources, local via variavel de ambiente EMULATOR_RESOURCES
     */
    public void deletarQrCode() {
        try {
            File pathEmulador = new File(System.getenv("EMULATOR_RESOURCES"));
            File[] fileQrCodeEmulatorResources = pathEmulador.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("QRCode");
                }
            });

            if (fileQrCodeEmulatorResources.length > 0) {
                fileQrCodeEmulatorResources[0].delete();
            }
        } catch (Exception e) {
            System.out.printf("Erro ao deletar o QrCode na pasta resources: " + e.getMessage());
        }
    }

    /**
     * Inserir o QrCode rederizado na camera do emulador, presente na pasta resources, local via variavel de ambiente EMULATOR_RESOURCES
     * QRcode da respectiva massa de teste
     * @param pathQrCode Local da pasta com QRcode da massa a ser substituida
     * @throws Exception
     */
    public void inserirQrCode(String pathQrCode) throws Exception {
        try {
            Path basedir = FileSystems.getDefault().getPath("").toAbsolutePath();
            File pathEmulador = new File(System.getenv("EMULATOR_RESOURCES").concat("/qrcode.png"));
            File qrCodeDir = FileSystems.getDefault().getPath(basedir + "/credencialParameters/" + pathQrCode + "/qrcode.png").toFile();
            Files.copy(qrCodeDir, pathEmulador);
        } catch (Exception e) {
            System.out.println("Erro ao inserir o QrCode na pasta resources: " + e.getMessage());
        }
    }

    /* COMANDOS NATIVOS */
    public static void voltarNativoAndroid() {
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
    }


    /* REGISTRO DE PRINT DA TELA NO RELATORIO DE TESTES */
    public void printTelaLogRelatorio() throws Exception {
        ExtentReportsResult.test.log(Status.INFO, "Apresentacao/print da tela: ", extent.takeScreenshot(driver));
    }


    /* COMANDOS PERTINENTE AO SCROLL PASSAGENS DE PAGINAS, SUBIDA DE PAGINAS E DESCIDA DE PAGINAS */
    public enum Direcoes{
        CIMA, BAIXO, ESQUERDA, DIREITA,
    }

    /**
     * Scroll na tela de acordo com a direção passada e com a porcentagem de rolagem
     * @param direcaoScroll Direção da rolagem, pode ser CIMA, BAIXO, ESQUERDA ou DIREITA
     * @param scrollRatic Distância da rolagem entre 0.0 e 1.0
     */
    public void pageScrolling(Direcoes direcaoScroll, double scrollRatic) {
        Duration SCROLL_DURATION = Duration.ofMillis(300);
        if (scrollRatic < 0 || scrollRatic > 1){
            throw new Error("Distancia do Scroll precisa estar entre 0 e 1");
        }

        Dimension sizeEmulator = driver.manage().window().getSize();

        Point midPoint = new Point((int)(sizeEmulator.width * 0.5), (int)(sizeEmulator.height * 0.5));

        int bottom = midPoint.y + (int)(midPoint.y * scrollRatic); // 50 + 25
        int top = midPoint.y - (int)(midPoint.y * scrollRatic); // 50 - 25
        int left = midPoint.x - (int)(midPoint.x * scrollRatic); // 25 - 12.5
        int right = midPoint.x + (int)(midPoint.x * scrollRatic); // 25 + 12.5

        switch (direcaoScroll) {
            case CIMA:
                swipe(new Point(midPoint.x, bottom), new Point(midPoint.x, top), SCROLL_DURATION);
                break;
            case BAIXO:
                swipe(new Point(midPoint.x, top), new Point(midPoint.x, bottom), SCROLL_DURATION);
                break;
            case ESQUERDA:
                swipe(new Point(left, midPoint.y), new Point(right, midPoint.y), SCROLL_DURATION);
                break;
            case DIREITA:
                swipe(new Point(right, midPoint.y), new Point(left, midPoint.y), SCROLL_DURATION);
                break;
            default:
                throw new IllegalArgumentException("swipeScreen(): dir: " + direcaoScroll + " NOT supported");
        }

    }

    /**
     * Swipe de tela, utilizando o ponto inicial e o ponto final
     * Metodo utilizado apenas com o scroll de tela
     * @param start Ponto inicial
     * @param end Ponto final
     * @param duration Duração do movimento
     */
    public void swipe(Point start, Point end, Duration duration) {
        PointerInput input = new PointerInput(PointerInput.Kind.TOUCH,"finger");
        Sequence swipe = new Sequence(input, 0);
        swipe.addAction(input.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start.x, start.y));
        swipe.addAction(input.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(input.createPointerMove(duration,PointerInput.Origin.viewport(), end.x, end.y));
        swipe.addAction(input.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(ImmutableList.of(swipe));
    }


    /* COMANDOS DO ADB, UTILIZANDO A CLASSE LOCALCOMMANDLINE, OU SEJA, VIA TERMINAL, HENDLESS, CMD */
    public void sendKeysCommandLine(String text) throws IOException {
        runCommand = new LocalCommandLine();
        runCommand.executeCommand("adb shell input text " + text);
        getDriver().hideKeyboard();
    }

    public void getFileCommandLine() throws IOException {
        runCommand = new LocalCommandLine();
        runCommand.executeCommand("adb pull /sdcard/Download/downloadfile.pdf " + root + "/vpass-qa-android-identprofissionais-automation/output");
    }

    public void removeFileCommandLine() throws IOException {
        runCommand = new LocalCommandLine();
        runCommand.executeCommand("adb shell rm -f -rR -v /sdcard/Download/downloadfile.pdf");
    }

    public void selectComponentPicker(By element, String value) throws Exception  {
        WebElement webElement = aguardaElementoEstarVisivel(element);
        try {
            webElement.sendKeys(value);
        } catch (WebDriverException e) {
            extent.registroLogFalha("Erro ao digitar valor no elemento " + element.toString() + ": " + e.getMessage(), driver);
            throw e;
        }
    }

}
