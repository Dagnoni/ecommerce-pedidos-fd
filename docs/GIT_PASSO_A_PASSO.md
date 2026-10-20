# Enviando ao Git remoto sem problemas

Rode na raiz do clone (a pasta que contém `.git`).

```bash
# 0. Evita o "tudo modificado" por CRLF/LF
git config core.autocrlf true

# 1. main atualizada
git switch main
git pull

# 2. branch limpa (nome SEM o prefixo "origin/")
git switch -c test/tratamento-polimorfismo

# 3. Copie por cima do clone os arquivos deste pacote
#    (back/, docs/, .gitignore, .gitattributes, README.md)
#    ATENÇÃO: o nome do arquivo é ".gitignore" (sem ".txt").

# 4. Tire do controle de versão o que não deveria estar lá
git rm -r --cached back/target .vscode
git rm --cached .gitignore.txt

# 5. Confira ANTES de commitar
git status                      # não pode ter <<<<<<< , target/ nem .vscode/
grep -rn '^<<<<<<<\|^>>>>>>>' back/src    # deve voltar vazio
cd back && mvn test && cd ..    # tudo verde

# 6. Commits pequenos
git add .gitignore .gitattributes
git commit -m "chore: corrige .gitignore e remove artefatos de build"
git add back/pom.xml
git commit -m "build: adiciona junit-jupiter-engine e surefire; corrige tag name"
git add back/src/main
git commit -m "fix: resolve conflitos de merge e delega processar() à versão completa"
git add back/src/test
git commit -m "test: cobre Pedido.pagar, Dinheiro e sobrecarga de processar"
git add docs
git commit -m "docs: mapa de exceções e verificação dos checklists"

# 7. Envio
git push -u origin test/tratamento-polimorfismo
```

Depois abra o Pull Request, peça revisão de um colega e só então faça o merge na `main`.

Se o `git pull` acusar conflito, **não commite com os marcadores** (`<<<<<<<`):
abra o arquivo, escolha o trecho correto, apague os marcadores e rode `mvn test`.
