package projeto.banco.exception;

import javax.swing.JOptionPane;

public class ValorInvalidoEx extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = "Valor inválido!";

	public ValorInvalidoEx() {
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
