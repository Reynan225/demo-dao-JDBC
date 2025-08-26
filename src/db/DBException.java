package db;

//Exceção personalizada para indicar erros genéricos de banco de dados
//Exemplo: falha em INSERT, UPDATE, SELECT ou tentar deletar um ID inexistente
public class DBException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DBException(String msg) {
		super(msg);
	}
}
