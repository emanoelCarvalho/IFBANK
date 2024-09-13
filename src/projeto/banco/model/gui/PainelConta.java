package projeto.banco.model.gui;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import projeto.banco.dao.ContaDAO;
import projeto.banco.database.ConexaoMySql;
import projeto.banco.exception.ContaTemSaldoEx;
import projeto.banco.exception.SaldoNaContaEx;
import projeto.banco.exception.TransferirMesmaContaEX;
import projeto.banco.exception.ValorInvalidoEx;
import projeto.banco.model.cliente.Cliente;
import projeto.banco.model.cliente.ICliente;
import projeto.banco.model.conta.IConta;

public class PainelConta extends JFrame {

	private static final long serialVersionUID = 1L;

	ICliente cliente;
	JLabel alert;
	JLabel titulo;
	JLabel clienteNome;
	JLabel clienteCpf;
	JLabel balancoEntreContas;

	public PainelConta(ICliente cliente) {
		this.cliente = cliente;
		setTitle("Ações do banco");
		setSize(1000, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		this.titulo = (new JLabel("Bem vindo " + this.cliente.getNome()));
		this.titulo.setBounds(400, 50, 500, 30);

		this.clienteCpf = (new JLabel("Bem vindo " + this.cliente.getCpf()));
		this.clienteCpf.setBounds(400, 50, 500, 30);

		int numeroContas = this.cliente.getContas().size();

		if (numeroContas == 0 || numeroContas == 1) {
			JButton criarConta = new JButton("Criar conta");
			criarConta.setBounds(400, 400, 150, 30);
			criarConta.addActionListener(this::adicionarConta);

			if (numeroContas == 0) {
				this.alert = new JLabel("Você ainda não possui contas!");
				this.alert.setBounds(390, 300, 500, 30);

				add(alert);
				add(criarConta);
			}

			if (numeroContas == 1) {
				add(criarConta);
			}

			int labelX = 100;
			for (IConta conta : this.cliente.getContas()) {
				String saldoFormatado = formatarSaldo(conta.getSaldo());

				JLabel contaLabel = new JLabel("Número da conta: " + conta.getNumero() + " - Tipo: " + conta.getTipo()
						+ " - Saldo: R$" + saldoFormatado);
				contaLabel.setBounds(labelX, 120, 700, 30);
				add(contaLabel);

				JButton botaoDepositar = new JButton("Depósito");
				botaoDepositar.setBounds(labelX, 150, 100, 30);
				botaoDepositar.addActionListener(e -> depositarConta(conta));
				add(botaoDepositar);

				JButton botaoTransferir = new JButton("Efetuar transferência");
				botaoTransferir.setBounds(labelX + 110, 150, 160, 30);
				botaoTransferir.addActionListener(e -> transferir(conta));
				add(botaoTransferir);

				JButton botaoSaque = new JButton("Realizar saque");
				botaoSaque.setBounds(labelX, 190, 120, 30);
				botaoSaque.addActionListener(e -> sacarConta(conta));
				add(botaoSaque);

				JButton botaoRemover = new JButton("Remover conta");
				botaoRemover.setBounds(labelX + 130, 190, 130, 30);
				botaoRemover.addActionListener(e -> removerConta(conta));
				add(botaoRemover);

				labelX += 400;

				JButton botaoVerSaldoTotalContas = new JButton("Mostrar total dos saldos");
				botaoVerSaldoTotalContas.setBounds(380, 350, 200, 30);
				botaoVerSaldoTotalContas.addActionListener(this::exibirSaldoTotal);
				add(botaoVerSaldoTotalContas);

			}

			JButton sair = new JButton("Sair");
			sair.setBounds(20, 20, 100, 30);
			sair.addActionListener(this::sairConta);
			add(sair);

			JButton removeClienteButton = new JButton("Excluir conta");
			removeClienteButton.setBounds(20, 400, 150, 30);
			removeClienteButton.addActionListener(this::apagarCliente);
			add(removeClienteButton);

			setVisible(true);
			add(titulo);
			add(clienteCpf);
		}
	}

	private void adicionarConta(ActionEvent e) {
		dispose();
		new TelaCriarConta(this.cliente);
	}

	private void removerConta(IConta conta) {
		// TODO Auto-generated method stub
		try {
			if (conta.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
				throw new SaldoNaContaEx();
			} else {
				int confirmDelete = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover essa conta?",
						"Confirmação", JOptionPane.YES_NO_OPTION);

				if (confirmDelete == JOptionPane.YES_OPTION) {
					this.cliente.removerConta(conta);
					this.cliente.carregarContas();
					dispose();
					new PainelConta(this.cliente);
				}
			}
		} catch (SaldoNaContaEx e) {
			// TODO: handle exception
			e.alert();
		}
	}

