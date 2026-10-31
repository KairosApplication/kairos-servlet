# ⏳ Kairos

### Sistema de Gerenciamento de Estoque e Reposição de Produtos

O **Kairos** é uma aplicação web desenvolvida para auxiliar empresas no gerenciamento de estoques, na organização de produtos e no acompanhamento de processos de reposição.

O projeto busca facilitar o controle de mercadorias, permitindo que administradores e funcionários acompanhem informações sobre produtos, setores, gôndolas, reposições e alertas de ruptura de estoque.

A aplicação foi desenvolvida utilizando **Java, Jakarta Servlets, JSP e PostgreSQL**, seguindo uma arquitetura baseada no padrão **MVC (Model-View-Controller)**.

---

## 📌 Sobre o projeto

O gerenciamento eficiente de estoques é fundamental para empresas que trabalham com grandes quantidades de produtos.

A falta de organização e acompanhamento pode ocasionar problemas como rupturas de estoque, dificuldades na reposição de mercadorias e perda de informações importantes.

O **Kairos** foi idealizado para contribuir com a solução desses problemas, centralizando diferentes operações em uma única plataforma.

### Objetivos

- Facilitar o gerenciamento de produtos e estoques.
- Organizar produtos em setores e gôndolas.
- Auxiliar no acompanhamento de reposições.
- Registrar e acompanhar alertas de ruptura.
- Gerenciar usuários e suas responsabilidades.
- Disponibilizar informações de maneira organizada e acessível.
- Manter registros históricos de operações importantes.

---

## 🚀 Funcionalidades

O sistema possui diferentes módulos administrativos.

| Módulo     | Funcionalidade                                              |
| ---------- | ----------------------------------------------------------- |
| Usuários   | Cadastro, consulta, atualização e exclusão de usuários.     |
| Empresas   | Gerenciamento de empresas cadastradas na plataforma.        |
| Setores    | Organização e gerenciamento dos setores.                    |
| Gôndolas   | Gerenciamento de gôndolas e associação de produtos.         |
| Produtos   | Cadastro e gerenciamento de produtos.                       |
| Reposições | Registro e acompanhamento de reposições de mercadorias.     |
| Alertas    | Gerenciamento de alertas relacionados à ruptura de estoque. |
| Compras    | Registro e gerenciamento de compras.                        |

### Funcionalidades adicionais

**Autenticação e cadastro**

O sistema conta com páginas de login e cadastro de usuários, incluindo validações dos dados informados.

**Relacionamentos entre entidades**

A aplicação permite estabelecer associações entre diferentes entidades, como usuários e gôndolas, além de produtos e gôndolas.

**Pesquisa de registros**

Os módulos administrativos possuem recursos de busca para facilitar a localização de informações.

**Histórico de operações**

O banco de dados contempla estruturas destinadas ao armazenamento de históricos de alertas, reposições e compras.

---

## 🛠️ Tecnologias utilizadas

### Back-End

- **Java:** linguagem principal da aplicação.
- **Jakarta Servlets:** processamento de requisições HTTP.
- **JDBC:** comunicação com o banco de dados.
- **Apache Tomcat:** servidor de aplicação.

### Front-End

- **JSP (JavaServer Pages):** construção de páginas dinâmicas.
- **HTML5:** estrutura das páginas.
- **CSS3:** estilização e responsividade.
- **JavaScript:** interatividade e manipulação de elementos.

### Banco de dados

- **PostgreSQL:** armazenamento e gerenciamento dos dados.
- **SQL:** criação, consulta e manipulação dos registros.

### Ferramentas de desenvolvimento

- Git
- GitHub
- IntelliJ IDEA
- Visual Studio Code
- DBeaver
- pgAdmin

---

## 🏗️ Arquitetura do sistema

O projeto utiliza uma organização baseada no padrão **MVC**, com separação de responsabilidades entre as diferentes camadas.

### Model

Responsável pela representação das entidades utilizadas pelo sistema.

Exemplos:

- Usuário
- Empresa
- Setor
- Gôndola
- Produto
- Reposição
- Alerta
- Compra

