package projeto.banco.model.transacao;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

import projeto.banco.model.transacao.enumarator.TipoTransacao;

public class RegistroTransacao {
	private Integer numero;
	private Integer contaOrigem;
	private Integer contaDestino;
	private LocalDate dataTransacao;
	private BigDecimal valorTransacao;
	private TipoTransacao tipoTransacao;

	public RegistroTransacao(Integer contaOrigem, Integer contaDestino, BigDecimal valorTransacao,
			TipoTransacao tipoTransacao) {
		super();
		this.numero = new Random().nextInt(99999999);
		this.contaOrigem = contaOrigem;
		this.contaDestino = contaDestino;
		this.dataTransacao = LocalDate.now();
		this.valorTransacao = valorTransacao;
		this.tipoTransacao = tipoTransacao;
	}

	public TipoTransacao getTipoTransacao() {
		return tipoTransacao;
	}

	public void setTipoTransacao(TipoTransacao tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}

	public RegistroTransacao(Integer numero, Integer contaOrigem, Integer contaDestino, Date dataTransacao2,
			BigDecimal valorTransacao, TipoTransacao tipoTransacao) {
		super();
		this.numero = new Random().nextInt(99999999);
		this.contaOrigem = contaOrigem;
		this.contaDestino = contaDestino;
		this.dataTransacao = LocalDate.now();
		this.valorTransacao = valorTransacao;
		this.tipoTransacao = tipoTransacao;

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

	public void setContaOrigem(Integer contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public long getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(Integer contaDestino) {
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
