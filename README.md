# AgroSense

## Membros do Grupo
- **Antonio Augusto Gomes dos Santos** – RM550344
- **Camilla Ribeiro Santana** – RM99491
- **Charles Carvalho da Silveira Carvalho** - RM550113
- **Luan Ribeiro Dias** - RM94156
- **Raphael Torres Gonçalves** - RM99354

---

## Sobre o Projeto
A aplicação é um app móvel que permite aos usuários analisarem alimentos e classificarem sua qualidade como **bom**, **médio** ou **ruim**. Utilizando uma inteligência artificial, o aplicativo oferece uma **avaliação em tempo real** sobre o estado do alimento. Além disso, o app armazena um **histórico de todas as análises**, permitindo que os usuários acompanhem a condição dos alimentos ao longo do tempo.

Com um **design intuitivo e fácil de usar**, o aplicativo é uma ferramenta valiosa para **evitar o desperdício de alimentos** e **informar melhor as decisões de consumo**. Ele atende tanto consumidores comuns quanto profissionais do setor alimentício, oferecendo uma forma prática de monitorar a qualidade dos produtos.

---

## Funcionalidades CRUD
O CRUD foi implementado principalmente para gerenciar o cadastro, login e perfil dos usuários:

- **GET e POST**: Utilizados para **realizar o cadastro e login** de usuários.
- **PUT e DELETE**: Na sessão do perfil, permitem **atualizar e excluir o cadastro** do usuário.

### Armazenamento de Dados
O aplicativo utiliza **SharedPreferences** para armazenar o ID do usuário que fez login, facilitando o uso desse ID em outras áreas da aplicação, como na funcionalidade de histórico. As **SharedPreferences** também são usadas para salvar as análises, permitindo que as informações sejam recuperadas para consulta futura.
