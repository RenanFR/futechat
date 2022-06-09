# Futechat
## _Estatísticas e dados do mundo do futebol em tempo real enquanto você conversa no discord_

Uma resenha sobre futebol é um dos passatempos favoritos do brasileiro, tanto na família ou entre um grupo de amigos de longa data e até mesmo como um quebra gelo entre desconhecidos. Discutir os placares da última rodada, cornetar o centroavante em jejum ou repercutir as estatísticas de uma partida específica, a conversa vai se estendendo e prolongando e é sempre uma experiência agradável e criadora de laços, independente da rivalidade
Antigamente sediada primariamente nos bares, festas e outros locais físicos, a resenha futebolística como qualquer outro aspecto do cotidiano foi impactada pela inovação dos tempos modernos. Grupos no whatsapp, servidores no discord e outros meios digitais e remotos são onde ela acontece ou mesmo se estende após um evento físico como um churrasco ou uma ida com os amigos ao estádio
Você já esteve em uma dessas conversas e surgiu uma dúvida sobre um fato do futebol? Como por exemplo quem foi o último artilheiro da Premier League? Ou qual a altura daquele zagueiro do PSG? Ou talvez quem foi o jogador envolvido na transferência mais cara do último ano?
Dados enriquecem a conversa e as vezes é chato ter que sair do discord para pesquisar no google e dependendo de quão específica é a pergunta pode ser difícil encontrar
O Futechat é um bot integrado ao discord, ele provê dados do futebol em tempo real por meio da execução de simples comandos, você tem a informação a sua disposição para fortalecer seu argumento na resenha e nem precisa sair da janela da conversa
## Exemplos
```sh
/transferencias_jogador nome:Neymar time:Paris Saint Germain
```
```sh
/altura_jogador nome:Neymar time:Paris Saint Germain
```
```sh
/artilheiro temporada:2021 liga:Ligue 1
```
## Arquitetura
As etapas seguidas para a publicação desse serviço foram
- Criação do cluster no ECS que será nosso ambiente de execução
  - Ele requer a criação de uma VPC e subnets onde as instâncias de nosso serviços estarão hospedados
- Criação da task definition vinculando o container com a imagem apontando para o ECR
  - A task definition consiste na definição a nível de container, aspectos como imagem base, portas expostas e variáveis de ambiente são inseridos aqui
- A Service é a abstração de execução que representa o agregador de instâncias (Tasks), aqui configuramos o número desejável de instâncias mínimo e máximo
## Integração contínua
A pipeline de CI/CD (A nossa está no ***CodePipeline***) que consiste no agregador das etapas de implantação do projeto, geralmente consiste em uma etapa de origem de onde o código fonte é clonado com base no repositório, uma etapa de Build (Utilizamos o ***CodeBuild*** da amazon) que é onde a aplicação é compilada, os testes são executados e os artefatos de execução são gerados e o Deploy (Utilizamos o ***CodeDeploy integrado com o ECS***) que é a efetiva implantação dos artefatos de execução da aplicação no ambiente alvo que irá hospedar a aplicação
###### Origem
Nessa etapa configuramos de que provedor nosso código será clonado dentre os vários disponíveis na aws
O nome do repositório e qual a branch que irá acionar a pipeline são escolhidos nesse momento
###### Construção
O [***buildspec.yml***](https://github.com/RenanFR/futechat/blob/main/buildspec.yml) é o arquivo no qual configuramos como a aplicação será compilada e construída no CodeBuild. Responsável por gerar o artefato
Em nosso buildspec temos
- O login no ***ECR*** e Docker para construção e push da imagem da aplicação para nosso repositório
- Instalação da ***jdk 18*** e última versão do ***maven***
- Geração do artefato jar com o comando `mvn clean package`
- Geração da imagem Docker com o comando `mvn spring-boot:build-image` proveniente do ***Buildpack*** contido no plugin presente no pom
- Geração do ***imagedefinitions.json*** com os dados da imagem Docker produzida pelo processo e que usaremos no Deploy
A máquina gerenciada do CodeBuild já possui os utilitários necessários para o processo como o aws, Docker e mvn

A função de serviço do CodeBuild precisa ter acesso ao ECR
O CodeBuild é cobrado por tempo de execução dos builds portanto é interessante otimizar e customizar a máquina de runtime trabalhando com uma máquina customizada com imagem própria adaptada para as necessidades do projeto
###### Implantação
Na criação da etapa de Deploy escolhemos o ***ECS*** como provedor e o cluster criado anteriormente, o arquivo de definição de imagem é o mesmo configurado como saída no ***buildspec*** e esse é o ***imagedefinitions.json***

