package projeto.banco.exception;

import javax.swing.JOptionPane;

public class CamposDeEntradaVaziosEx extends Exception {

	private static final long serialVersionUID = 1L;

    private String message = "Todos os campos devem ser preenchidos!";

    
	public CamposDeEntradaVaziosEx() {
		super();
	}
	
	 @Override
	    public String getMessage() {
	        return message;
	    }

	    public void alert() {
	        JOptionPane.showMessageDialog(null, getMessage(), "Erro",
	                JOptionPane.ERROR_MESSAGE);
	    }
}
