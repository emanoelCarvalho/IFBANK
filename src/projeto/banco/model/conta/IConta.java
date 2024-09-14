package projeto.banco.model.conta;

import java.math.BigDecimal;
import java.util.List;

import projeto.banco.model.transacao.RegistroTransacao;

public interface IConta {
	public Integer getNumero();

	public void setNumero(int numero);

	public String getTipo();

	public void setTipo(String tipo);

	public BigDecimal getSaldo();

	public void setSaldo(BigDecimal saldo);

	public String getCpfTitular();

	public void setCpfTitular(String cpf);

	public Boolean getStatus();

	public void setStatus(Boolean status);

	public void setDetalhesConta(Integer numero, String tipo, BigDecimal saldo, String cpfTitular, Boolean status);

	public Boolean depositar(BigDecimal quantia);

	public Boolean sacar(BigDecimal quantia);

	public Boolean transferir(BigDecimal quantia, int contaDestino);

	public List<RegistroTransacao> emitirExtrato(int numeroConta, int mes, int ano);
}
