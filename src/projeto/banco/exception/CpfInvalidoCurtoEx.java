package projeto.banco.exception;

import javax.swing.JOptionPane;

public class CpfInvalidoCurtoEx extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = "CPF deve conter 11 digitos!";

	public CpfInvalidoCurtoEx() {
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
