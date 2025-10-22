# Guia de Testes Unitários

## Executar os Testes

Para executar todos os testes unitários:

\`\`\`bash
./mvnw test
\`\`\`

Para executar um teste específico:

\`\`\`bash
./mvnw test -Dtest=HashServiceTest
\`\`\`

Para executar testes com relatório de cobertura:

\`\`\`bash
./mvnw test jacoco:report
\`\`\`

## Cobertura de Testes Completa

### Serviços de Autenticação

#### HashServiceTest
- ✅ Geração de hash BCrypt
- ✅ Verificação de senha correta
- ✅ Verificação de senha incorreta
- ✅ Hashes diferentes para mesma senha (salt aleatório)

#### JwtServiceTest
- ✅ Geração de token JWT válido
- ✅ Tokens diferentes para usuários diferentes
- ✅ Validação de claims do token
- ✅ Expiração de token

#### AuthResourceTest
- ✅ Login com sucesso (retorna token e dados do usuário)
- ✅ Login com senha incorreta (401)
- ✅ Login com email inexistente (401)
- ✅ Login com email inválido (400)
- ✅ Login com campos vazios (400)

#### UsuarioServiceTest
- ✅ Criação de usuário com senha hasheada
- ✅ Autenticação com sucesso
- ✅ Autenticação com senha incorreta
- ✅ Autenticação com email inexistente
- ✅ Atualização de usuário mantendo senha hasheada
- ✅ Listagem de usuários
- ✅ Busca de usuário por ID
- ✅ Exclusão de usuário

### Serviços de Chat e Mensagens

#### ChatServiceTest
- ✅ Criação de novo chat
- ✅ Retorno de chat existente (evita duplicação)
- ✅ Listagem de chats por usuário
- ✅ Listagem de chats por ponto de coleta
- ✅ Listagem de todos os chats
- ✅ Busca de chat por usuário e ponto de coleta

#### MensagemServiceTest
- ✅ Envio de mensagem do usuário
- ✅ Envio de mensagem do ponto de coleta
- ✅ Listagem de mensagens por chat
- ✅ Listagem de todas as mensagens

### Serviços de Material

#### MaterialServiceImplTest
- ✅ Listagem de materiais com paginação
- ✅ Busca de material por ID (sucesso)
- ✅ Busca de material por ID (não encontrado)
- ✅ Criação de material
- ✅ Atualização de material (sucesso)
- ✅ Atualização de material (não encontrado)
- ✅ Exclusão de material sem imagem
- ✅ Exclusão de material com imagem
- ✅ Exclusão de material (não encontrado)
- ✅ Download de imagem
- ✅ Upload de imagem nova
- ✅ Upload substituindo imagem existente
- ✅ Upload com material não encontrado

### Serviços de Ponto de Coleta

#### PontoColetaServiceImplTest
- ✅ Listagem de pontos sem filtro
- ✅ Listagem de pontos com filtro de material
- ✅ Busca por distância (geolocalização)
- ✅ Busca por nome e endereço
- ✅ Busca de ponto por ID (sucesso)
- ✅ Busca de ponto por ID (não encontrado)
- ✅ Criação de ponto de coleta
- ✅ Atualização de ponto de coleta (sucesso)
- ✅ Atualização de ponto de coleta (não encontrado)
- ✅ Exclusão de ponto de coleta
- ✅ Download de imagem
- ✅ Upload de imagem nova
- ✅ Upload substituindo imagem existente
- ✅ Upload com ponto não encontrado

### Serviço de Arquivos

#### FileServiceTest
- ✅ Upload de arquivo com sucesso
- ✅ Upload de arquivo muito grande (validação)
- ✅ Upload de tipo de arquivo não suportado
- ✅ Exclusão de arquivo existente
- ✅ Exclusão de arquivo inexistente
- ✅ Download de arquivo existente
- ✅ Download de arquivo inexistente (retorna padrão)

### Resources (Endpoints REST)

#### ChatResourceTest
- ✅ Criação de chat com sucesso
- ✅ Criação de chat com usuário não encontrado
- ✅ Criação de chat com ponto não encontrado
- ✅ Criação de chat com request inválido
- ✅ Listagem de chats por usuário (sucesso)
- ✅ Listagem de chats por usuário (não encontrado)
- ✅ Listagem de todos os chats
- ✅ Envio de mensagem com sucesso
- ✅ Envio de mensagem com chat não encontrado
- ✅ Envio de mensagem com request inválido
- ✅ Listagem de mensagens (sucesso)
- ✅ Listagem de mensagens (chat não encontrado)

#### MaterialResourceTest
- ✅ Busca de material por ID
- ✅ Listagem de todos os materiais
- ✅ Criação de material
- ✅ Atualização de material
- ✅ Exclusão de material
- ✅ Download de imagem (sucesso)
- ✅ Download de imagem (não encontrada)

#### PontoColetaResourceTest
- ✅ Busca de ponto por ID
- ✅ Listagem sem filtros
- ✅ Listagem com filtro de distância
- ✅ Listagem com filtro de termo de busca
- ✅ Criação de ponto de coleta
- ✅ Atualização de ponto de coleta
- ✅ Exclusão de ponto de coleta
- ✅ Download de imagem (sucesso)
- ✅ Download de imagem (não encontrada)

### Repositories

#### ChatRepositoryTest
- ✅ Busca de chat por usuário e ponto (encontrado)
- ✅ Busca de chat por usuário e ponto (não encontrado)

#### MaterialRepositoryTest
- ✅ Busca de múltiplos materiais por IDs
- ✅ Busca com lista vazia

## Requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL (para testes de integração)
- Mockito (para mocks)
- JUnit 5 (framework de testes)

## Configuração do Banco de Dados de Teste

Os testes usam o banco de dados configurado no `application.properties`. Para testes isolados, considere usar um banco de dados em memória ou um container Docker.

### Exemplo de configuração para testes:

\`\`\`properties
# application-test.properties
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:testdb
quarkus.hibernate-orm.database.generation=drop-and-create
