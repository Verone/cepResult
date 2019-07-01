package br.com.montroni.cepResult.view;

public class Cep {

	private String cepInicial;
	private String cepFinal;
	private String cepConsulta;
	private String cepDescricao;
	/**
	 * @return the cepInicial
	 */
	public String getCepInicial() {
		return cepInicial;
	}
	/**
	 * @param cepInicial the cepInicial to set
	 */
	public void setCepInicial(String cepInicial) {
		this.cepInicial = cepInicial;
	}
	/**
	 * @return the cepFinal
	 */
	public String getCepFinal() {
		return cepFinal;
	}
	/**
	 * @param cepFinal the cepFinal to set
	 */
	public void setCepFinal(String cepFinal) {
		this.cepFinal = cepFinal;
	}
	/**
	 * @return the cepConsulta
	 */
	public String getCepConsulta() {
		return cepConsulta;
	}
	/**
	 * @param cepConsulta the cepConsulta to set
	 */
	public void setCepConsulta(String cepConsulta) {
		this.cepConsulta = cepConsulta;
	}
	/**
	 * @return the cepDescricao
	 */
	public String getCepDescricao() {
		return cepDescricao;
	}
	/**
	 * @param cepDescricao the cepDescricao to set
	 */
	public void setCepDescricao(String cepDescricao) {
		this.cepDescricao = cepDescricao;
	}
	public Cep(String cepInicial, String cepFinal, String cepConsulta, String cepDescricao) {
		super();
		this.cepInicial = cepInicial;
		this.cepFinal = cepFinal;
		this.cepConsulta = cepConsulta;
		this.cepDescricao = cepDescricao;
	}
	
	
}
