import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { ROTAS, rotaProduto } from "../routes/paths";
import { listarProdutos } from "../services/produtoService";
import ProdutoCard from "../components/ProdutoCard/ProdutoCard";
import heroImagem from "../assets/hero.png";
import "./Home.css";

export default function Home() {
  const [destaques, setDestaques] = useState([]);
  const [categorias, setCategorias] = useState([]);

  useEffect(() => {
    listarProdutos().then((lista) => {
      setDestaques(lista.slice(0, 3));                       // 3 primeiros
      setCategorias([...new Set(lista.map((p) => p.categoria))]);
    });
  }, []);

  const primeiroProduto = destaques[0];

  return (
    <>
      <section className="hero">
        <div className="hero__texto">
          <span className="hero__selo">Tecnologia no seu dia a dia</span>
          <h1>Seu próximo upgrade começa aqui.</h1>
          <p>Explore celulares, notebooks e acessórios. Compare os modelos e encontre a tecnologia que combina com você.</p>

          <div className="hero__botoes">
            <Link to={ROTAS.catalogo} className="botao botao--primario">
              Explorar catálogo
            </Link>
            {primeiroProduto && (
              <Link to={rotaProduto(primeiroProduto.id)} className="botao botao--secundario">
                Ver produto
              </Link>
            )}
          </div>

          <div className="hero__categorias">
            {categorias.map((categoria) => (
              <Link key={categoria} to={`${ROTAS.catalogo}?categoria=${categoria}`} className="hero__chip">
                {categoria}
              </Link>
            ))}
          </div>
        </div>

        <img className="hero__imagem" src={heroImagem} alt="Notebook e acessórios" />
      </section>

      <section className="home__destaques">
        <h2>Explore o catálogo</h2>
        <p>Comece alguns dos produtos disponíveis na loja.</p>

        <div className="home__grade">
          {destaques.map((produto) => (
            <ProdutoCard key={produto.id} produto={produto} />
          ))}
        </div>
      </section>
    </>
  );
}