package projeto.banco.exception;

import javax.swing.JOptionPane;

public class CpfExistenteEx extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = "Cpf já está em uso";

	public CpfExistenteEx() {
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
