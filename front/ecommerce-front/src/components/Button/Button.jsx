export default function Button({ children, variante = "primario", tipo = "button", ...resto }) {
  return (
    <button type={tipo} className={`botao botao--${variante}`} {...resto}>
      {children}
    </button>
  );
}