package projeto.banco.model.cliente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import projeto.banco.dao.ContaDAO;
import projeto.banco.database.ConexaoMySql;
import projeto.banco.model.conta.IConta;

public class Cliente implements ICliente {

	private String cpf;

	private String nome;

	private List<IConta> contas;

	public Cliente(String cpf) {
		this.cpf = cpf;
	}

	public Cliente(String cpf, String nome) {
		this.cpf = cpf;
		this.nome = nome;

		this.contas = new ArrayList<>();
		carregarContas();
	}

	@Override
	public String getCpf() {
		// TODO Auto-generated method stub
		return this.cpf;
	}

	@Override
	public void setCpf(String cpf) {
		// TODO Auto-generated method stub
		this.cpf = cpf;
	}

	@Override
	public String getNome() {
		// TODO Auto-generated method stub
		return this.nome;
	}

	@Override
	public void setNome(String nome) {
		// TODO Auto-generated method stub
		this.nome = nome;

	}

	@Override
	public List<IConta> getContas() {
		// TODO Auto-generated method stub
		return this.contas;
	}

	@Override
	public String toString() {
		return "Cliente [cpf=" + cpf + ", nome=" + nome + ", contas=" + contas + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(nome, other.nome);
	}

	@Override
	public void carregarContas() {
		// TODO Auto-generated method stub
		ContaDAO listaDeContas = new ContaDAO(new ConexaoMySql());
		this.contas = listaDeContas.resgatarContas(this.cpf);

	}

	@Override
	public void adicionarConta(IConta conta) {
		ContaDAO contaDao = new ContaDAO(new ConexaoMySql());
		contaDao.adicionarConta(conta, this.cpf);
	}

	@Override
	public void removerConta(IConta conta) {
		ContaDAO contaDao = new ContaDAO(new ConexaoMySql());
		contaDao.removerConta(conta, this);
	}

	@Override
	public BigDecimal balancoEntreContas() {
		BigDecimal saldoTotal = BigDecimal.ZERO;
		List<IConta> contas = this.getContas();

		for (IConta conta : contas) {
			saldoTotal = saldoTotal.add(conta.getSaldo());
		}
		return saldoTotal;
	}

}
