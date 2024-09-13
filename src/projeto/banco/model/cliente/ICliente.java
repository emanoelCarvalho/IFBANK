package projeto.banco.model.cliente;

import java.math.BigDecimal;
import java.util.List;

import projeto.banco.model.conta.IConta;

public interface ICliente {
	public String getCpf();

	public void setCpf(String cpf);

	public String getNome();

	public void setNome(String nome);

	public List<IConta> getContas();

	public void carregarContas();

	public void adicionarConta(IConta conta);

	public void removerConta(IConta conta);

	public BigDecimal balancoEntreContas();

}
