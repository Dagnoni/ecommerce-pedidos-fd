import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { listarProdutos } from "../services/produtoService";
import { formatarPreco } from "../utils/formatarPreco";
import ProdutoCard from "../components/ProdutoCard/ProdutoCard";
import "./Catalogo.css";

export default function Catalogo() {
  const [produtos, setProdutos] = useState([]);
  const [ordem, setOrdem] = useState("padrao");
  const [precoLimite, setPrecoLimite] = useState(null);
  const [params, setParams] = useSearchParams();
  const categoriaAtiva = params.get("categoria");

  useEffect(() => {
    listarProdutos().then(setProdutos);
  }, []);

  // tudo abaixo é calculado a partir da lista: produto novo = tudo se ajusta
  const categorias = [...new Set(produtos.map((p) => p.categoria))];
  const precoMaximo = produtos.length ? Math.max(...produtos.map((p) => p.preco)) : 0;
  const limite = precoLimite ?? precoMaximo;

  let lista = produtos.filter((p) => p.preco <= limite);
  if (categoriaAtiva) {
    lista = lista.filter((p) => p.categoria === categoriaAtiva);
  }

  if (ordem === "menor") lista.sort((a, b) => a.preco - b.preco);
  if (ordem === "maior") lista.sort((a, b) => b.preco - a.preco);
  if (ordem === "nome") lista.sort((a, b) => a.nome.localeCompare(b.nome));

  return (
    <section className="catalogo">
      <aside className="catalogo__filtros cartao">
        <h2>Categorias</h2>

        <button
          className={!categoriaAtiva ? "catalogo__categoria catalogo__categoria--ativa" : "catalogo__categoria"}
          onClick={() => setParams({})}
        >
          Todas
        </button>

        {categorias.map((categoria) => (
          <button
            key={categoria}
            className={categoria === categoriaAtiva ? "catalogo__categoria catalogo__categoria--ativa" : "catalogo__categoria"}
            onClick={() => setParams({ categoria })}
          >
            {categoria}
          </button>
        ))}

        <h2>Faixa de preço</h2>
        <input
          type="range"
          min="0"
          max={precoMaximo}
          value={limite}
          onChange={(e) => setPrecoLimite(Number(e.target.value))}
        />
        <p className="catalogo__preco">Até {formatarPreco(limite)}</p>
      </aside>

      <div>
        <div className="catalogo__topo">
          <div>
            <h1>Catálogo</h1>
            <p>{lista.length} produtos disponíveis</p>
          </div>

          <select value={ordem} onChange={(e) => setOrdem(e.target.value)}>
            <option value="padrao">Ordenar</option>
            <option value="menor">Menor preço</option>
            <option value="maior">Maior preço</option>
            <option value="nome">Nome (A-Z)</option>
          </select>
        </div>

        <div className="catalogo__grade">
          {lista.map((produto) => (
            <ProdutoCard key={produto.id} produto={produto} />
          ))}
        </div>
      </div>
    </section>
  );
}