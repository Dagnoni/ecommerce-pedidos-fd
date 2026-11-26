import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { listarProdutos } from "../services/produtoService";
import ProdutoCard from "../components/ProdutoCard/ProdutoCard";
import "./Catalogo.css";

export default function Catalogo() {
  const [produtos, setProdutos] = useState([]);
  const [params, setParams] = useSearchParams();
  const categoriaAtiva = params.get("categoria"); // vem do link: /catalogo?categoria=Celulares

  useEffect(() => {
    listarProdutos().then(setProdutos);
  }, []);

  // as categorias nascem dos próprios produtos: produto novo = categoria nova
  const categorias = [...new Set(produtos.map((p) => p.categoria))];

  const produtosFiltrados = categoriaAtiva
    ? produtos.filter((p) => p.categoria === categoriaAtiva)
    : produtos;

  return (
    <section className="catalogo">
      <aside className="catalogo__filtros">
        <h2>Categorias</h2>
        <button onClick={() => setParams({})}>Todas</button>
        {categorias.map((categoria) => (
          <button key={categoria} onClick={() => setParams({ categoria })}>
            {categoria}
          </button>
        ))}
      </aside>

      <div>
        <h1>Catálogo</h1>
        <p>{produtosFiltrados.length} produtos disponíveis</p>

        <div className="catalogo__grade">
          {produtosFiltrados.map((produto) => (
            <ProdutoCard key={produto.id} produto={produto} />
          ))}
        </div>
      </div>
    </section>
  );
}