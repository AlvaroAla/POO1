# SysBiblioteca - Projeto Final de POO (Java + Swing)

Sistema de biblioteca com:
- **Interface Java (`interface`)** (`Identificavel` aplicada em `Pessoa`, `ItemAcervo`, `Exemplar` e `Emprestimo`)
- **Herança** (`Pessoa` -> `Usuario`/`Bibliotecario`)
- **Classe abstrata + Polimorfismo** (`ItemAcervo` -> `Livro`/`Revista`/`MidiaDigital`)
- **Agregação** (`Exemplar` referencia `ItemAcervo`)
- **Composição** (`Emprestimo` com datas e referência obrigatória ao exemplar/usuário)
- **Persistência** (serialização em `data/biblioteca.dat`)
- **Exceções customizadas**
- **Interface Swing** (tabs: Usuários, Acervo, Empréstimos/Devoluções)

## Como executar (sem IDE)
Requisitos: Java 8+.

No terminal, dentro da pasta do projeto:

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out br.edu.biblioteca.app.Main
```

> Os dados são salvos automaticamente quando você fecha a janela e confirma.

## Como executar (Eclipse/IntelliJ)
1. Importe como **Java Project** (não-Maven).
2. Marque `src/` como **Source Folder**.
3. Rode a classe `br.edu.biblioteca.app.Main`.

## Pastas
- `src/` código-fonte
- `data/` arquivo persistido
- `docs/` relatório + UML (gerados)

## Observação (screenshots)
Para o relatório, execute o sistema e tire prints das telas (Usuários, Acervo e Empréstimos) e substitua no PDF caso sua professora exija prints reais.
