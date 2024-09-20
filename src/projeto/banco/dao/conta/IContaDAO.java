package projeto.banco.dao.conta;

import java.math.BigDecimal;
import java.util.List;

import projeto.banco.model.cliente.ICliente;
import projeto.banco.model.conta.IConta;
import projeto.banco.model.transacao.RegistroTransacao;

public interface IContaDAO {

	void adicionarConta(IConta conta, String cpf);

	List<IConta> removerConta(IConta conta, ICliente cliente);

	boolean buscarContaNumero(int numeroConta);

	void apagarCliente(String cpf);

	Boolean depositarConta(int numeroConta, BigDecimal quantia);

	Boolean sacar(int numeroConta, BigDecimal quantia);

	void transferir(BigDecimal quantia, int contaDestino, int contaOrigem);

	List<IConta> resgatarContas(String cpf);

	boolean balancoEntreContas(String cpf);

	List<RegistroTransacao> emitirExtrato(int numeroConta, int mes, int ano);

	String getTipo(int numeroConta);
}
