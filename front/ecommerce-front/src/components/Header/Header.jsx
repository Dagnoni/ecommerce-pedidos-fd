import { NavLink } from "react-router-dom";
import { ROTAS } from "../../routes/paths";
import "./Header.css";

const menuPrincipal = [
  { texto: "Início", rota: ROTAS.home },
  { texto: "Catálogo", rota: ROTAS.catalogo },
  { texto: "Carrinho", rota: ROTAS.carrinho },
  { texto: "Pagamento", rota: ROTAS.pagamento },
];

const menuConta = [
  { texto: "Cadastro", rota: ROTAS.cadastro },
  { texto: "Entrar", rota: ROTAS.login },
  { texto: "Acesso admin", rota: ROTAS.admin },
];

function classeDoLink({ isActive }) {
  return isActive ? "header__link header__link--ativo" : "header__link";
}

export default function Header() {
  return (
    <header className="header">
      <NavLink to={ROTAS.home} className="header__logo">EletroShop</NavLink>

      <nav className="header__menu">
        {menuPrincipal.map((item) => (
          <NavLink key={item.rota} to={item.rota} end className={classeDoLink}>
            {item.texto}
          </NavLink>
        ))}
      </nav>

      <nav className="header__menu header__menu--conta">
        {menuConta.map((item) => (
          <NavLink key={item.rota} to={item.rota} className={classeDoLink}>
            {item.texto}
          </NavLink>
        ))}
      </nav>
    </header>
  );
}