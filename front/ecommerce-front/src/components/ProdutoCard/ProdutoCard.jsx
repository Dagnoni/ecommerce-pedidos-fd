import { Link } from "react-router-dom";
import { rotaProduto } from "../../routes/paths";
import { useCarrinho } from "../../context/CarrinhoContext";
import { formatarPreco } from "../../utils/formatarPreco";
import Button from "../Button/Button";
import "./ProdutoCard.css";

export default function ProdutoCard({ produto }) {
  const { adicionar } = useCarrinho();

  return (
    <article className="produto-card">
      <Link to={rotaProduto(produto.id)} className="produto-card__link">
        <div className="produto-card__imagem">
          {produto.imagem && <img src={produto.imagem} alt={produto.nome} />}
        </div>
        <h3>{produto.nome}</h3>
        <p>{produto.descricaoCurta}</p>
        <strong>{formatarPreco(produto.preco)}</strong>
      </Link>

      <Button onClick={() => adicionar(produto)}>Adicionar ao carrinho</Button>
    </article>
  );
}