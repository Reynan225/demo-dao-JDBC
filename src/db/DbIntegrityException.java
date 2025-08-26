package db;

//Exceção personalizada para indicar violação de integridade no banco
//Exemplo: tentar deletar um departamento que ainda tem vendedores vinculados

public class DbIntegrityException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DbIntegrityException(String msg) {
		super(msg);
	}
}

