const [form, setForm] = useState({ nome: "", sobrenome: "", cpf: "" /* ... */ });

function alterarCampo(evento) {
  setForm({ ...form, [evento.target.name]: evento.target.value });
}
// no input: <input name="nome" value={form.nome} onChange={alterarCampo} />