### View

Responsável pela interface apresentada ao usuário.

As páginas são desenvolvidas utilizando JSP, HTML, CSS e JavaScript.

### Controller

Responsável por receber e processar requisições HTTP, coordenando a comunicação entre a interface e as demais camadas.

Essa responsabilidade é desempenhada pelos Servlets.

### Service

Camada responsável por organizar regras de negócio e validações da aplicação.

### DAO (Data Access Object)

Camada responsável pela comunicação com o PostgreSQL, realizando operações de persistência e consulta dos dados.

---

## 📁 Organização do projeto

A estrutura abaixo representa a organização principal da aplicação.

```text
Kairos/
│
├── src/
│   └── main/
│       │
│       ├── java/
│       │   └── com/
│       │       └── kairos/
│       │           │
│       │           ├── model/
│       │           ├── dao/
│       │           ├── service/
│       │           ├── servlet/
│       │           └── utils/
│       │
│       └── webapp/
│           │
│           ├── assets/
│           │   ├── css/
│           │   ├── js/
│           │   └── images/
│           │
│           └── WEB-INF/
│               └── views/
│
└── README.md
```

A organização separa as responsabilidades da aplicação, facilitando a manutenção, o desenvolvimento de novas funcionalidades e o trabalho colaborativo.

---

## 🗄️ Banco de dados

O Kairos utiliza **PostgreSQL** como sistema gerenciador de banco de dados relacional.

Entre as principais tabelas do projeto, estão:

| Tabela       | Descrição                                 |
| ------------ | ----------------------------------------- |
| `usuarios`   | Armazena informações dos usuários.        |
| `empresas`   | Armazena os dados das empresas.           |
| `setores`    | Organiza os setores cadastrados.          |
| `gondolas`   | Armazena as gôndolas e suas capacidades.  |
| `produtos`   | Armazena os produtos cadastrados.         |
| `reposicoes` | Registra as operações de reposição.       |
| `alertas`    | Armazena alertas relacionados ao estoque. |
| `compras`    | Registra informações de compras.          |

O banco também utiliza tabelas auxiliares para representar relacionamentos entre entidades e estruturas de histórico para preservar registros de operações.

---

## ⚙️ Como executar o projeto

### Pré-requisitos

Para configurar o ambiente de desenvolvimento, são necessários:

- JDK compatível com a versão utilizada pelo projeto.
- Apache Tomcat 11.
- PostgreSQL ou uma instância PostgreSQL hospedada.
- IDE com suporte ao desenvolvimento Java.
- Dependências Jakarta EE e driver JDBC do PostgreSQL.

### Configuração

1. Clone o repositório:

```bash
git clone URL_DO_REPOSITORIO
```

2. Abra o projeto na IDE de sua preferência.

3. Configure a conexão com o banco de dados PostgreSQL, utilizando as credenciais correspondentes ao ambiente.

4. Execute os scripts SQL necessários para criar as tabelas e demais estruturas do banco de dados.

5. Configure o Apache Tomcat e as dependências da aplicação.

6. Compile e publique a aplicação no servidor.

7. Acesse a aplicação pelo navegador utilizando o endereço e o contexto configurados no Tomcat.

> **Importante:** as credenciais do banco de dados não devem ser publicadas no repositório. Utilize configurações locais ou variáveis de ambiente para armazenar informações sensíveis.

---

## 🔄 Controle de versão

O desenvolvimento utiliza **Git e GitHub** para gerenciamento do código-fonte e colaboração entre os integrantes.

O fluxo de trabalho contempla branches destinadas ao desenvolvimento de funcionalidades, integração e manutenção da versão principal.

As alterações podem ser integradas por meio de **Pull Requests**, permitindo a revisão do código antes de sua incorporação às branches compartilhadas.

---

## 👥 Equipe de desenvolvimento

O Kairos é desenvolvido por uma equipe com responsabilidades distribuídas entre diferentes áreas.

