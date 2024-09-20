package projeto.banco.dao.cliente;

import java.util.List;

import projeto.banco.exception.CpfExistenteEx;
import projeto.banco.model.cliente.ICliente;

public interface IClienteDAO {

	Boolean adicionarCliente(ICliente cliente);

	ICliente buscarClientePorCpf(String cpf);

	Boolean verificarCpf(String cpf) throws CpfExistenteEx;

	ICliente logarConta(String cpf);

	List<String> listarClientes();

	List<String> getDadosClientes(String cpf);
}
