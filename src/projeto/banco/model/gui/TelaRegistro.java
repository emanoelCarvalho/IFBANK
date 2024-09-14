package projeto.banco.model.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import projeto.banco.dao.ClienteDAO;
import projeto.banco.database.ConexaoMySql;
import projeto.banco.exception.CamposDeEntradaVaziosEx;
import projeto.banco.exception.CpfExistenteEx;
import projeto.banco.exception.CpfInvalidoCurtoEx;
import projeto.banco.model.cliente.Cliente;
import projeto.banco.model.cliente.ICliente;

public class TelaRegistro extends JFrame {

	private static final long serialVersionUID = 1L;

	JLabel labelCpf;
	JTextField inputCpf;

	JLabel labelNome;
	JTextField inputNome;

	public TelaRegistro() {
		setTitle("IFBANK");
		setSize(1000, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.labelNome = new JLabel("Nome: ");
		this.labelNome.setBounds(390, 120, 120, 30); 

		this.inputNome = new JTextField();
		this.inputNome.setBounds(430, 120, 120, 30); 

		this.labelCpf = new JLabel("Cpf: ");
		this.labelCpf.setBounds(390, 160, 120, 30); 

		this.inputCpf = new JTextField();
		this.inputCpf.setBounds(430, 160, 120, 30);

		setLayout(null);

		JButton botaoRegistro = new JButton("Criar");
		botaoRegistro.setBounds(450, 360, 80, 30);
		botaoRegistro.addActionListener(e -> registrar());

		JButton botaoVoltar = new JButton("Voltar");
		botaoVoltar.setBounds(450, 400, 80, 30);
		botaoVoltar.addActionListener(e -> voltar());

		add(labelNome);
		add(inputNome);
		add(labelCpf);
		add(inputCpf);
		add(botaoRegistro);
		add(botaoVoltar);
		setVisible(true);

	}

	private void registrar() {
		// TODO Auto-generated method stub

		String cpfCliente = inputCpf.getText();
		String nomeCliente = inputNome.getText();

		try {
			if (cpfCliente.isEmpty() || nomeCliente.isEmpty()) {
				throw new CamposDeEntradaVaziosEx();
			} else {
				if (cpfCliente.length() != 11) {
					throw new CpfInvalidoCurtoEx();
				}
				ClienteDAO cDAO = new ClienteDAO(new ConexaoMySql());

				if (!cDAO.verificarCpf(cpfCliente)) {
					return;
				} else {
					ICliente cliente = new Cliente(cpfCliente, nomeCliente);
					cDAO.adicionarCliente(cliente);
					dispose();
					new PainelConta(cliente);
				}
			}
		} catch (CpfExistenteEx e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		} catch (CpfInvalidoCurtoEx e) {
			// TODO: handle exception
			e.alert();
		} catch (CamposDeEntradaVaziosEx e) {
			// TODO: handle exception
			e.alert();
		}
	}

	private void voltar() {
		// TODO Auto-generated method stub
		dispose();
		new TelaPrincipal();
	}

}
