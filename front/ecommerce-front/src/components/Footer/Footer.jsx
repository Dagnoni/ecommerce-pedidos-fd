import "./Footer.css";

export default function Footer() {
  return (
    <footer className="footer">
      <p>© {new Date().getFullYear()} EletroShop. Todos os direitos reservados.</p>
    </footer>
  );
}