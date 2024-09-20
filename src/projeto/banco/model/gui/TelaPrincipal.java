package projeto.banco.model.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import projeto.banco.dao.cliente.ClienteDAO;
import projeto.banco.database.ConexaoMySql;
import projeto.banco.exception.CamposDeEntradaVaziosEx;
import projeto.banco.exception.CpfInvalidoCurtoEx;
import projeto.banco.model.cliente.ICliente;

public class TelaPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTextField inputCpf;

	public TelaPrincipal() {
		setTitle("IFBANK");
		setSize(1000, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Layout setup
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.anchor = GridBagConstraints.CENTER;

		// CPF Label
		JLabel labelCpf = new JLabel("CPF:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(labelCpf, constraints);

		// CPF Input
		inputCpf = new JTextField(15);
		constraints.gridx = 1;
		add(inputCpf, constraints);

		// Login Button
		JButton botaoLogar = new JButton("Entrar");
		botaoLogar.addActionListener(this::logar);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		add(botaoLogar, constraints);

		// Register Button
		JButton botaoRegistro = new JButton("Criar");
		botaoRegistro.addActionListener(this::registrar);
		constraints.gridy = 2;
		add(botaoRegistro, constraints);

		// Clients Button
		JButton botaoConsultaClientes = new JButton("Clientes");
		botaoConsultaClientes.addActionListener(this::consultarClientes);
		constraints.gridy = 3;
		add(botaoConsultaClientes, constraints);

		setVisible(true);
	}

	private void logar(ActionEvent e) {
		String cpf = inputCpf.getText().trim();

		try {
			validarCpf(cpf);

			ClienteDAO cDAO = new ClienteDAO(new ConexaoMySql());
			ICliente cliente = cDAO.logarConta(cpf);

			if (cliente != null) {
				dispose();
				new TelaConta(cliente);
			} else {
				JOptionPane.showMessageDialog(this, "Cliente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		} catch (CamposDeEntradaVaziosEx | CpfInvalidoCurtoEx ex) {
			((CamposDeEntradaVaziosEx) ex).alert();
		}
	}

	private void validarCpf(String cpf) throws CamposDeEntradaVaziosEx, CpfInvalidoCurtoEx {
		if (cpf.isEmpty()) {
			throw new CamposDeEntradaVaziosEx();
		}
		if (cpf.length() != 11) {
			throw new CpfInvalidoCurtoEx();
		}
	}

	private void registrar(ActionEvent e) {
		dispose();
		new TelaRegistro();
	}

	private void consultarClientes(ActionEvent e) {
		dispose();
		new TelaClientes();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(TelaPrincipal::new);
	}
}
