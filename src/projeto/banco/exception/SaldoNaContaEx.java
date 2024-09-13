package projeto.banco.exception;

import javax.swing.JOptionPane;

public class SaldoNaContaEx extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = "Não é possível remover uma conta com saldo.";

	public SaldoNaContaEx() {
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
