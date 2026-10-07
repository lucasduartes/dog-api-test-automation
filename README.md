# Automação de Testes - Dog API

[![Dog API Tests](https://github.com/lucasduartes/dog-api-test-automation/actions/workflows/api-tests.yml/badge.svg)](https://github.com/lucasduartes/dog-api-test-automation/actions/workflows/api-tests.yml)

Projeto de automação de testes de API para a [Dog CEO API](https://dog.ceo/dog-api/documentation), desenvolvido com Java, REST Assured e JUnit 5.

A suíte valida os principais endpoints da API por meio de testes funcionais, negativos, de contrato e de integração, com geração de relatórios Allure e execução automatizada em Linux, Windows e macOS através do GitHub Actions.

## Tecnologias utilizadas

- Java 11
- Maven
- REST Assured
- JUnit 5
- JSON Schema Validator
- Allure Report
- GitHub Actions

## Endpoints testados

| Método | Endpoint | Cobertura |
|---|---|---|
| GET | `/breeds/list/all` | Status HTTP, payload, estrutura de raças e contrato |
| GET | `/breed/{breed}/images` | Raça válida, URLs das imagens, contrato e raça inexistente |
| GET | `/breeds/image/random` | Status HTTP, URL da imagem e contrato |

Também existe um cenário de integração entre endpoints, no qual uma raça retornada por `/breeds/list/all` é utilizada para consultar suas imagens em `/breed/{breed}/images`.

## Estrutura do projeto

```text id="yy61zw"
src/test
├── java/com/dogapi
│   ├── client
│   │   └── DogApiClient.java
│   ├── config
│   │   └── TestConfig.java
│   ├── specs
│   │   └── RequestSpecFactory.java
│   └── tests
│       ├── BreedsTest.java
│       ├── BreedImagesTest.java
│       ├── RandomImageTest.java
│       └── BreedIntegrationTest.java
│
└── resources
    ├── allure.properties
    └── schemas
        ├── breeds-list-schema.json
        ├── breed-images-schema.json
        ├── random-image-schema.json
        └── error-schema.json
```

## Arquitetura

O projeto separa a comunicação HTTP das validações realizadas pelos testes:

```text id="b8dnm3"
Tests
  ↓
DogApiClient
  ↓
RequestSpecFactory
  ↓
TestConfig
  ↓
Dog API
```

Responsabilidades principais:

- `DogApiClient`: centraliza as chamadas aos endpoints da API.
- `RequestSpecFactory`: centraliza a configuração do REST Assured e o filtro responsável por anexar requests e responses ao Allure.
- `TestConfig`: concentra configurações do ambiente, como a URL base.
- `tests`: contém os cenários funcionais, negativos, de contrato e de integração.
- `schemas`: contém os JSON Schemas utilizados na validação de contrato.

## Pré-requisitos

Ambiente utilizado no desenvolvimento:

```text id="jhhgq3"
Java 11
Maven 3.x
```

Para validar o ambiente instalado:

```bash id="0wwl6g"
java --version
mvn --version
```

O projeto também inclui Maven Wrapper.

## Como executar os testes

Clone o repositório:

```bash id="8mz8lr"
git clone https://github.com/lucasduartes/dog-api-test-automation.git
cd dog-api-test-automation
```

### Utilizando Maven

```bash id="gl0jbr"
mvn clean test
```

### Utilizando Maven Wrapper no Linux ou macOS

```bash id="4tu8du"
./mvnw clean test
```

### Utilizando Maven Wrapper no Windows

```powershell id="q56amx"
.\mvnw.cmd clean test
```

Por padrão, os testes são executados contra:

```text id="8av1iu"
https://dog.ceo/api
```

Também é possível informar outra URL base através de uma propriedade do Maven:

```bash id="qf1f19"
mvn clean test -DbaseUrl=https://dog.ceo/api
```

## Relatórios

### Maven Surefire

Os resultados padrão do JUnit são gerados em:

```text id="5tyfzt"
target/surefire-reports
```

### Allure Report

Execute os testes:

```bash id="ql0gco"
mvn clean test
```

Em seguida, gere o relatório:

```bash id="zu817d"
mvn allure:report
```

O relatório HTML será criado em:

```text id="8wdv4r"
target/allure-report/index.html
```

Também é possível gerar e abrir o relatório diretamente:

```bash id="jrvno6"
mvn allure:serve
```

O Allure registra os resultados dos testes e os detalhes das requisições e respostas HTTP, facilitando a análise de falhas.

## Integração Contínua

O projeto utiliza GitHub Actions para executar automaticamente a suíte completa em:

- Linux
- Windows
- macOS

O workflow é executado em:

- pushes para a branch `main`;
- pull requests direcionados para `main`;
- execução manual através do GitHub Actions.

Os relatórios são disponibilizados como artifacts da execução:

```text id="85i1ym"
allure-report-<os>
allure-results-<os>
surefire-reports-<os>
```

Os artifacts são publicados mesmo quando algum teste falha, permitindo investigar o erro diretamente pela execução do pipeline.

## Estratégia de testes

A suíte foi desenvolvida para validar o comportamento e o contrato da API sem criar dependência desnecessária de dados que podem mudar ao longo do tempo.

São validados:

- status HTTP;
- conteúdo das respostas;
- contratos JSON;
- cenários positivos;
- cenários negativos;
- estrutura de raças e sub-raças;
- URLs de imagens;
- consistência entre diferentes endpoints.

Informações dinâmicas, como quantidade exata de raças ou imagens, não são fixadas nos testes para evitar falhas causadas por alterações legítimas no conjunto de dados da API.

## Autor

Lucas Duarte