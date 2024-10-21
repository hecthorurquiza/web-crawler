# Bot para Coleta de Dados do Padrão TISS - ANS
Author: Heithor Urquiza

Este projeto tem como objetivo desenvolver um **Web Crawler** (bot) para automatizar a coleta de dados da página da **Agência Nacional de Saúde Suplementar (ANS)**, especificamente relacionados ao **Padrão TISS (Troca de Informações na Saúde Suplementar)**. O projeto está dividido em três bots, cada um com uma tarefa específica.

## Funcionalidades dos Bots

### Bot 1: Download da Documentação do Padrão TISS
- **Objetivo:** Baixar automaticamente os arquivos da documentação do **Padrão TISS** na versão mais recente, disponível no site da ANS.
- **Ação:** O bot acessa a área designada para o **"Espaço do Prestador de Serviços de Saúde"** e faz o download do **Componente de Comunicação** do Padrão TISS.

### Bot 2: Coleta de Dados Históricos das Versões do Padrão TISS
- **Objetivo:** Extrair os dados das versões históricas do Padrão TISS a partir de **jan/2016**.
- **Ação:** O bot acessa o campo **"Histórico das versões dos Componentes do Padrão TISS"** e coleta as seguintes informações:
    - **Competência**
    - **Data de Publicação**
    - **Início de Vigência**
- **Saída:** Os dados são armazenados em um arquivo CSV.

### Bot 3: Download da "Tabela de Erros no Envio para a ANS"
- **Objetivo:** Baixar a tabela de erros relacionada ao envio de dados para a ANS.
- **Ação:** O bot acessa o campo **"Tabelas relacionadas"** e baixa a **"Tabela de erros no envio para a ANS"**, armazenando o arquivo de forma organizada no sistema.
