import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { ROTAS } from "../routes/paths";
import { buscarProduto } from "../services/produtoService";
import { useCarrinho } from "../context/CarrinhoContext";
import { formatarPreco } from "../utils/formatarPreco";
import Button from "../components/Button/Button";
import "./Produto.css";

export default function Produto() {
  const { id } = useParams();            // o :id da rota
  const navigate = useNavigate();
  const { adicionar } = useCarrinho();

  const [produto, setProduto] = useState(null);
  const [carregando, setCarregando] = useState(true);
  const [cor, setCor] = useState("");
  const [armazenamento, setArmazenamento] = useState("");
  const [secaoAberta, setSecaoAberta] = useState("detalhes");

  useEffect(() => {
    setCarregando(true);
    buscarProduto(id)
      .then((dados) => {
        setProduto(dados ?? null);
        setCor(dados?.cores?.[0] ?? "");                   // começa na 1ª opção
        setArmazenamento(dados?.armazenamentos?.[0] ?? "");
      })
      .catch(() => setProduto(null))
      .finally(() => setCarregando(false));
  }, [id]);

  if (carregando) return <p>Carregando...</p>;

  if (!produto) {
    return (
      <p>
        Produto não encontrado. <Link to={ROTAS.catalogo}>Voltar ao catálogo</Link>
      </p>
    );
  }

  function aoAdicionar() {
    adicionar(produto, { cor, armazenamento });
    navigate(ROTAS.carrinho);              // se não quiser ir ao carrinho, apague esta linha
  }

  // seções que abrem e fecham: texto de exemplo, depois pode vir do backend
  const secoes = [
    { id: "detalhes", titulo: "Detalhes do produto", texto: produto.descricao },
    { id: "especificacoes", titulo: "Especificações técnicas", texto: produto.especificacoes ?? "Em breve." },
    { id: "entrega", titulo: "Entrega e suporte", texto: "Frete calculado no carrinho. Garantia de 12 meses." },
  ];

  return (
    <section className="produto">
      <div className="produto__imagem">
        {produto.imagem && <img src={produto.imagem} alt={produto.nome} />}
      </div>

      <div className="produto__info">
        <h1>{produto.nome}</h1>

        <span className="produto__selo">
          {[armazenamento, cor].filter(Boolean).join(" · ")}
        </span>

        <strong className="produto__preco">{formatarPreco(produto.preco)}</strong>
        <p className="produto__descricao">{produto.descricao}</p>

        <div className="produto__opcoes">
          {produto.cores.length > 0 && (
            <div className="campo">
              <label htmlFor="cor">Cor</label>
              <select id="cor" value={cor} onChange={(e) => setCor(e.target.value)}>
                {produto.cores.map((opcao) => (
                  <option key={opcao}>{opcao}</option>
                ))}
              </select>
            </div>
          )}

          {produto.armazenamentos.length > 0 && (
            <div className="campo">
              <label htmlFor="armazenamento">Armazenamento</label>
              <select id="armazenamento" value={armazenamento} onChange={(e) => setArmazenamento(e.target.value)}>
                {produto.armazenamentos.map((opcao) => (
                  <option key={opcao}>{opcao}</option>
                ))}
              </select>
            </div>
          )}
        </div>

        <Button onClick={aoAdicionar}>Adicionar ao carrinho</Button>

        <div className="produto__secoes">
          {secoes.map((secao) => (
            <div key={secao.id} className="produto__secao">
              <button
                className="produto__secao-titulo"
                onClick={() => setSecaoAberta(secaoAberta === secao.id ? null : secao.id)}
              >
                {secao.titulo}
              </button>
              {secaoAberta === secao.id && <p>{secao.texto}</p>}
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}