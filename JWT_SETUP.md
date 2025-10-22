# Guia de Configuração JWT Authentication

## Visão Geral
Esta aplicação agora usa JWT (JSON Web Token) para autenticação e BCrypt para hash de senhas.

## Funcionalidades Adicionadas
1. **Hash de Senhas**: Todas as senhas são criptografadas usando BCrypt antes de serem armazenadas no banco de dados
2. **Geração de Token JWT**: Após login bem-sucedido, um token JWT é gerado com expiração de 24 horas
3. **Endpoints Protegidos**: A maioria dos endpoints agora requer autenticação via token JWT
4. **Controle de Acesso por Perfil**: Endpoints podem ser restritos por perfis de usuário (ADMIN, USUARIO)

## Chaves RSA Configuradas

✅ **As chaves RSA já estão configuradas e prontas para uso!**

Os arquivos de chave já foram criados em:
- `src/main/resources/token/publicKey.pem` (chave pública)
- `src/main/resources/token/privateKey.pem` (chave privada)

Não é necessário gerar novas chaves. O sistema está pronto para uso imediato.

## Uso da API

### 1. Registrar Novo Usuário (Público)
\`\`\`bash
POST /usuarios
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@example.com",
  "senha": "senha123",
  "telefone": "63999999999",
  "tipoUsuario": "USUARIO"
}
\`\`\`

### 2. Login (Público)
\`\`\`bash
POST /auth/login
Content-Type: application/json

{
  "email": "joao@example.com",
  "senha": "senha123"
}
\`\`\`

**Resposta:**
\`\`\`json
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@example.com",
    "telefone": "63999999999",
    "tipoUsuario": "USUARIO"
  }
}
\`\`\`

### 3. Acessar Endpoints Protegidos
Inclua o token JWT no cabeçalho Authorization:

\`\`\`bash
GET /usuarios
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
\`\`\`

## Anotações de Segurança

- `@PermitAll`: Endpoint acessível sem autenticação
- `@RolesAllowed({"ADMIN", "USUARIO"})`: Endpoint requer autenticação e perfis específicos
- Sem anotação: Endpoint requer autenticação (comportamento padrão)

## Segurança de Senhas

- Senhas são automaticamente criptografadas usando BCrypt ao criar ou atualizar usuários
- Senhas originais nunca são armazenadas no banco de dados
- Verificação de senha é feita de forma segura usando o algoritmo de comparação do BCrypt

## Expiração do Token

- Tokens JWT expiram após 24 horas
- Usuários precisam fazer login novamente após a expiração do token
- Token inclui ID do usuário, email e perfis

## Estrutura de Arquivos Criados

\`\`\`
src/main/java/unitins/ecoleta/
├── dto/
│   └── Response/
│       └── AuthResponseDTO.java          # DTO de resposta de autenticação
├── resource/
│   └── AuthResource.java                 # Endpoint de login
├── security/
│   ├── JwtAuthenticationFilter.java      # Filtro de autenticação JWT
│   └── Secured.java                      # Anotação customizada
└── service/
    ├── HashService.java                  # Serviço de hash BCrypt
    └── JwtService.java                   # Serviço de geração JWT

src/main/resources/
└── token/
    ├── publicKey.pem                     # Chave pública RSA
    └── privateKey.pem                    # Chave privada RSA
