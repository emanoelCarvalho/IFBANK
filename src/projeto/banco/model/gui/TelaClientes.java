package projeto.banco.model.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import projeto.banco.dao.ClienteDAO;
import projeto.banco.database.ConexaoMySql;
import projeto.banco.model.cliente.ICliente;
import projeto.banco.model.conta.IConta;

public class TelaClientes extends JFrame {

	private static final long serialVersionUID = 1L;

	private JList<String> listaDeClientes;
	private DefaultListModel<String> model;
	private JTextField searchField;
	private JButton botaoVoltar;
	private JLabel userDataLabel;
	private JLabel accountDataLabel;

	public TelaClientes() {
		setTitle("IFBANK");
		setSize(1000, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		model = new DefaultListModel<>();

		ClienteDAO cDAO = new ClienteDAO(new ConexaoMySql());
		List<String> clientes = cDAO.listarClientes();

		for (String cliente : clientes) {
			model.addElement(cliente);
		}

		listaDeClientes = new JList<>(model);
		listaDeClientes.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				handleSelection(listaDeClientes.getSelectedValue());
			}
		});

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(listaDeClientes);

		JScrollPane scrollPane = new JScrollPane(panel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(10, 10, 10, 10);

		JLabel titleLabel = new JLabel("Informações do Cliente");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		mainPanel.add(titleLabel, constraints);

		JLabel searchLabel = new JLabel("Número de CPF:");
		searchField = new JTextField(9);
		searchField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				filterList();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				filterList();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				filterList();

			}
		});

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		mainPanel.add(searchLabel, constraints);

		constraints.gridx = 1;
		mainPanel.add(searchField, constraints);

		botaoVoltar = new JButton("Voltar");
		botaoVoltar.addActionListener(e -> voltarTela());

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		mainPanel.add(botaoVoltar, constraints);

		userDataLabel = new JLabel();
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		mainPanel.add(userDataLabel, constraints);

		accountDataLabel = new JLabel();
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		mainPanel.add(accountDataLabel, constraints);

		getContentPane().add(mainPanel, BorderLayout.EAST);

		setVisible(true);
	}

	public void voltarTela() {
		dispose();
		new TelaPrincipal();
	}

	private void filterList() {
		String searchTerm = searchField.getText().toLowerCase();
		DefaultListModel<String> filteredModel = new DefaultListModel<>();

		for (int i = 0; i < model.size(); i++) {
			String item = model.getElementAt(i);
			String cpf = extrairCpf(item);

			if (cpf.toLowerCase().contains(searchTerm)) {
				filteredModel.addElement(item);
			}
		}

		listaDeClientes.setModel(filteredModel);
	}

	public String extrairCpf(String item) {
		
		if (item == null) {
			return "";
		}
		
		String cpf = item.replaceAll("\\D", "");
		return cpf;
	}

	private void handleSelection(String selectedValue) {
		String cpf = extrairCpf(selectedValue);

		ClienteDAO dado = new ClienteDAO(new ConexaoMySql());
		ICliente cliente = dado.buscarClientePorCpf(cpf);

		if (cliente != null) {
			StringBuilder dadosDoCliente = new StringBuilder();
			dadosDoCliente.append("<html>");
			dadosDoCliente.append("Nome: ").append(cliente.getNome()).append("<br/>");
			dadosDoCliente.append("CPF: ").append(cliente.getCpf()).append("<br/>");
			dadosDoCliente.append("</html>");
			userDataLabel.setText(dadosDoCliente.toString());

			List<IConta> contas = cliente.getContas();

			if (contas != null && !contas.isEmpty()) {
				StringBuilder dadosConta = new StringBuilder();
				dadosConta.append("<html>Contas:<br/>");
				for (IConta conta : contas) {
					dadosConta.append("Número: ").append(conta.getNumero()).append("<br/>");
					dadosConta.append("Tipo: ").append(conta.getTipo()).append("<br/>");
					dadosConta.append("Saldo: ").append(conta.getSaldo()).append("<br/><br/>");
				}
				dadosConta.append("</html>");
				accountDataLabel.setText(dadosConta.toString());
			} else {
				accountDataLabel.setText("Não há contas associadas a este usuário.");
			}
		} else {
			userDataLabel.setText("");
			accountDataLabel.setText("");
		}

	}
}
