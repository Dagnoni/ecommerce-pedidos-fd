import { createContext, useContext, useState } from "react";

const CarrinhoContext = createContext();

export function CarrinhoProvider({ children }) {
  const [itens, setItens] = useState([]);

  function adicionar(produto, opcoes = {}) {
    // cada combinação produto + cor + armazenamento é um item diferente
    const chave = `${produto.id}-${opcoes.cor ?? ""}-${opcoes.armazenamento ?? ""}`;

    setItens((atual) => {
      const existente = atual.find((i) => i.chave === chave);
      if (existente) {
        return atual.map((i) =>
          i.chave === chave ? { ...i, quantidade: i.quantidade + 1 } : i
        );
      }
      return [...atual, { chave, produto, ...opcoes, quantidade: 1 }];
    });
  }

  function remover(chave) {
    setItens((atual) => atual.filter((i) => i.chave !== chave));
  }

  function alterarQuantidade(chave, quantidade) {
    if (quantidade < 1) return remover(chave);
    setItens((atual) =>
      atual.map((i) => (i.chave === chave ? { ...i, quantidade } : i))
    );
  }

  function limpar() {
    setItens([]);
  }

  const subtotal = itens.reduce((soma, i) => soma + i.produto.preco * i.quantidade, 0);

  return (
    <CarrinhoContext.Provider
      value={{ itens, subtotal, adicionar, remover, alterarQuantidade, limpar }}
    >
      {children}
    </CarrinhoContext.Provider>
  );
}

export function useCarrinho() {
  return useContext(CarrinhoContext);
}