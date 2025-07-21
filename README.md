# Meltech Automation Tests UI

[![Java](https://img.shields.io/badge/Java-21-blue)](https://jdk.java.net/21/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-green)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.9.3-orange)](https://junit.org/junit5/)
[![Selenium](https://img.shields.io/badge/Selenium-4.15.0-yellow)](https://www.selenium.dev/)
[![Appium](https://img.shields.io/badge/Appium-9.0.0-red)](https://appium.io/)
[![License](https://img.shields.io/badge/License-Internal-lightgrey)](#licença)

---

## Descrição

Este projeto automatiza testes de interface para aplicações Android e web, utilizando Java 21, Selenium, Appium, JUnit 5 e ExtentReports, com foco em confiabilidade, escalabilidade e fácil manutenção.

---

## Tecnologias

* Java 21
* Maven 3.8+
* JUnit 5 (Jupiter)
* Selenium 4.15.0
* Appium Java Client 9.0.0
* ExtentReports 5.1.1
* Lombok 1.18.24
* Apache Commons IO 2.15.1
* SLF4J Simple 2.0.5

---

## Pré-requisitos

* Java JDK 21 instalado e configurado no PATH
* Maven 3.8+ instalado
* Node.js instalado (para Appium)
* Appium Server instalado e rodando
* Emulador Android ou dispositivo físico conectado
* IDE com suporte a Lombok (IntelliJ recomendado)

---

## Estrutura do projeto

├── app                          # APK para testes mobile  
│   └── VodQA.apk  
├── output                       # Relatórios e vídeos gerados  
│   ├── report_android_vodqa_21-07-2025.html  
│   └── vodqa  
│       └── video  
│           └── TC - Valida sucesso no login com senha digitada válida.mp4  
├── pom.xml                      # Configuração Maven  
├── README.md                    # Documentação  
└── src  
├── main  
│   ├── java  
│   │   └── br              # Código principal (configs, helpers)  
│   └── resources  
│       └── globalParameters.properties # Configurações globais  
└── test  
├── java  
│   ├── testcases       # Testes automatizados  
│   └── testsuites      # Suites de testes agrupados  
└── resources           # Recursos auxiliares dos testes

---

## Como executar os testes

1. Certifique-se que o Appium Server está rodando (execute `appium` no terminal)
2. Garanta que o emulador Android ou dispositivo físico está conectado e disponível
3. Execute o comando para rodar os testes:

```bash
mvn test -Dplatform=android  
# MeltechAutomationTestsVodQA
