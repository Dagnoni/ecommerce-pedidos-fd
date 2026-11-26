import { Routes, Route } from "react-router-dom";
import { ROTAS } from "./routes/paths";
import Layout from "./components/Layout/Layout";
import Home from "./screens/Home";
import Catalogo from "./screens/Catalogo";
import Produto from "./screens/Produto";
import Carrinho from "./screens/Carrinho";
import Cadastro from "./screens/Cadastro";
import Login from "./screens/Login";
import Pagamento from "./screens/Pagamento";

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path={ROTAS.home} element={<Home />} />
        <Route path={ROTAS.catalogo} element={<Catalogo />} />
        <Route path={ROTAS.produto} element={<Produto />} />
        <Route path={ROTAS.carrinho} element={<Carrinho />} />
        <Route path={ROTAS.cadastro} element={<Cadastro />} />
        <Route path={ROTAS.login} element={<Login />} />
        <Route path={ROTAS.admin} element={<Login />} /> {/* provisório */}
        <Route path={ROTAS.pagamento} element={<Pagamento />} />
      </Route>
    </Routes>
  );
}