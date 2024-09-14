package projeto.banco.model.transacao;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

public class RegistroTransacao {
	private Integer numero;
	private long contaOrigem;
	private long contaDestino;
	private LocalDate dataTransacao;
	private BigDecimal valorTransacao;

	public RegistroTransacao(long contaOrigem, long contaDestino, BigDecimal valorTransacao) {
		super();
		this.numero = new Random().nextInt(99999999);
		this.contaOrigem = contaOrigem;
		this.contaDestino = contaDestino;
		this.dataTransacao = LocalDate.now();
		this.valorTransacao = valorTransacao;
	}

	public RegistroTransacao(Integer numero, long contaOrigem, long contaDestino, Date dataTransacao2,
			BigDecimal valorTransacao) {
		super();
		this.numero = new Random().nextInt(99999999);
		this.contaOrigem = contaOrigem;
		this.contaDestino = contaDestino;
		this.dataTransacao = LocalDate.now();
		this.valorTransacao = valorTransacao;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public long getContaOrigem() {
		return contaOrigem;
	}

	public void setContaOrigem(long contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public long getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(long contaDestino) {
		this.contaDestino = contaDestino;
	}

	public LocalDate getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(LocalDate dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	public BigDecimal getValorTransacao() {
		return valorTransacao;
	}

	public void setValorTransacao(BigDecimal valorTransacao) {
		this.valorTransacao = valorTransacao;
	}

}
