import { NavLink } from "react-router-dom";
import { ROTAS } from "../../routes/paths";
import styles from "./Header.module.css";

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
  return isActive ? `${styles.link} ${styles.linkAtivo}` : styles.link;
}

export default function Header() {
  return (
    <header className={styles.header}>
      <NavLink to={ROTAS.home} className={styles.logo}>EletroShop</NavLink>

      <nav className={styles.menu}>
        {menuPrincipal.map((item) => (
          <NavLink key={item.rota} to={item.rota} end className={classeDoLink}>
            {item.texto}
          </NavLink>
        ))}
      </nav>

      <nav className={`${styles.menu} ${styles.menuConta}`}>
        {menuConta.map((item) => (
          <NavLink key={item.rota} to={item.rota} className={classeDoLink}>
            {item.texto}
          </NavLink>
        ))}
      </nav>
    </header>
  );
}