# 🐾 Ampara Pet

Ampara Pet é um site desenvolvido para uma ONG fictícia de proteção animal com o objetivo de facilitar a adoção de cães e gatos. O sistema conta com uma área pública e um painel administrativo com controle de acesso, permitindo gerenciar os animais disponíveis para adoção e usuários com diferentes permissões.

---

## Funcionalidades

- Cadastro e login de usuários
- Perfis de acesso: **Administrador** e **Visitante**
- CRUD completo de animais (criar, listar, editar e excluir)
- Filtro de busca por nome, raça, idade e porte
- Controle de acesso com Spring Security e autenticação JWT
- Documentação interativa da API com Swagger

---

## 🛠Tecnologias utilizadas

### ⚙️ Back-end

- Java 21  
- Spring Boot  
- Spring Data JPA  
- Spring Security  
- MapStruct  
- MySQL  
- Swagger / OpenAPI  
- Postman  

---

## Como rodar o projeto localmente

### Pré-requisitos

- Java 17 ou superior  
- MySQL  
- Maven  
- IDE de sua preferência (IntelliJ, Eclipse ou VSCode)

### 📦 Passos

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/ampara-pet.git

# Acesse o diretório do projeto
cd ampara-pet

# Compile o projeto
mvn clean install

# Rode o projeto
mvn spring-boot:run
