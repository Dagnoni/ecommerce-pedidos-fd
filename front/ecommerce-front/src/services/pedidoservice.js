// pedidoService.js
import { api } from "./api";

export const criarPedido = (pedido) =>
  api("/pedidos", { method: "POST", body: JSON.stringify(pedido) });