	public void apagarCliente(ActionEvent e) {
		ContaDAO cDAO = new ContaDAO(new ConexaoMySql());
		boolean verificarSaldo = cDAO.balancoEntreContas(this.cliente.getCpf());

		try {
			if (verificarSaldo) {
				throw new ContaTemSaldoEx();
			} else {
				int confirmarDelete = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir sua conta?",
						"Confirmação", JOptionPane.YES_NO_OPTION);

				if (confirmarDelete == JOptionPane.YES_OPTION) {
					cDAO.apagarCliente(this.cliente.getCpf());
					sairConta(e);
				}
			}
		} catch (ContaTemSaldoEx e2) {
			// TODO: handle exception
			e2.alert();
		}
	}

	private void sairConta(ActionEvent e) {
		// TODO Auto-generated method stub
		dispose();
		this.cliente = null;
		new TelaPrincipal();

	}

	private void sacarConta(IConta conta) {
		// TODO Auto-generated method stub
		String quantiaTexto = JOptionPane.showInputDialog(this, "Digite o valor do saque:");

		if (quantiaTexto != null) {
			try {
				BigDecimal valorSaque = new BigDecimal(quantiaTexto);

				if (valorSaque.compareTo(BigDecimal.ZERO) <= 0 || valorSaque.compareTo(conta.getSaldo()) > 0) {
					throw new ValorInvalidoEx();
				} else {
					conta.sacar(valorSaque);
					this.cliente.carregarContas();
					dispose();
					new PainelConta(this.cliente);
				}

			} catch (NumberFormatException e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
			} catch (ValorInvalidoEx e) {
				// TODO: handle exception
				e.alert();
			}
		}
	}

	private void depositarConta(IConta conta) {
		// TODO Auto-generated method stub

		String quantiaTexto = JOptionPane.showInputDialog(this, "Digite o valor do depósito:");

		if (quantiaTexto != null) {
			try {
				BigDecimal valorDeposito = new BigDecimal(quantiaTexto);
				if (valorDeposito.compareTo(BigDecimal.ZERO) <= 0) {
					throw new ValorInvalidoEx();
				} else {
					conta.depositar(valorDeposito);
					this.cliente.carregarContas();
					dispose();
					new PainelConta(this.cliente);
				}
			} catch (NumberFormatException e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
			} catch (ValorInvalidoEx e) {
				// TODO: handle exception
				e.alert();
			}
		}
	}

	private void transferir(IConta conta) {
		// TODO Auto-generated method stub
		String contaDestinoTexto = JOptionPane.showInputDialog(this, "Digite o número da conta de destino:");

		if (contaDestinoTexto != null) {
			try {
				int numeroContaDestino = Integer.parseInt(contaDestinoTexto);
				ContaDAO cDAO = new ContaDAO(new ConexaoMySql());
				if (cDAO.buscarContaNumero(numeroContaDestino)) {
					if (conta.getNumero() == numeroContaDestino) {
						throw new TransferirMesmaContaEX();
					} else {
						String quantiaTexto = JOptionPane.showInputDialog(this, "Digite o valor da transferência:");

						if (quantiaTexto != null) {
							try {
								BigDecimal valorTransfer = new BigDecimal(quantiaTexto);
								if (valorTransfer.compareTo(BigDecimal.ZERO) < 0
										|| valorTransfer.compareTo(conta.getSaldo()) > 0) {
									throw new ValorInvalidoEx();
								} else {
									conta.transferir(valorTransfer, numeroContaDestino);
									this.cliente.carregarContas();
									dispose();
									new PainelConta(this.cliente);
								}
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro",
										JOptionPane.ERROR_MESSAGE);
							} catch (ValorInvalidoEx e) {
								e.alert();
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(this, "A conta de destino não foi encontrada!", "Erro",
							JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "Número de conta inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
			} catch (TransferirMesmaContaEX e) {
				// TODO: handle exception
				e.alert();
			}
		}
	}

	private String formatarSaldo(BigDecimal saldo) {
		// TODO Auto-generated method stub
		return saldo.setScale(2, RoundingMode.HALF_UP).toString();
	}

	public void exibirSaldoTotal(ActionEvent e) {
		JOptionPane.showMessageDialog(this, "Saldo de " + cliente.balancoEntreContas());
	}
}
