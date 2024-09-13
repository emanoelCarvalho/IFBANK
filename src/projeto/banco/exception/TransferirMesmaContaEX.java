package projeto.banco.exception;

import javax.swing.JOptionPane;

public class TransferirMesmaContaEX extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = "Não é possível transferir para a mesma conta!";

	public TransferirMesmaContaEX() {
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
