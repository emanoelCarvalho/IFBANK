package projeto.banco.exception;

import javax.swing.JOptionPane;

public class CpfInvalidoEx extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String mensagem = "CPF infválido, tente nomamente";

	public CpfInvalidoEx() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return mensagem;
	}

	public void alert() {
		JOptionPane.showMessageDialog(null, getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
	}

}
