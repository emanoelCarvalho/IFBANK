package projeto.banco.model.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import projeto.banco.dao.conta.ContaDAO;
import projeto.banco.database.ConexaoMySql;
import projeto.banco.exception.ContaTemSaldoEx;
import projeto.banco.exception.SaldoNaContaEx;
import projeto.banco.exception.TransferirMesmaContaEX;
import projeto.banco.exception.ValorInvalidoEx;
import projeto.banco.model.cliente.ICliente;
import projeto.banco.model.conta.IConta;
import projeto.banco.model.transacao.RegistroTransacao;

public class TelaConta extends JFrame {

	private static final long serialVersionUID = 1L;

	ICliente cliente;
	JLabel alert;
	JLabel titulo;
	JLabel clienteNome;
	JLabel clienteCpf;
	JLabel balancoEntreContas;

	public TelaConta(ICliente cliente) {
		this.cliente = cliente;
		setTitle("Ações do banco");
		setSize(1500, 1000);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

		this.titulo = new JLabel("Bem vindo " + this.cliente.getNome());
		this.titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelPrincipal.add(this.titulo);

		this.clienteCpf = new JLabel("Cpf: " + this.cliente.getCpf());
		this.clienteCpf.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelPrincipal.add(this.clienteCpf);

		int numeroContas = this.cliente.getContas().size();
		if (numeroContas == 0 || numeroContas == 1 || numeroContas == 2) {
			JButton criarConta = new JButton("Criar conta");
			criarConta.setAlignmentX(Component.CENTER_ALIGNMENT);
			criarConta.addActionListener(this::adicionarConta);
			panelPrincipal.add(criarConta);

			if (numeroContas == 0) {
				JLabel alert = new JLabel("Você ainda não possui contas!");
				alert.setAlignmentX(Component.CENTER_ALIGNMENT);
				panelPrincipal.add(alert);
			}

			for (IConta conta : this.cliente.getContas()) {
				JPanel painelConta = new JPanel();
				painelConta.setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets = new Insets(10, 10, 10, 10);

				gbc.gridx = 0;
				gbc.gridy = 0;
				JLabel contaLabel = new JLabel("Número da conta: " + conta.getNumero() + " - Tipo: " + conta.getTipo()
						+ " - Saldo: R$" + formatarSaldo(conta.getSaldo()));
				painelConta.add(contaLabel, gbc);

				gbc.gridy++;
				JButton botaoDepositar = new JButton("Depósito");
				botaoDepositar.addActionListener(e -> depositarConta(conta));
				painelConta.add(botaoDepositar, gbc);

				gbc.gridy++;
				JButton botaoTransferir = new JButton("Efetuar transferência");
				botaoTransferir.addActionListener(e -> transferir(conta));
				painelConta.add(botaoTransferir, gbc);

				gbc.gridy++;
				JButton botaoSaque = new JButton("Realizar saque");
				botaoSaque.addActionListener(e -> sacarConta(conta));
				painelConta.add(botaoSaque, gbc);

				gbc.gridy++;
				JButton botaoRemover = new JButton("Remover conta");
				botaoRemover.addActionListener(e -> removerConta(conta));
				painelConta.add(botaoRemover, gbc);

				gbc.gridy++;
				JButton botaoEmitirExtrato = new JButton("Emitir Extrato");
				botaoEmitirExtrato.addActionListener(e -> emitirExtrato(conta));
				painelConta.add(botaoEmitirExtrato, gbc);

				panelPrincipal.add(painelConta);
			}

			JButton botaoVerSaldoTotalContas = new JButton("Mostrar total dos saldos");
			botaoVerSaldoTotalContas.setAlignmentX(Component.CENTER_ALIGNMENT);
			botaoVerSaldoTotalContas.addActionListener(this::exibirSaldoTotal);
			panelPrincipal.add(botaoVerSaldoTotalContas);
		}

		JButton sair = new JButton("Sair");
		sair.addActionListener(this::sairConta);
		JButton removeClienteButton = new JButton("Excluir conta");
		removeClienteButton.addActionListener(this::apagarCliente);

		JPanel panelBotoes = new JPanel();
		panelBotoes.add(sair);
		panelBotoes.add(removeClienteButton);

		add(panelPrincipal, BorderLayout.CENTER);
		add(panelBotoes, BorderLayout.SOUTH);

		setVisible(true);

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
					new TelaConta(this.cliente);
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
					new TelaConta(this.cliente);
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
					new TelaConta(this.cliente);
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

	private void emitirExtrato(IConta conta) {
		String mesTexto = JOptionPane.showInputDialog(this, "Digite o mês (em número):");
		String anoTexto = JOptionPane.showInputDialog(this, "Digite o ano:");

		if (mesTexto != null && anoTexto != null) {
			try {
				int mes = Integer.parseInt(mesTexto);
				int ano = Integer.parseInt(anoTexto);

				if (mes < 1 || mes > 12 || ano < 1900) {
					JOptionPane.showMessageDialog(this, "Mês ou ano inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}

				int numeroConta = conta.getNumero();

				List<RegistroTransacao> extrato = conta.emitirExtrato(numeroConta, mes, ano);

				StringBuilder extratoStr = new StringBuilder(
						"Extrato da conta " + conta.getNumero() + " para " + mes + "/" + ano + ":\n");
				for (RegistroTransacao transacao : extrato) {
					extratoStr.append("Número da Transação: ").append(transacao.getNumero()).append(", ")
							.append("Data: ").append(transacao.getDataTransacao()).append(", ")
							.append("Conta de Origem: ").append(transacao.getContaOrigem()).append(", ")
							.append("Conta de Destino: ").append(transacao.getContaDestino()).append(", ")
							.append("Valor: R$").append(transacao.getValorTransacao()).append(", ").append("Tipo: ")
							.append(transacao.getTipoTransacao()).append("\n");
				}
				JOptionPane.showMessageDialog(this, extratoStr.toString(), "Extrato", JOptionPane.INFORMATION_MESSAGE);

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Mês ou ano inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
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
									new TelaConta(this.cliente);
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
