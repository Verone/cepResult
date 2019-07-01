package br.com.montroni.cepResult.view;

public class Peso {

	private String pesoInicial;
	private String pesoFinal;
	private String pesoConsulta;
		
	/**
	 * @return the pesoInicial
	 */
	public String getPesoInicial() {
		return pesoInicial;
	}


	/**
	 * @param pesoInicial the pesoInicial to set
	 */
	public void setPesoInicial(String pesoInicial) {
		this.pesoInicial = pesoInicial;
	}


	/**
	 * @return the pesoFinal
	 */
	public String getPesoFinal() {
		return pesoFinal;
	}


	/**
	 * @param pesoFinal the pesoFinal to set
	 */
	public void setPesoFinal(String pesoFinal) {
		this.pesoFinal = pesoFinal;
	}


	/**
	 * @return the pesoConsulta
	 */
	public String getPesoConsulta() {
		return pesoConsulta;
	}


	/**
	 * @param pesoConsulta the pesoConsulta to set
	 */
	public void setPesoConsulta(String pesoConsulta) {
		this.pesoConsulta = pesoConsulta;
	}


	public Peso(String pPesoInicial, String pPesoFinal, String pPesoConsulta) {
		this.pesoInicial = pPesoConsulta;
		this.pesoFinal = pPesoFinal;
		this.pesoConsulta = pPesoConsulta;// TODO Auto-generated constructor stub
	}

}
