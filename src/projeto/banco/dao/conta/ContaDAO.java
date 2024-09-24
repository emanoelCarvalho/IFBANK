package projeto.banco.dao.conta;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import projeto.banco.database.ConexaoMySql;
import projeto.banco.model.cliente.ICliente;
import projeto.banco.model.conta.ContaCorrente;
import projeto.banco.model.conta.ContaPoupanca;
import projeto.banco.model.conta.IConta;
import projeto.banco.model.transacao.RegistroTransacao;
import projeto.banco.model.transacao.enumarator.TipoTransacao;
import projeto.banco.utils.TaxaUtils;

public class ContaDAO implements IContaDAO {
	private ConexaoMySql conn;

	public ContaDAO(ConexaoMySql conn) {
		this.conn = conn;
	}

	@Override
	public void adicionarConta(IConta conta, String cpf) {
		List<IConta> contasExistentes = resgatarContas(cpf);

		for (IConta contaExistente : contasExistentes) {
			if (contaExistente.getTipo().equals(conta.getTipo())) {
				JOptionPane.showMessageDialog(null, "O usuário já possui uma conta deste tipo.", "Erro",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		String sql = "INSERT INTO CONTAS (NUMERO, TIPO, SALDO, STATUS, CPF_CLIENTE) VALUES (?,?,?,?,?)";
		PreparedStatement ppst = null;

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setLong(1, conta.getNumero());
			ppst.setString(2, conta.getTipo());
			ppst.setBigDecimal(3, conta.getSaldo());
			ppst.setBoolean(4, conta.getStatus());
			ppst.setString(5, cpf);

			ppst.executeUpdate();
			JOptionPane.showMessageDialog(null, "Conta adicionada com sucesso!", "Sucesso",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Erro no servidor: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			if (ppst != null) {
				try {
					ppst.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PrepareStatement: " + e2.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);

				}
			}
			this.conn.closeConnection();
		}

	}

	@Override
	public List<IConta> removerConta(IConta conta, ICliente cliente) {
		String sql = "DELETE FROM CONTAS WHERE NUMERO = ?";
		PreparedStatement ppst = null;
		List<IConta> contas = new ArrayList<>();

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setLong(1, conta.getNumero());
			ppst.executeUpdate();

			if (cliente != null) {
				JOptionPane.showMessageDialog(null, "Conta removida com sucesso!", "Sucesso",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Erro no servidor: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			if (ppst != null) {
				try {
					ppst.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PrepareStatement: " + e2.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);

				}
			}
			this.conn.closeConnection();
		}
		return contas;
	}

	@Override
	public boolean buscarContaNumero(int numeroConta) {
		String sql = "SELECT COUNT(*) FROM CONTAS WHERE NUMERO = ?";
		PreparedStatement ppst = null;
		ResultSet rs = null;

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setInt(1, numeroConta);
			rs = ppst.executeQuery();
			rs.next();
			int contador = rs.getInt(1);
			return contador > 0;
		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Erro no servidor: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o ResultSet: " + e2.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);

				}
			}

			if (ppst != null) {
				try {
					ppst.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PrepareStatement: " + e2.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);

				}
			}
			this.conn.closeConnection();
		}
		return false;
	}

	@Override
	public void apagarCliente(String cpf) {
		List<IConta> contasUsuario = resgatarContas(cpf);

		for (IConta conta : contasUsuario) {
			removerConta(conta, null);
		}

		String sqlDeletarContas = "DELETE FROM CONTAS WHERE CPF_CLIENTE = ?";
		String sqlDeletarCliente = "DELETE FROM CLIENTES WHERE CPF = ?";

		PreparedStatement ppstCt = null;
		PreparedStatement ppstCl = null;

		try {
			ppstCt = this.conn.getConnection().prepareStatement(sqlDeletarContas);
			ppstCt.setString(1, cpf);
			ppstCt.executeUpdate();

			ppstCl = this.conn.getConnection().prepareStatement(sqlDeletarCliente);
			ppstCl.setString(1, cpf);
			int linhasApagadas = ppstCl.executeUpdate();

			if (linhasApagadas > 0) {
				JOptionPane.showMessageDialog(null, "Usuário removido com sucesso!", "Sucesso",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "O usuário não foi encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
			}

		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Erro no servidor: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			if (ppstCt != null) {
				try {
					ppstCt.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e2.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
				}
			}

			if (ppstCl != null) {
				try {
					ppstCl.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e2.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
			this.conn.closeConnection();
		}
	}

	@Override
	public Boolean depositarConta(int numeroConta, BigDecimal quantia) {
		String sql = "UPDATE CONTAS SET SALDO = SALDO + ? WHERE NUMERO = ?";
		String sqlTransacao = "INSERT INTO REGISTROS_TRANSACOES (NUMERO, NUMERO_CONTA_ORIGEM, NUMERO_CONTA_DESTINO, DATA_TRANSACAO, VALOR_TRANSACAO, TIPO_TRANSACAO) VALUES (?,?,?,?,?,?)";

		PreparedStatement ppst = null;
		PreparedStatement ppstTransacao = null;

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setBigDecimal(1, quantia);
			ppst.setLong(2, numeroConta);
			ppst.executeUpdate();

			RegistroTransacao transacao = new RegistroTransacao(numeroConta, numeroConta, quantia,
					TipoTransacao.CREDITO);
			ppstTransacao = this.conn.getConnection().prepareStatement(sqlTransacao);
			ppstTransacao.setInt(1, transacao.getNumero());
			ppstTransacao.setLong(2, transacao.getContaOrigem());
			ppstTransacao.setLong(3, transacao.getContaDestino());
			ppstTransacao.setDate(4, java.sql.Date.valueOf(transacao.getDataTransacao()));
			ppstTransacao.setBigDecimal(5, transacao.getValorTransacao());
			ppstTransacao.setInt(6, transacao.getTipoTransacao().getValor());
			ppstTransacao.executeUpdate();

			JOptionPane.showMessageDialog(null, "Depósito realizado com sucesso!", "Sucesso",
					JOptionPane.INFORMATION_MESSAGE);
			return true;
		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Erro no servidor: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} finally {
			if (ppst != null) {
				try {
					ppst.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e2.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
			this.conn.closeConnection();
		}
	}

	@Override
	public Boolean sacar(int numeroConta, BigDecimal quantia) {
		String sql = "UPDATE CONTAS SET SALDO = SALDO - ? WHERE NUMERO = ?";
		String sqlTransacao = "INSERT INTO REGISTROS_TRANSACOES (NUMERO, NUMERO_CONTA_ORIGEM, NUMERO_CONTA_DESTINO, DATA_TRANSACAO, VALOR_TRANSACAO, TIPO_TRANSACAO) VALUES (?,?,?,?,?,?)";

		PreparedStatement ppst = null;
		PreparedStatement ppstTransacao = null;

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setBigDecimal(1, quantia);
			ppst.setLong(2, numeroConta);
			ppst.executeUpdate();

			RegistroTransacao transacao = new RegistroTransacao(numeroConta, numeroConta, quantia,
					TipoTransacao.DEBITO);
			ppstTransacao = this.conn.getConnection().prepareStatement(sqlTransacao);
			ppstTransacao.setInt(1, transacao.getNumero());
			ppstTransacao.setLong(2, transacao.getContaOrigem());
			ppstTransacao.setLong(3, transacao.getContaDestino());
			ppstTransacao.setDate(4, java.sql.Date.valueOf(transacao.getDataTransacao()));
			ppstTransacao.setBigDecimal(5, transacao.getValorTransacao());
			ppstTransacao.setInt(6, transacao.getTipoTransacao().getValor());
			ppstTransacao.executeUpdate();

			JOptionPane.showMessageDialog(null, "Saque realizado com sucesso!", "Sucesso",
					JOptionPane.INFORMATION_MESSAGE);
			return true;
		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Erro no servidor: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} finally {
			if (ppst != null) {
				try {
					ppst.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e2.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
			this.conn.closeConnection();
		}

	}

	@Override
	public void transferir(BigDecimal quantia, int contaDestino, int contaOrigem) {
		String contaDebitaSql = "UPDATE CONTAS SET SALDO = SALDO - ? WHERE NUMERO = ?";
		String contaDepositoSql = "UPDATE CONTAS SET SALDO = SALDO + ? WHERE NUMERO = ?";
		String registroTransacaoSql = "INSERT INTO REGISTROS_TRANSACOES (NUMERO, NUMERO_CONTA_ORIGEM, NUMERO_CONTA_DESTINO, DATA_TRANSACAO, VALOR_TRANSACAO, TIPO_TRANSACAO) VALUES (?,?,?,?,?,?)";

		PreparedStatement ppstContaDebita = null;
		PreparedStatement ppstContaDeposito = null;
		PreparedStatement ppstTransacao = null;

		try {
			ContaDAO contaDAO = new ContaDAO(this.conn);

			String tipoContaOrigem = contaDAO.getTipo(contaOrigem);
			String tipoContaDestino = contaDAO.getTipo(contaDestino);

			if (tipoContaOrigem == null || tipoContaDestino == null) {
				JOptionPane.showMessageDialog(null, "Uma ou ambas as contas não foram encontradas.", "Erro",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			BigDecimal taxaADM = new BigDecimal(TaxaUtils.TAXA_ADMINISTRATIVA);
			BigDecimal quantiaTaxada = quantia;

			if (!tipoContaOrigem.equals(tipoContaDestino)) {
				quantiaTaxada = quantia.multiply(BigDecimal.ONE.subtract(taxaADM));
				JOptionPane.showMessageDialog(null, "Uma taxa administrativa de 2% foi aplicada à transferência.",
						"Informativo", JOptionPane.INFORMATION_MESSAGE);
			}

			ppstContaDebita = this.conn.getConnection().prepareStatement(contaDebitaSql);
			ppstContaDebita.setBigDecimal(1, quantia);
			ppstContaDebita.setLong(2, contaOrigem);
			ppstContaDebita.executeUpdate();

			ppstContaDeposito = this.conn.getConnection().prepareStatement(contaDepositoSql);
			ppstContaDeposito.setBigDecimal(1, quantiaTaxada);
			ppstContaDeposito.setLong(2, contaDestino);
			ppstContaDeposito.executeUpdate();

			RegistroTransacao transacaoOrigem = new RegistroTransacao(contaOrigem, contaDestino, quantia,
					TipoTransacao.TRANSACAO_DEBITO);
			ppstTransacao = this.conn.getConnection().prepareStatement(registroTransacaoSql);
			ppstTransacao.setInt(1, transacaoOrigem.getNumero());
			ppstTransacao.setLong(2, transacaoOrigem.getContaOrigem());
			ppstTransacao.setLong(3, transacaoOrigem.getContaDestino());
			ppstTransacao.setDate(4, java.sql.Date.valueOf(transacaoOrigem.getDataTransacao()));
			ppstTransacao.setBigDecimal(5, transacaoOrigem.getValorTransacao());
			ppstTransacao.setInt(6, transacaoOrigem.getTipoTransacao().getValor());
			ppstTransacao.executeUpdate();

			RegistroTransacao transacaoDestino = new RegistroTransacao(contaDestino, contaOrigem, quantiaTaxada,
					TipoTransacao.TRANSACAO_CREDITO);
			ppstTransacao.setInt(1, transacaoDestino.getNumero());
			ppstTransacao.setLong(2, transacaoDestino.getContaOrigem());
			ppstTransacao.setLong(3, transacaoDestino.getContaDestino());
			ppstTransacao.setDate(4, java.sql.Date.valueOf(transacaoDestino.getDataTransacao()));
			ppstTransacao.setBigDecimal(5, transacaoDestino.getValorTransacao());
			ppstTransacao.setInt(6, transacaoDestino.getTipoTransacao().getValor());
			ppstTransacao.executeUpdate();

			JOptionPane.showMessageDialog(null, "Transferência realizada com sucesso!", "Sucesso",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro no servidor: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			try {
				this.conn.getConnection().rollback();
			} catch (SQLException rollbackException) {
				JOptionPane.showMessageDialog(null, "Erro ao executar rollback: " + rollbackException.getMessage(),
						"Erro", JOptionPane.ERROR_MESSAGE);
			}
		} finally {
			if (ppstContaDebita != null) {
				try {
					ppstContaDebita.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			if (ppstContaDeposito != null) {
				try {
					ppstContaDeposito.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			if (ppstTransacao != null) {
				try {
					ppstTransacao.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			if (ppstTransacao != null) {
				try {
					ppstTransacao.close();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			this.conn.closeConnection();
		}
	}

	@Override
	public List<IConta> resgatarContas(String cpf) {
		String sql = "SELECT * FROM CONTAS WHERE CPF_CLIENTE = ?";
		PreparedStatement ppst = null;
		ResultSet rs = null;
		List<IConta> contas = new ArrayList<>();

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setString(1, cpf);
			rs = ppst.executeQuery();

			while (rs.next()) {
				int numero = rs.getInt("NUMERO");
				String tipo = rs.getString("TIPO");
				BigDecimal saldo = rs.getBigDecimal("SALDO");
				Boolean status = rs.getBoolean("STATUS");
				String cpfTitular = rs.getString("CPF_CLIENTE");

				if (rs.getString("TIPO").equals("poupanca")) {
					IConta novaContaSalva = new ContaPoupanca(cpfTitular, "poupanca");
					novaContaSalva.setDetalhesConta(numero, tipo, saldo, cpfTitular, status);
					contas.add(novaContaSalva);
				}

				if (rs.getString("TIPO").equals("corrente")) {
					IConta novaContaSalva = new ContaCorrente(cpfTitular, "corrente");
					novaContaSalva.setDetalhesConta(numero, tipo, saldo, cpfTitular, status);
					contas.add(novaContaSalva);
				}

			}

		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Erro no servidor: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o ResultSet: " + e2.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);

				}
			}

			if (ppst != null) {
				try {
					ppst.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PrepareStatement: " + e2.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);

				}
			}

		}
		return contas;
	}

	@Override
	public boolean balancoEntreContas(String cpf) {
		List<IConta> contas = resgatarContas(cpf);

		for (IConta conta : contas) {
			if (conta.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<RegistroTransacao> emitirExtrato(int numeroConta, int mes, int ano) {
		List<RegistroTransacao> extrato = new ArrayList<>();
		String sql = "SELECT * FROM REGISTROS_TRANSACOES " + "WHERE NUMERO_CONTA_ORIGEM = ? "
				+ "AND YEAR(DATA_TRANSACAO) = ? AND MONTH(DATA_TRANSACAO) = ?";

		try {
			PreparedStatement ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setInt(1, numeroConta);
			ppst.setInt(2, ano);
			ppst.setInt(3, mes);

			ResultSet rs = ppst.executeQuery();
			while (rs.next()) {
				int numero = rs.getInt("NUMERO");
				int numeroContaOrigem = rs.getInt("NUMERO_CONTA_ORIGEM");
				int numeroContaDestino = rs.getInt("NUMERO_CONTA_DESTINO");
				Date dataTransacao = rs.getDate("DATA_TRANSACAO");
				BigDecimal valorTransacao = rs.getBigDecimal("VALOR_TRANSACAO");
				String tipoTransacaoStr = rs.getString("TIPO_TRANSACAO");
				TipoTransacao tipoTransacao = TipoTransacao.valueOf(tipoTransacaoStr);

				RegistroTransacao transacao = new RegistroTransacao(numero, numeroContaOrigem, numeroContaDestino,
						dataTransacao, valorTransacao, tipoTransacao);
				extrato.add(transacao);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return extrato;
	}

	@Override
	public String getTipo(int numeroConta) {
		String tipoConta = null;
		String sql = "SELECT TIPO FROM CONTAS WHERE NUMERO = ?";

		try (PreparedStatement pst = this.conn.getConnection().prepareStatement(sql)) {
			pst.setInt(1, numeroConta);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				tipoConta = rs.getString("TIPO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tipoConta;
	}
}
