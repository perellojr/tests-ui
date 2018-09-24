# language: pt
Funcionalidade: Faz uma pesquisa no google

  Contexto: 
    Dado que acesse o site do google

  Cenario: Faz uma pesquisa no google
    Quando valido todos os elementos da pagina inicial do google
    E preencho o campo de pesquisa com dados validos
      | txtPesquisa                          |
      | Cristiane silveira da silva exército |
