package br.com.montroni.cepResult;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import br.com.montroni.cepResult.view.GerarCSV;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class ControllerForm {

	@FXML
	private TextField usuario;

	@FXML
	private TextField senha;

	@FXML
	private TextField arquivo;

	@FXML
	private TextField cep;

	@FXML
	private ProgressBar progresso;

	@FXML
	private CheckBox cb_40010;

	@FXML
	private CheckBox cb_40045;

	@FXML
	private CheckBox cb_40126;

	@FXML
	private CheckBox cb_40215;

	@FXML
	private CheckBox cb_40290;
	@FXML
	private CheckBox cb_40096;
	@FXML
	private CheckBox cb_40436;
	@FXML
	private CheckBox cb_40444;
	@FXML
	private CheckBox cb_40568;
	@FXML
	private CheckBox cb_40606;
	@FXML
	private CheckBox cb_41106;
	@FXML
	private CheckBox cb_41211;
	@FXML
	private CheckBox cb_41068;
	@FXML
	private CheckBox cb_04669;
	@FXML
	private CheckBox cb_04510;
	@FXML
	private CheckBox cb_10707;
	@FXML
	private CheckBox cb_10014;
	@FXML
	private CheckBox cb_04162;
	@FXML
	private CheckBox cb_04014;
	@FXML
	private CheckBox cb_04693;

	@FXML
	private CheckBox cb_04316;

	@FXML
	private CheckBox cb_04812;

	@FXML
	private CheckBox cb_40836;

	@FXML
	private TextField edCodigo01;

	@FXML
	private TextField edDescricao01;

	@FXML
	private TextField edCodigo02;

	@FXML
	private TextField edDescricao02;

	@FXML
	private CheckBox boConsulta;

	@FXML
	private CheckBox boACRE;

	@FXML
	private CheckBox boALAGOAS;

	@FXML
	private CheckBox boAMAZONAS;

	@FXML
	private CheckBox boAMAPA;

	@FXML
	private CheckBox boBAHIA;

	@FXML
	private CheckBox boCEARA;

	@FXML
	private CheckBox boDISTRITO_FEDERAL;

	@FXML
	private CheckBox boESPIRITO_SANTO;

	@FXML
	private CheckBox boGOIAS;

	@FXML
	private CheckBox boMARANHAO;

	@FXML
	private CheckBox boMINAS_GERAIS;

	@FXML
	private CheckBox boMATO_GROSSO_SUL;

	@FXML
	private CheckBox boMATO_GROSSO;

	@FXML
	private CheckBox boPARA;

	@FXML
	private CheckBox boPARAIBA;

	@FXML
	private CheckBox boPERNAMBUCO;

	@FXML
	private CheckBox boPIAUI;

	@FXML
	private CheckBox boPARANA;

	@FXML
	private CheckBox boRIO_JANEIRO;

	@FXML
	private CheckBox boRIO_GRANDE_NORTE;

	@FXML
	private CheckBox boRONDONIA;

	@FXML
	private CheckBox boRORAIMA;

	@FXML
	private CheckBox boRIO_GRANDE_SUL;

	@FXML
	private CheckBox boSANTA_CATARINA;

	@FXML
	private CheckBox boSERGIPE;

	@FXML
	private CheckBox boSAO_PAULO;

	@FXML
	private CheckBox boTOCANTINS;

	@FXML
	private Button botao;

	@FXML
	private TextField qt_req;

	@FXML
	private TextField txtAltura;

	@FXML
	private TextField txtLargura;

	@FXML
	private TextField txtComprimento;

	GerarCSV gerarCSV = GerarCSV.getInstance();

	Thread thredGerarCSV = new Thread(gerarCSV);

	public ControllerForm() {
	}

	public void setaStatus(Double percentual) {
		progresso.setProgress(percentual);
	}

	public void setaRequisicao(String nreq) {
		qt_req.setText(nreq.toString());
	}

	@FXML
	public void abrirLink() {
		try {
			Desktop.getDesktop().browse(new URI("http://www.corporativo.correios.com.br/encomendas/sigepweb/"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void iniciarProcesso() {

		if (cep.getText().length() != 8) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Erro ao iniciar atividade");
			alert.setHeaderText("Atenção");
			alert.setContentText("Cep deve conter 8 caracteres.\n.Atualmente tem " + cep.getText().length());
			alert.show();
			return;

		}

		boolean bo_servico = false;

		if (botao.getText() == "Cancelar e Encerrar") {
			System.exit(0);
		} else {
			botao.setText("Cancelar e Encerrar");
		}

		gerarCSV.setUsuario(usuario.getText());
		gerarCSV.setSenha(senha.getText());
		gerarCSV.setCep(cep.getText());
		gerarCSV.setLog(boConsulta.isSelected());
		gerarCSV.setTxtAltura(txtAltura.getText());
		gerarCSV.setTxtLargura(txtLargura.getText());
		gerarCSV.setTxtComprimento(txtComprimento.getText());

		listaDeEstadosHabilitados();

		if (cb_41068.isSelected()) {
			gerarCSV.incluiServico("41068", "PAC com contrato - 41068");
			bo_servico = true;
		}

		if (cb_41211.isSelected()) {
			gerarCSV.incluiServico("41211", "PAC com contrato - 41211");
			bo_servico = true;
		}

		if (cb_41106.isSelected()) {
			gerarCSV.incluiServico("41106", "PAC sem contrato - 41106");
			bo_servico = true;
		}

		if (cb_40606.isSelected()) {
			gerarCSV.incluiServico("40606", "SEDEX com contrato - 40606");
			bo_servico = true;
		}

		if (cb_40568.isSelected()) {
			gerarCSV.incluiServico("40568", "SEDEX com contrato - 40568");
			bo_servico = true;
		}

		if (cb_40444.isSelected()) {
			gerarCSV.incluiServico("40444", "SEDEX com contrato - 40444");
			bo_servico = true;
		}

		if (cb_40436.isSelected()) {
			gerarCSV.incluiServico("40436", "SEDEX com contrato - 40436");
			bo_servico = true;
		}

		if (cb_40096.isSelected()) {
			gerarCSV.incluiServico("40096", "SEDEX com contrato - 40096");
			bo_servico = true;
		}

		if (cb_40290.isSelected()) {
			gerarCSV.incluiServico("40290", "SEDEX Hoje sem contrato - 40290");
			bo_servico = true;
		}

		if (cb_40215.isSelected()) {
			gerarCSV.incluiServico("40215", "SEDEX 10 sem contrato - 40215");
			bo_servico = true;
		}

		if (cb_40126.isSelected()) {
			gerarCSV.incluiServico("40126", "SEDEX a Cobrar contrato - 40126");
			bo_servico = true;
		}

		if (cb_40045.isSelected()) {
			gerarCSV.incluiServico("40045", "SEDEX a Cobrar contrato - 40045");
			bo_servico = true;
		}

		if (cb_40010.isSelected()) {
			gerarCSV.incluiServico("40010", "SEDEX sem contrato - 40010");
			bo_servico = true;
		}

		if (cb_04669.isSelected()) {
			gerarCSV.incluiServico("04669", "PAC contrato / Reverso 04669");
			bo_servico = true;
		}

		if (cb_04510.isSelected()) {
			gerarCSV.incluiServico("04510", "PAC VAREJO A VISTA - 04510");
			bo_servico = true;
		}

		if (cb_10707.isSelected()) {
			gerarCSV.incluiServico("10707", "Carta registrada contrato - 10707");
			bo_servico = true;
		}

		if (cb_10014.isSelected()) {
			gerarCSV.incluiServico("10014", "Carta Registrada a Vista - 10014");
			bo_servico = true;
		}

		if (cb_04162.isSelected()) {
			gerarCSV.incluiServico("04162", "SEDEX relac varejo - 04162");
			bo_servico = true;
		}

		if (cb_04693.isSelected()) {
			gerarCSV.incluiServico("04693", "PAC - Contrato - Grandes Formatos");
			bo_servico = true;
		}

		if (cb_04014.isSelected()) {
			gerarCSV.incluiServico("04014", "SEDEX varejo a vista - 04014");
			bo_servico = true;
		}

		if (cb_04316.isSelected()) {
			gerarCSV.incluiServico("04316", "SEDEX Industrial - 04316");
			bo_servico = true;
		}

		if (cb_04812.isSelected()) {
			gerarCSV.incluiServico("04812", "Pac Industrial - 04812");
			bo_servico = true;
		}

		if (cb_40836.isSelected()) {
			gerarCSV.incluiServico("40836", "Sedex - 40836");
			bo_servico = true;
		}

		if (!edCodigo01.getText().trim().isEmpty()) {

			if (edDescricao01.getText().trim().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Erro ao iniciar atividade");
				alert.setHeaderText("Atenção");
				alert.setContentText("Informou um codigo personalizado porém faltou a descrição.");
				alert.show();
				return;
			}

			gerarCSV.incluiServico(edCodigo01.getText().trim(), edDescricao01.getText().trim());
			bo_servico = true;
		}

		if (!edCodigo02.getText().trim().isEmpty()) {

			if (edDescricao02.getText().trim().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Erro ao iniciar atividade");
				alert.setHeaderText("Atenção");
				alert.setContentText("Informou um codigo personalizado porém faltou a descrição.");
				alert.show();
				return;
			}

			gerarCSV.incluiServico(edCodigo02.getText().trim(), edDescricao02.getText().trim());
			bo_servico = true;
		}

		if (!bo_servico) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Erro ao iniciar atividade");
			alert.setHeaderText("Atenção");
			alert.setContentText("Pelo menos 1 dos servicos deve estar selecionado.");
			alert.show();
			return;
		}

		GerarCSV.setArquivo(arquivo.getText());

		thredGerarCSV.start();

		GerarCSV.setContador(new Double(0));
		GerarCSV.setVltotalProcessamento(new Double(0));

		Timer t = new Timer();

		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {

				setaStatus(GerarCSV.getContador() / GerarCSV.getVltotalProcessamento());
				setaRequisicao(GerarCSV.getContador().toString() + '/' + GerarCSV.getVltotalProcessamento().toString());

			}
		};

		t.scheduleAtFixedRate(timerTask, 3, 3000);

	}

	private void listaDeEstadosHabilitados() {
		gerarCSV.setBoACRE(boACRE.isSelected());
		gerarCSV.setBoALAGOAS(boALAGOAS.isSelected());
		gerarCSV.setBoAMAZONAS(boAMAZONAS.isSelected());
		gerarCSV.setBoAMAPA(boAMAPA.isSelected());
		gerarCSV.setBoBAHIA(boBAHIA.isSelected());
		gerarCSV.setBoCEARA(boCEARA.isSelected());
		gerarCSV.setBoDISTRITO_FEDERAL(boDISTRITO_FEDERAL.isSelected());
		gerarCSV.setBoESPIRITO_SANTO(boESPIRITO_SANTO.isSelected());
		gerarCSV.setBoGOIAS(boGOIAS.isSelected());
		gerarCSV.setBoMARANHAO(boMARANHAO.isSelected());
		gerarCSV.setBoMINAS_GERAIS(boMINAS_GERAIS.isSelected());
		gerarCSV.setBoMATO_GROSSO_SUL(boMATO_GROSSO_SUL.isSelected());
		gerarCSV.setBoMATO_GROSSO(boMATO_GROSSO.isSelected());
		gerarCSV.setBoPARA(boPARA.isSelected());
		gerarCSV.setBoPARAIBA(boPARAIBA.isSelected());
		gerarCSV.setBoPERNAMBUCO(boPERNAMBUCO.isSelected());
		gerarCSV.setBoPIAUI(boPIAUI.isSelected());
		gerarCSV.setBoPARANA(boPARANA.isSelected());
		gerarCSV.setBoRIO_JANEIRO(boRIO_JANEIRO.isSelected());
		gerarCSV.setBoRIO_GRANDE_NORTE(boRIO_GRANDE_NORTE.isSelected());
		gerarCSV.setBoRONDONIA(boRONDONIA.isSelected());
		gerarCSV.setBoRORAIMA(boRORAIMA.isSelected());
		gerarCSV.setBoRIO_GRANDE_SUL(boRIO_GRANDE_SUL.isSelected());
		gerarCSV.setBoSANTA_CATARINA(boSANTA_CATARINA.isSelected());
		gerarCSV.setBoSERGIPE(boSERGIPE.isSelected());
		gerarCSV.setBoSAO_PAULO(boSAO_PAULO.isSelected());
		gerarCSV.setBoTOCANTINS(boTOCANTINS.isSelected());
	}

}
