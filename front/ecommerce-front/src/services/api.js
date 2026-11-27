const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

export async function api(rota, opcoes = {}) {
  const resposta = await fetch(`${API_URL}${rota}`, {
    headers: { "Content-Type": "application/json" },
    ...opcoes,
  });

  if (!resposta.ok) {
    throw new Error(`Erro ${resposta.status} em ${rota}`);
  }
  return resposta.json();
}