export const ROTAS = {
  home: "/",
  catalogo: "/catalogo",
  produto: "/produto/:id",
  carrinho: "/carrinho",
  pagamento: "/pagamento",
  cadastro: "/cadastro",
  login: "/entrar",
  admin: "/admin",
};

// monta o link de um produto específico: /produto/3
export const rotaProduto = (id) => `/produto/${id}`;