// authService.js
import { api } from "./api";

export const login = (email, senha) =>
  api("/login", { method: "POST", body: JSON.stringify({ email, senha }) });

export const cadastrar = (dados) =>
  api("/clientes", { method: "POST", body: JSON.stringify(dados) });