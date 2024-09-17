package projeto.banco.model.cliente;

import java.math.BigDecimal;
import java.util.List;

import projeto.banco.model.conta.IConta;

public interface ICliente {
	 String getCpf();

	 void setCpf(String cpf);

	 String getNome();

	 void setNome(String nome);

	 List<IConta> getContas();

	 void carregarContas();

	 void adicionarConta(IConta conta);

	 void removerConta(IConta conta);

	 BigDecimal balancoEntreContas();

}
