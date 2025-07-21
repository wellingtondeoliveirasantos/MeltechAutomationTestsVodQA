package testsuites;

import br.com.automation.setupdriver.Initializer;
import org.junit.jupiter.api.*;
import testcases.*;

@Tag("vodqa")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class SmokeTestingVodQA extends Initializer {

    private LoginCases login;

    @BeforeAll
    public static void setupAppName() {
        appNome = "vodqa";
    }

    @BeforeEach()
    public void Background() {
        login = new LoginCases();
    }

    @Nested
    @Order(1)
    @Tag("Login")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class Login{
        @Test
        @Order(1)
        @DisplayName("TC - Valida sucesso no login com senha digitada válida")
        public void valida_Sucesso_Acesso_App() throws Exception {
            login.validaSucessoAcessoApp();
        }

    }

}
