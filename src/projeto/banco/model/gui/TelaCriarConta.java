package projeto.banco.model.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import projeto.banco.model.cliente.ICliente;
import projeto.banco.model.conta.ContaCorrente;
import projeto.banco.model.conta.ContaPoupanca;
import projeto.banco.model.conta.IConta;

public class TelaCriarConta extends JFrame {
	private static final long serialVersionUID = 1L;

	ICliente cliente;

	public TelaCriarConta(ICliente cliente) {
		this.cliente = cliente;

		setTitle("Criar Conta");
		setSize(400, 250);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);

		JLabel label = new JLabel("Selecione o tipo de conta:");
		label.setBounds(120, 20, 200, 30);

		JButton botaoContaPoupanca = new JButton("Poupança");
		botaoContaPoupanca.setBounds(120, 60, 150, 30);
		botaoContaPoupanca.addActionListener(this::adicionarContaPoupanca);

		JButton botaoContaCorrente = new JButton("Corrente");
		botaoContaCorrente.setBounds(120, 100, 150, 30);
		botaoContaCorrente.addActionListener(this::adicionarContaCorrente);

		JButton botaoVoltar = new JButton("Voltar");
		botaoVoltar.setBounds(120, 140, 150, 30);
		botaoVoltar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new PainelConta(cliente);
				dispose();
			}
		});

		add(label);
		add(botaoContaPoupanca);
		add(botaoContaCorrente);
		add(botaoVoltar);

		setVisible(true);
	}

	public void voltar(ActionListener e) {
		dispose();
		new PainelConta(this.cliente);
	}

	public void adicionarContaCorrente(ActionEvent e) {
		IConta novaConta = new ContaCorrente(this.cliente.getCpf(), "corrente");
		this.cliente.adicionarConta(novaConta);
		this.cliente.carregarContas();
		dispose();
		new PainelConta(this.cliente);
	}

	public void adicionarContaPoupanca(ActionEvent e) {
		IConta novaConta = new ContaPoupanca(this.cliente.getCpf(), "poupanca");
		this.cliente.adicionarConta(novaConta);
		this.cliente.carregarContas();
		dispose();
		new PainelConta(this.cliente);
	}
}
