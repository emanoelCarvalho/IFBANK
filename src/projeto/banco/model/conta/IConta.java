package projeto.banco.model.conta;

import java.math.BigDecimal;
import java.util.List;

import projeto.banco.model.transacao.RegistroTransacao;

public interface IConta {
	Integer getNumero();

	void setNumero(int numero);

	String getTipo();

	void setTipo(String tipo);

	BigDecimal getSaldo();

	void setSaldo(BigDecimal saldo);

	String getCpfTitular();

	void setCpfTitular(String cpf);

	Boolean getStatus();

	void setStatus(Boolean status);

	void setDetalhesConta(Integer numero, String tipo, BigDecimal saldo, String cpfTitular, Boolean status);

	Boolean depositar(BigDecimal quantia);

	Boolean sacar(BigDecimal quantia);

	Boolean transferir(BigDecimal quantia, int contaDestino);

	List<RegistroTransacao> emitirExtrato(int numeroConta, int mes, int ano);
}
