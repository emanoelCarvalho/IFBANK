package projeto.banco.model.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import projeto.banco.dao.cliente.ClienteDAO;
import projeto.banco.database.ConexaoMySql;
import projeto.banco.model.cliente.ICliente;
import projeto.banco.model.conta.IConta;

public class TelaClientes extends JFrame {

	private static final long serialVersionUID = 1L;

	private JList<String> listaDeClientes;
	private DefaultListModel<String> model;
	private JTextField searchField;
	private JLabel userDataLabel;
	private JLabel accountDataLabel;

	public TelaClientes() {
		setTitle("IFBANK");
		setSize(1000, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initializing components
		model = new DefaultListModel<>();
		listaDeClientes = new JList<>(model);
		searchField = new JTextField(15);
		userDataLabel = new JLabel();
		accountDataLabel = new JLabel();

		// Load client data
		loadClientData();

		// Layout setup
		setLayout(new BorderLayout());

		// Setup client list panel
		JPanel listPanel = new JPanel(new BorderLayout());
		listPanel.setBorder(BorderFactory.createTitledBorder("Clientes"));
		listaDeClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaDeClientes.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				handleSelection(listaDeClientes.getSelectedValue());
			}
		});
		JScrollPane scrollPane = new JScrollPane(listaDeClientes);
		listPanel.add(scrollPane, BorderLayout.CENTER);
		add(listPanel, BorderLayout.WEST);

		// Setup search panel
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchPanel.add(new JLabel("Número de CPF:"));
		searchPanel.add(searchField);
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				filterList();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filterList();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				filterList();
			}
		});
		add(searchPanel, BorderLayout.NORTH);

		// Setup detail panel
		JPanel detailPanel = new JPanel(new GridBagLayout());
		detailPanel.setBorder(BorderFactory.createTitledBorder("Informações do Cliente"));
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);

		JLabel titleLabel = new JLabel("Informações do Cliente");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		detailPanel.add(titleLabel, constraints);

		constraints.gridwidth = 1;
		constraints.gridy = 1;
		detailPanel.add(new JLabel("Nome:"), constraints);
		constraints.gridx = 1;
		detailPanel.add(userDataLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		detailPanel.add(new JLabel("Contas:"), constraints);
		constraints.gridx = 1;
		detailPanel.add(accountDataLabel, constraints);

		add(detailPanel, BorderLayout.CENTER);

		// Setup back button
		JButton backButton = new JButton("Voltar");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				voltarTela();
			}
		});
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(backButton);
		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void loadClientData() {
		ClienteDAO cDAO = new ClienteDAO(new ConexaoMySql());
		List<String> clientes = cDAO.listarClientes();
		model.clear();
		for (String cliente : clientes) {
			model.addElement(cliente);
		}
	}

	private void filterList() {
		String searchTerm = searchField.getText().toLowerCase();
		DefaultListModel<String> filteredModel = new DefaultListModel<>();
		for (int i = 0; i < model.size(); i++) {
			String item = model.getElementAt(i);
			if (extrairCpf(item).toLowerCase().contains(searchTerm)) {
				filteredModel.addElement(item);
			}
		}
		listaDeClientes.setModel(filteredModel);
	}

	private String extrairCpf(String item) {
		if (item == null) {
			return "";
		}
		return item.replaceAll("\\D", "");
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
					dadosConta.append("Saldo: R$").append(conta.getSaldo()).append("<br/><br/>");
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

	private void voltarTela() {
		dispose();
		new TelaPrincipal();
	}
}
