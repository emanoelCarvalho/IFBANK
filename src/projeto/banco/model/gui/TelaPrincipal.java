package projeto.banco.model.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import projeto.banco.dao.ClienteDAO;
import projeto.banco.database.ConexaoMySql;
import projeto.banco.exception.CamposDeEntradaVaziosEx;
import projeto.banco.exception.CpfInvalidoCurtoEx;
import projeto.banco.model.cliente.ICliente;

public class TelaPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;

	JLabel labelCpf;
	JTextField inputCpf;

	public TelaPrincipal() {
		setTitle("IFBANK ");
		setSize(1000, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.labelCpf = new JLabel("CPF: ");
		this.labelCpf.setBounds(400, 220, 120, 30);
		this.inputCpf = new JTextField();
		this.inputCpf.setBounds(430, 220, 120, 30);

		setLayout(null);
		JButton botaoLogar = new JButton("Entrar");
		botaoLogar.setBounds(450, 300, 80, 30);
		botaoLogar.addActionListener(e -> logar());

		JButton botaoRegistro = new JButton("Criar");
		botaoRegistro.setBounds(450, 340, 80, 30);
		botaoRegistro.addActionListener(e -> registrar());

		JButton botaoConsultaClientes = new JButton("Clientes");
		botaoConsultaClientes.setBounds(600, 340, 80, 30);
		botaoConsultaClientes.addActionListener(e -> consultarClientes());

		add(inputCpf);
		add(labelCpf);
		add(botaoLogar);
		add(botaoRegistro);
		add(botaoConsultaClientes);
		setVisible(true);
	}

	public void logar() {
		String cpf = inputCpf.getText();

		try {
			if (cpf.isEmpty()) {
				throw new CamposDeEntradaVaziosEx();
			}

			if (cpf.length() != 11) {
				throw new CpfInvalidoCurtoEx();
			}

			ClienteDAO cDAO = new ClienteDAO(new ConexaoMySql());
			ICliente cliente = cDAO.logarConta(cpf);

			if (cliente == null) {
				return;
			}
			dispose();
			new PainelConta(cliente);
		} catch (CamposDeEntradaVaziosEx e2) {
			// TODO: handle exception
			e2.alert();
		} catch (CpfInvalidoCurtoEx e2) {
			// TODO: handle exception
			e2.alert();
		}
	}

	public void registrar() {
		dispose();
		new TelaRegistro();
	}

	public void consultarClientes() {
		dispose();
		new TelaClientes();
	}

}