| Integrante       | Responsabilidade                                 |
| ---------------- | ------------------------------------------------ |
| Victor Backschat | Back-End                                         |
| Heitor Rangel    | Banco de Dados                                   |
| Davi Carracedo   | Gerenciamento do Projeto, Assistente e Front-End |
| Arthur Souza     | Front-End                                        |
| Arthur Teixeira  | Design Gráfico                                   |

O trabalho envolve desenvolvimento de interfaces, implementação de regras de negócio, gerenciamento de dados, organização do projeto e criação da identidade visual.

---

## 🎓 Contexto acadêmico e ODS 12

O **Kairos** é um projeto desenvolvido no **Instituto J&F — Germinare Tech**, tendo como principal referência a **ODS 12 — Consumo e Produção Responsáveis**, um dos 17 Objetivos de Desenvolvimento Sustentável estabelecidos pela Organização das Nações Unidas (ONU).

A ODS 12 busca promover padrões sustentáveis de consumo e produção, incentivando o uso eficiente dos recursos naturais, a redução de desperdícios e a adoção de práticas mais responsáveis por empresas e consumidores.

### 🌱 Relação do Kairos com a ODS 12

O Kairos foi idealizado para contribuir com esses objetivos por meio da tecnologia, oferecendo uma solução para melhorar o gerenciamento de estoques e os processos de reposição de mercadorias.

A falta de controle sobre produtos pode ocasionar compras desnecessárias, excesso de mercadorias, perdas e desperdícios. Por isso, o sistema busca proporcionar maior organização e acompanhamento das operações.

Entre suas contribuições potenciais para a ODS 12, destacam-se:

- **Redução de desperdícios:** auxiliar na identificação de problemas de estoque e no acompanhamento de produtos, contribuindo para evitar perdas.
- **Gestão eficiente de recursos:** facilitar o controle de mercadorias e a organização dos processos de reposição.
- **Consumo responsável:** disponibilizar informações que possam apoiar decisões de compra e reposição mais conscientes.
- **Organização dos estoques:** melhorar a distribuição e o acompanhamento de produtos em setores e gôndolas.
- **Acompanhamento de operações:** registrar reposições, compras e alertas, proporcionando maior rastreabilidade das atividades.

### 🎯 Metas da ODS 12 relacionadas ao projeto

**Meta 12.3 — Redução do desperdício de alimentos**

Busca reduzir pela metade o desperdício de alimentos per capita mundial nos níveis de varejo e consumidor até 2030, além de diminuir as perdas de alimentos ao longo das cadeias de produção e abastecimento.

O Kairos se relaciona com essa meta ao propor um gerenciamento mais eficiente dos estoques, especialmente em estabelecimentos comerciais que trabalham com produtos alimentícios.

**Meta 12.5 — Redução da geração de resíduos**

Busca reduzir substancialmente a geração de resíduos por meio da prevenção, redução, reciclagem e reutilização.

O projeto pode contribuir para essa meta ao auxiliar empresas na prevenção de perdas decorrentes da má organização e do controle inadequado de mercadorias.

### 💡 Tecnologia e sustentabilidade

O desenvolvimento do Kairos demonstra como conhecimentos de programação, bancos de dados e engenharia de software podem ser aplicados à resolução de problemas relacionados à sustentabilidade.

Além de proporcionar experiência prática em desenvolvimento de sistemas, o projeto incentiva a reflexão sobre a responsabilidade das empresas na utilização de recursos e na redução de desperdícios.

Dessa forma, o **Kairos une tecnologia, gestão e sustentabilidade**, buscando contribuir para práticas comerciais mais eficientes e alinhadas aos princípios da ODS 12.

---

## 📈 Evolução do projeto

O Kairos contempla funcionalidades de gerenciamento de estoque e reposição, com uma estrutura preparada para manutenção e expansão.

Entre as possibilidades de evolução estão o aperfeiçoamento das validações, melhorias na experiência do usuário, ampliação dos recursos de acompanhamento e desenvolvimento de novas funcionalidades administrativas.

---

**Kairos — Organização, controle e eficiência no gerenciamento de estoques.**
