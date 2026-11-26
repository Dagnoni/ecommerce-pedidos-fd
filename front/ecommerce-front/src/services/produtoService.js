import { api } from "./api";
import { produtos } from "../data/produtos";

const USAR_MOCK = true; // vire false quando o backend estiver pronto

export async function listarProdutos() {
  if (USAR_MOCK) return produtos;
  return api("/produtos");
}

export async function buscarProduto(id) {
  if (USAR_MOCK) return produtos.find((p) => p.id === Number(id));
  return api(`/produtos/${id}`);
}