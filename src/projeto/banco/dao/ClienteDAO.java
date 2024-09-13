package projeto.banco.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import projeto.banco.database.ConexaoMySql;
import projeto.banco.exception.CpfExistenteEx;
import projeto.banco.exception.CpfInvalidoEx;
import projeto.banco.model.cliente.Cliente;
import projeto.banco.model.cliente.ICliente;

public class ClienteDAO {
	private ConexaoMySql conn;

	public ClienteDAO(ConexaoMySql conn) {
		this.conn = conn;
	}

	public Boolean adicionarCliente(ICliente cliente) {
		String sql = "INSERT INTO CLIENTES (CPF, NOME_COMPLETO) VALUES (?,?)";
		PreparedStatement ppst = null;

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setString(1, cliente.getCpf());
			ppst.setString(2, cliente.getNome());

			ppst.executeUpdate();
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException("Erro no servidor: " + e.getMessage(), e);
		} finally {
			if (ppst != null) {
				try {
					ppst.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar ppst: " + e2.getMessage());
				}
			}
		}
		return false;
	}

	public ICliente buscarClientePorCpf(String cpf) {
		String sql = "SELECT * FROM CLIENTES WHERE CPF = ?";
		PreparedStatement ppst = null;
		ResultSet rs = null;
		ICliente dados = null;

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setString(1, cpf);
			rs = ppst.executeQuery();

			if (rs.next()) {
				String cpfCliente = rs.getString("CPF");
				String nomeCliente = rs.getString("NOME_COMPLETO");

				dados = new Cliente(cpfCliente, nomeCliente);
				dados.carregarContas();
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
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
				} catch (Exception e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e2.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		return dados;
	}

	public Boolean verificarCpf(String cpf) throws CpfExistenteEx {
		String sql = "SELECT CPF FROM CLIENTES WHERE CPF = ?";
		PreparedStatement ppst = null;
		ResultSet rs = null;

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setString(1, cpf);
			rs = ppst.executeQuery();

			if (rs.next()) {
				String cpfExistente = rs.getString("CPF");

				if (cpfExistente.equals(cpf)) {
					throw new CpfExistenteEx();
				}
			} else {
				return true;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException("Erro no servidor: " + e.getMessage(), e);
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
				} catch (Exception e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e2.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		return true;
	}

	public ICliente logarConta(String cpf) {
		String sql = "SELECT * FROM CLIENTES WHERE CPF = ?";
		PreparedStatement ppst = null;
		ResultSet rs = null;

		try {
			ppst = conn.getConnection().prepareStatement(sql);
			ppst.setString(1, cpf);

			rs = ppst.executeQuery();

			if (rs.next()) {
				String cpfRecuperado = rs.getString("CPF");
				String nomeRecuperado = rs.getString("NOME_COMPLETO");

				if (!cpfRecuperado.equals(cpf)) {
					throw new CpfInvalidoEx();
				}

				ICliente cliente = new Cliente(cpfRecuperado, nomeRecuperado);
				return cliente;
			}
			throw new CpfInvalidoEx();
		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Erro no servidor: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			return null;
		} catch (CpfInvalidoEx e) {
			// TODO: handle exception
			e.alert();
			return null;
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
				} catch (Exception e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + e2.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public List<String> listarClientes() {
		List<String> listaClientes = new ArrayList<>();

		String sql = "SELECT * FROM CLIENTES";
		PreparedStatement ppst = null;
		ResultSet rs = null;

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			rs = ppst.executeQuery();

			while (rs.next()) {
				String cpfCliente = rs.getString("CPF");
				String dadosCliente = "CPF: " + cpfCliente + ". ";
				listaClientes.add(dadosCliente);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ppst != null) {
				try {
					ppst.close();
				} catch (SQLException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Erro ao fechar ppst: " + e2.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
					// TODO: handle exception
				}
			}
		}

		return listaClientes;
	}

	public List<String> getDadosClientes(String cpf) {
		List<String> dadosClient = new ArrayList<>();

		String sql = "SELECT * FROM CLIENTES WHERE CPF = ?";
		PreparedStatement ppst = null;
		ResultSet rs = null;

		try {
			ppst = this.conn.getConnection().prepareStatement(sql);
			ppst.setString(1, cpf);
			rs = ppst.executeQuery();

			while (rs.next()) {
				String cpfCliente = rs.getString("CPF");
				String nomeCliente = rs.getString("NOME_COMPLETO");

				String dados = "Cpf: " + cpfCliente + " nome: " + nomeCliente;
				dadosClient.add(dados);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.getStackTrace();
		}
		return null;
	}
}
