package projeto.banco.exception;

import javax.swing.JOptionPane;

public class ContaTemSaldoEx extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = "Não é possível remover a conta devido ao saldo em uma ou ambas ás contas.";

	public ContaTemSaldoEx() {
		super();
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void alert() {
		JOptionPane.showMessageDialog(null, getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
	}
}
