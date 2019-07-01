package br.com.montroni.cepResult.view;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import br.com.montroni.cepResult.CepResult;

public final class GerarCSV extends Thread {

    private static final GerarCSV INSTANCE = new GerarCSV();

    private boolean geraLog = false;

    private boolean boACRE;

    private boolean boALAGOAS;

    private boolean boAMAZONAS;

    private boolean boAMAPA;

    private boolean boBAHIA;

    private boolean boCEARA;

    private boolean boDISTRITO_FEDERAL;

    private boolean boESPIRITO_SANTO;

    private boolean boGOIAS;

    private boolean boMARANHAO;

    private boolean boMINAS_GERAIS;

    private boolean boMATO_GROSSO_SUL;

    private boolean boMATO_GROSSO;

    private boolean boPARA;

    private boolean boPARAIBA;

    private boolean boPERNAMBUCO;

    private boolean boPIAUI;

    private boolean boPARANA;

    private boolean boRIO_JANEIRO;

    private boolean boRIO_GRANDE_NORTE;

    private boolean boRONDONIA;

    private boolean boRORAIMA;

    private boolean boRIO_GRANDE_SUL;

    private boolean boSANTA_CATARINA;

    private boolean boSERGIPE;

    private boolean boSAO_PAULO;

    private boolean boTOCANTINS;

    private boolean pararExecucao;

    private static Double vltotalProcessamento;

    private static Double contador;

    public static String arquivo;

    private String usuario;

    private String senha;

    private String cep;
    
    private String txtAltura;
    
    private String txtLargura;
    
    private String txtComprimento;

    Map<String, String> mapaServicos = new HashMap<String, String>();

    public static GerarCSV getInstance() {
        return INSTANCE;
    }

    private GerarCSV() {

    }

    public void incluiServico(String pCodigo, String pDescricao) {
        mapaServicos.put(pCodigo, pDescricao);
    }

    public String listaServicoSelecionado() {
        String listaServicos = "";

        for (Map.Entry<String, String> entry : mapaServicos.entrySet()) {
            listaServicos = listaServicos + entry.getKey() + ",";
        }
        // remover a ultima virgula
        return listaServicos.substring(0, listaServicos.length() - 1);
    }

    public void run() {

        ArrayList<Cep> listaCep = listaDeCepAhSerConsultado();
        ArrayList<Peso> listaPeso = listaDePesosAhSerConsultado();

        String pCd_Empresa = "";
        String pCd_Senha = "";
        String pCep_Origem = "86701390";

        pCd_Empresa = getUsuario();
        pCd_Senha = getSenha();
        pCep_Origem = getCep();

        pararExecucao = false;

        String pCep_Destino = "64100001";
        String pCd_Formato = "1";
        String pVl_Altura = txtAltura;
        String pVl_largura = txtLargura;
        String pVlComprimento = txtComprimento;
        String pcdMaoPropria = "n";
        String pvlValorDeclarado = "0";
        String pcdAvisoRecibimento = "n";

        String pvldiametro = "0";
        String pStrRetorno = "xml";
        String pindicaCalcula = "3";
        String consulta;

        Client client = Client.create();

        OutputStream out = null;
        try {
            out = new FileOutputStream(getArquivo());

            String cabecalho = "Descrição da Região;CEP INICIAL;CEP FINAL;TRANSPORTADORA;TIPO DE SERVIÇO;PESO INICIAL;PESO FINAL;VALOR;PRAZO" + (isGeraLog() ? ";Pesquisa Servico" : "");
            
            for (Byte b : cabecalho.getBytes("ISO-8859-1")) {
                out.write(b);
            }
            
            out.write('\n');
            
            vltotalProcessamento = (double) (listaCep.size() * listaPeso.size());
            
            for (Cep c : listaCep) {
                pCep_Destino = c.getCepConsulta();
                for (Peso p : listaPeso) {
                    addContador();
                    consulta = "http://ws.correios.com.br/calculador/CalcPrecoPrazo.aspx?nCdEmpresa=" + pCd_Empresa
                            + "&sDsSenha=" + pCd_Senha + "&sCepOrigem=" + pCep_Origem + "&sCepDestino="
                            + pCep_Destino + "&nVlPeso=" + p.getPesoConsulta() + "&nCdFormato=" + pCd_Formato
                            + "&nVlComprimento=" + pVlComprimento + "&nVlAltura=" + pVl_Altura + "&nVlLargura="
                            + pVl_largura + "&sCdMaoPropria=" + pcdMaoPropria + "&nVlValorDeclarado="
                            + pvlValorDeclarado + "&sCdAvisoRecebimento=" + pcdAvisoRecibimento + "&nCdServico="
                            + listaServicoSelecionado() + "&nVlDiametro=" + pvldiametro + "&StrRetorno=" + pStrRetorno
                            + "&nIndicaCalculo=" + pindicaCalcula + "";

                    WebResource webResource = client.resource(consulta);

                    String s = webResource.get(String.class);
                    Document doc = Jsoup.parse(s, "", Parser.xmlParser());
                    for (Object obj : doc.childNodes().get(2).childNodes().toArray()) {
                        boolean logAtivo = false;
                        if (logAtivo) {
                            System.out.println("Codigo: " + ((Element) obj).select("Codigo").text());
                            System.out.println("Valor: " + ((Element) obj).select("Valor").text());
                            System.out.println("PrazoEntrega: " + ((Element) obj).select("PrazoEntrega").text());
                            System.out.println(
                                    "ValorSemAdicionais: " + ((Element) obj).select("ValorSemAdicionais").text());
                            System.out.println("ValorMaoPropria: " + ((Element) obj).select("ValorMaoPropria").text());
                            System.out.println(
                                    "ValorAvisoRecebimento: " + ((Element) obj).select("ValorAvisoRecebimento").text());
                            System.out.println(
                                    "ValorValorDeclarado: " + ((Element) obj).select("ValorValorDeclarado").text());
                            System.out.println(
                                    "EntregaDomiciliar : " + ((Element) obj).select("EntregaDomiciliar").text());
                            System.out.println("Erro : " + ((Element) obj).select("Erro").text());
                            System.out.println("MsgErro: " + ((Element) obj).select("MsgErro").text());
                            System.out.println("obsFim : " + ((Element) obj).select("obsFim").text());
                        }
                        CepResult cep = new CepResult();
                        cep.setCodigo(((Element) obj).select("Codigo").text());
                        cep.setEntregaDomiciliar(((Element) obj).select("EntregaDomiciliar").text());
                        cep.setEntregaSabado("NI");
                        cep.setPrazoEntrega(((Element) obj).select("PrazoEntrega").text());
                        cep.setValor(((Element) obj).select("Valor").text());
                        cep.setValorAvisoRecebimento(((Element) obj).select("ValorValorDeclarado").text());
                        cep.setValorMaoPropria(((Element) obj).select("ValorMaoPropria").text());
                        cep.setValorSemAdiciona(((Element) obj).select("ValorSemAdicionais").text());
                        cep.setValorValorDeclarado(((Element) obj).select("ValorValorDeclarado").text());
                        if (cep.getValor().toString().equals("0,00".toString())) {
                            cep.setValor("Retorno com valor zerado");
                        }
                        String linha = c.getCepDescricao() + ";"
                                + c.getCepInicial() + ";" + c.getCepFinal()
                                + ";CORREIOS;"
                                + mapaServicos.get(cep.getCodigo()) + ";"
                                + p.getPesoInicial() + ";" + p.getPesoFinal() + ";"
                                + cep.getValor() + ";"
                                + cep.getPrazoEntrega()
                                + (isGeraLog() ? ";" + consulta : "");

                        for (byte b1 : linha.getBytes("ISO-8859-1")) {
                            out.write(b1);
                        }
                        out.write('\n');
                    };

                    if (pararExecucao) {
                        out.close();
                        pararExecucao = false;
                        return;
                    }

                }
            }
            out.close();

        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage());
        }
    }

    private ArrayList<Peso> listaDePesosAhSerConsultado() {
        ArrayList<Peso> listaPeso = new ArrayList<Peso>();
        listaPeso.add(new Peso("0", "0,5", "0,4"));
        listaPeso.add(new Peso("0,501", "1", "0,511"));
        listaPeso.add(new Peso("1,001", "1,5", "1,011"));
        listaPeso.add(new Peso("1,501", "2", "1,511"));
        listaPeso.add(new Peso("2,001", "3", "2,011"));
        listaPeso.add(new Peso("3,001", "4", "3,011"));
        listaPeso.add(new Peso("4,001", "5", "4,011"));
        listaPeso.add(new Peso("5,001", "6", "5,011"));
        listaPeso.add(new Peso("6,001", "7", "6,011"));
        listaPeso.add(new Peso("7,001", "8", "7,011"));
        listaPeso.add(new Peso("8,001", "9", "8,011"));
        listaPeso.add(new Peso("9,001", "10", "9,011"));
        listaPeso.add(new Peso("10,001", "11", "10,011"));
        listaPeso.add(new Peso("11,001", "12", "11,011"));
        listaPeso.add(new Peso("12,001", "13", "12,011"));
        listaPeso.add(new Peso("13,001", "14", "13,011"));
        listaPeso.add(new Peso("14,001", "15", "14,011"));
        listaPeso.add(new Peso("15,001", "16", "15,011"));
        listaPeso.add(new Peso("16,001", "17", "16,011"));
        listaPeso.add(new Peso("17,001", "18", "17,011"));
        listaPeso.add(new Peso("18,001", "19", "18,011"));
        listaPeso.add(new Peso("19,001", "20", "19,011"));
        listaPeso.add(new Peso("20,001", "21", "20,011"));
        listaPeso.add(new Peso("21,001", "22", "21,011"));
        listaPeso.add(new Peso("22,001", "23", "22,011"));
        listaPeso.add(new Peso("23,001", "24", "23,011"));
        listaPeso.add(new Peso("24,001", "25", "24,011"));
        listaPeso.add(new Peso("25,001", "26", "25,011"));
        listaPeso.add(new Peso("26,001", "27", "26,011"));
        listaPeso.add(new Peso("27,001", "28", "27,011"));
        listaPeso.add(new Peso("28,001", "29", "28,011"));
        listaPeso.add(new Peso("29,001", "30", "29,011"));
        return listaPeso;
    }

    private ArrayList<Cep> listaDeCepAhSerConsultado() {
        ArrayList<Cep> listaCep = new ArrayList<Cep>();
        if (boACRE) {
            listaCep.add(new Cep("69900001", "69924999", "69900001", "AC - Capital"));
            listaCep.add(new Cep("69925000", "69999999", "69925001", "AC - Interior"));
        }

        if (boALAGOAS) {
            listaCep.add(new Cep("57000001", "57099999", "57000001", "AL - Capital"));
            listaCep.add(new Cep("57100000", "57199999", "57100001", "AL - Regiao Metropolitana"));
            listaCep.add(new Cep("57200000", "57924999", "57200001", "AL - Interior"));
            listaCep.add(new Cep("57925000", "57929999", "57925001", "AL - Regiao Metropolitana 2"));
            listaCep.add(new Cep("57930000", "57934999", "57930001", "AL - Interior 2"));
            listaCep.add(new Cep("57935000", "57939999", "57935001", "AL - Regiao Metropolitana 3"));
            listaCep.add(new Cep("57940000", "57989999", "57940001", "AL - Interior 3"));
            listaCep.add(new Cep("57990000", "57994999", "57990001", "AL - Regiao Metropolitana 4"));
            listaCep.add(new Cep("57995000", "57999999", "57995001", "AL - Interior 4"));
        }

        if (boAMAZONAS) {
            listaCep.add(new Cep("69000001", "69099999", "69000001", "AM - Capital"));
            listaCep.add(new Cep("69100000", "69254999", "69100001", "AM - Interior"));
            listaCep.add(new Cep("69255000", "69259999", "69255001", "AM - Regiao Metropolitana"));
            listaCep.add(new Cep("69260000", "69414999", "69260001", "AM - Interior 2"));
            listaCep.add(new Cep("69415000", "69424999", "69415001", "AM - Regiao Metropolitana 2"));
            listaCep.add(new Cep("69390000", "69899999", "69390001", "AM - Interior 3"));
        }

        if (boAMAPA) {
            listaCep.add(new Cep("68900001", "68914999", "68900001", "AP - Capital"));
            listaCep.add(new Cep("68915000", "68925000", "68915001", "AP - Interior"));
            listaCep.add(new Cep("68925001", "68939999", "68925001", "AP - Regiao Metropolitana"));
            listaCep.add(new Cep("68940000", "68999999", "68940001", "AP - Interior 2"));
        }

        if (boBAHIA) {
            listaCep.add(new Cep("40000001", "42599999", "40000001", "BA - Capital"));
            listaCep.add(new Cep("42600000", "43899999", "42600001", "BA - Regiao Metropolitana"));
            listaCep.add(new Cep("43900000", "44459999", "43900000", "BA - Interior"));
            listaCep.add(new Cep("44460000", "44479999", "44460001", "BA - Regiao Metropolitana 2"));
            listaCep.add(new Cep("44480000", "48999999", "44480001", "BA - Interior 2"));
        }

        if (boCEARA) {
            listaCep.add(new Cep("60000000", "61599999", "60000001", "CE - Capital"));
            listaCep.add(new Cep("61600001", "61999999", "61600001", "CE - Regiao Metropolitana"));
            listaCep.add(new Cep("62000000", "62669999", "62000001", "CE - Interior"));
            listaCep.add(new Cep("62670000", "62679999", "62670001", "CE - Regiao Metropolitana 2"));
            listaCep.add(new Cep("62680000", "62859999", "62680001", "CE - Interior 2"));
            listaCep.add(new Cep("62860000", "62874999", "62860001", "CE - Regiao Metropolitana 3"));
            listaCep.add(new Cep("62875000", "62880000", "62875001", "CE - Interior 3"));
            listaCep.add(new Cep("62880001", "62899999", "62880001", "CE - Regiao Metropolitana 4"));
            listaCep.add(new Cep("62900000", "63999999", "62900001", "CE - Interior 4"));
        }

        if (boDISTRITO_FEDERAL) {
            listaCep.add(new Cep("70000000", "72799999", "70000001", "DF - Brasilia"));
            listaCep.add(new Cep("73000001", "73699999", "73000001", "DF - Regiao Metropolitana"));
        }

        if (boESPIRITO_SANTO) {
            listaCep.add(new Cep("29000000", "29099999", "29000001", "ES - Capital"));
            listaCep.add(new Cep("29100001", "29189999", "29100001", "ES - Regiao Metropolitana"));
            listaCep.add(new Cep("29190000", "29200000", "29190001", "ES - Interior"));
            listaCep.add(new Cep("29200001", "29229999", "29200001", "ES - Regiao Metropolitana 2"));
            listaCep.add(new Cep("29230000", "29999999", "29230001", "ES - Interior 2"));
        }

        if (boGOIAS) {
            listaCep.add(new Cep("74000001", "74899999", "74000001", "GO - Capital"));
            listaCep.add(new Cep("74900001", "75159999", "74900001", "GO - Regiao Metropolitana"));
            listaCep.add(new Cep("75160000", "75169999", "75160001", "GO - Interior"));
            listaCep.add(new Cep("75170000", "75179999", "75170001", "GO - Regiao Metropolitana 2"));
            listaCep.add(new Cep("75180000", "75194999", "75180001", "GO - Interior 2"));
            listaCep.add(new Cep("75195000", "75199999", "75195001", "GO - Regiao Metropolitana 3"));
            listaCep.add(new Cep("75200000", "75239999", "75200001", "GO - Interior 3"));
            listaCep.add(new Cep("75240000", "75264999", "75240001", "GO - Regiao Metropolitana 4"));
            listaCep.add(new Cep("75265000", "75339999", "75265001", "GO - Interior 4"));
            listaCep.add(new Cep("75340000", "75354999", "75340001", "GO - Regiao Metropolitana 5"));
            listaCep.add(new Cep("75355000", "75359999", "75355001", "GO - Interior 5"));
            listaCep.add(new Cep("75360000", "75394999", "75360001", "GO - Regiao Metropolitana 6"));
            listaCep.add(new Cep("75395000", "75399999", "75395001", "GO - Interior 6"));
            listaCep.add(new Cep("75400000", "75409999", "75400001", "GO - Regiao Metropolitana 7"));
            listaCep.add(new Cep("75410000", "75429999", "75410001", "GO - Interior 7"));
            listaCep.add(new Cep("75430000", "75449999", "75430001", "GO - Regiao Metropolitana 8"));
            listaCep.add(new Cep("75450000", "75459999", "75450001", "GO - Interior 8"));
            listaCep.add(new Cep("75460000", "75479999", "75460001", "GO - Regiao Metropolitana 9"));
            listaCep.add(new Cep("75480000", "76759999", "75480001", "GO - Interior 9"));
        }

        if (boMARANHAO) {
            listaCep.add(new Cep("65000001", "65109999", "65000001", "MA - Capital"));
            listaCep.add(new Cep("65110000", "65139999", "65110001", "MA - Regiao Metropolitana"));
            listaCep.add(new Cep("65140000", "65249999", "65140001", "MA - Interior"));
            listaCep.add(new Cep("65250000", "65254999", "65250001", "MA - Regiao Metropolitana 2"));
            listaCep.add(new Cep("65255000", "65630000", "65255001", "MA - Interior 2"));
            listaCep.add(new Cep("65630001", "65639999", "65630001", "MA - Regiao Metropolitana 3"));
            listaCep.add(new Cep("65640000", "65999999", "65640001", "MA - Interior 3"));
        }

        if (boMINAS_GERAIS) {
            listaCep.add(new Cep("30000001", "31999999", "30000001", "MG - Capital"));
            listaCep.add(new Cep("32000001", "32499999", "32000001", "MG - Regiao Metropolitana"));
            listaCep.add(new Cep("32500000", "32600001", "32500000", "MG - Interior"));
            listaCep.add(new Cep("32600001", "32699999", "32600001", "MG - Regiao Metropolitana 2"));
            listaCep.add(new Cep("32699999", "32600001", "32699999", "MG - Interior 2"));
        }

        if (boMATO_GROSSO_SUL) {
            listaCep.add(new Cep("79000000", "79129999", "79000001", "MS - Capital"));
            listaCep.add(new Cep("79130000", "79999999", "79130001", "MS - Interior"));
        }

        if (boMATO_GROSSO) {
            listaCep.add(new Cep("78000001", "78109999", "78000001", "MT - Capital"));
            listaCep.add(new Cep("78110001", "78174999", "78110001", "MT - Regiao Metropolitana"));
            listaCep.add(new Cep("78175000", "78179999", "78175001", "MT - Interior"));
            listaCep.add(new Cep("78180000", "78189999", "78180001", "MT - Regiao Metropolitana 2"));
            listaCep.add(new Cep("78190000", "78899999", "78190001", "MT - Interior 2"));
        }

        if (boPARA) {
            listaCep.add(new Cep("66000001", "66999999", "66000001", "PA - Capital"));
            listaCep.add(new Cep("67000001", "67999999", "67000001", "PA - Regiao Metropolitana"));
            listaCep.add(new Cep("68000000", "68789999", "68000001", "PA - Interior"));
            listaCep.add(new Cep("68790000", "68799999", "68790001", "PA - Regiao Metropolitana 2"));
            listaCep.add(new Cep("68800000", "68899999", "68800001", "PA - Interior 2"));
        }

        if (boPARAIBA) {
            listaCep.add(new Cep("58000000", "58099999", "58000001", "PB - Capital"));
            listaCep.add(new Cep("58100000", "58109999", "58100001", "PB - Regiao Metropolitana"));
            listaCep.add(new Cep("58110000", "58279999", "58110001", "PB - Interior"));
            listaCep.add(new Cep("58280000", "58286999", "58280001", "PB - Regiao Metropolitana 2"));
            listaCep.add(new Cep("58287000", "58296999", "58287001", "PB - Interior 2"));
            listaCep.add(new Cep("58297000", "58309999", "58297001", "PB - Regiao Metropolitana 3"));
            listaCep.add(new Cep("58310000", "58314999", "58310001", "PB - Interior 3"));
            listaCep.add(new Cep("58315000", "58329999", "58315001", "PB - Regiao Metropolitana 4"));
            listaCep.add(new Cep("58330000", "58336999", "58330001", "PB - Interior 4"));
            listaCep.add(new Cep("58337000", "58337999", "58337001", "PB - Regiao Metropolitana 5"));
            listaCep.add(new Cep("58338000", "58996999", "58338001", "PB - Interior 5"));
        }

        if (boPERNAMBUCO) {
            listaCep.add(new Cep("50000000", "52999999", "50000001", "PE - Capital"));
            listaCep.add(new Cep("53000000", "53989999", "53000001", "PE - Regiao Metropolitana"));
            listaCep.add(new Cep("53990000", "54000000", "53990001", "PE - Interior"));
            listaCep.add(new Cep("54000001", "54599999", "54000001", "PE - Regiao Metropolitana 2"));
            listaCep.add(new Cep("54600000", "54700000", "54600001", "PE - Interior 2"));
            listaCep.add(new Cep("54700001", "54999999", "54700001", "PE - Regiao Metropolitana 3"));
            listaCep.add(new Cep("55000000", "55589999", "55000001", "PE - Interior 3"));
            listaCep.add(new Cep("55590000", "55599999", "55590001", "PE - Regiao Metropolitana 4"));
            listaCep.add(new Cep("55600000", "56999999", "55600001", "PE - Interior 4"));
        }

        if (boPIAUI) {
            listaCep.add(new Cep("64000000", "64099999", "64000001", "PI - Capital"));
            listaCep.add(new Cep("64100000", "64109999", "64100001", "PI - Interior"));
            listaCep.add(new Cep("64110000", "64119999", "64110001", "PI - Regiao Metropolitana"));
            listaCep.add(new Cep("64120000", "64289999", "64120001", "PI - Interior 2"));
            listaCep.add(new Cep("64290000", "64294999", "64290001", "PI - Regiao Metropolitana 2"));
            listaCep.add(new Cep("64295000", "64334999", "64295001", "PI - Interior 3"));
            listaCep.add(new Cep("64335000", "64339999", "64335001", "PI - Regiao Metropolitana 3"));
            listaCep.add(new Cep("64340000", "64387999", "64340001", "PI - Interior 4"));
            listaCep.add(new Cep("64388000", "64394999", "64388001", "PI - Regiao Metropolitana 4"));
            listaCep.add(new Cep("64395000", "65630000", "64395001", "PI - Interior 5"));
        }

        if (boPARANA) {
            listaCep.add(new Cep("80000001", "82999999", "80000001", "PR - Capital"));
            listaCep.add(new Cep("83000001", "83189999", "83000001", "PR - Regiao Metropolitana"));
            listaCep.add(new Cep("83190000", "83300000", "83190001", "PR - Interior"));
            listaCep.add(new Cep("83300001", "83349999", "83300001", "PR - Regiao Metropolitana 1"));
            listaCep.add(new Cep("83350000", "83400000", "83350001", "PR - Interior 1"));
            listaCep.add(new Cep("83400001", "83479999", "83400001", "PR - Regiao Metropolitana 2"));
            listaCep.add(new Cep("83480000", "83500000", "83480001", "PR - Interior 2"));
            listaCep.add(new Cep("83500001", "83569999", "83500001", "PR - Regiao Metropolitana 3"));
            listaCep.add(new Cep("83570000", "83600000", "83570001", "PR - Interior 3"));
            listaCep.add(new Cep("83600001", "83749999", "83600001", "PR - Regiao Metropolitana 4"));
            listaCep.add(new Cep("83750000", "83799999", "83750001", "PR - Interior 4"));
            listaCep.add(new Cep("83800000", "83839999", "83800001", "PR - Regiao Metropolitana 5"));
            listaCep.add(new Cep("83840000", "87999999", "83840001", "PR - Interior 5"));
        }

        if (boRIO_JANEIRO) {
            listaCep.add(new Cep("20000000", "23799999", "20000001", "RJ - Capital"));
            listaCep.add(new Cep("23800000", "23890000", "23800001", "RJ - Interior"));
            listaCep.add(new Cep("23890001", "23899999", "23890001", "RJ - Regiao Metropolitana"));
            listaCep.add(new Cep("23900000", "24000000", "23900001", "RJ - Interior 2"));
            listaCep.add(new Cep("24000001", "25779999", "24000001", "RJ - Regiao Metropolitana 2"));
            listaCep.add(new Cep("25780000", "25900000", "25780001", "RJ - Interior 3"));
            listaCep.add(new Cep("25900001", "25949999", "25900001", "RJ - Regiao Metropolitana 3"));
            listaCep.add(new Cep("25950000", "26000000", "25950001", "RJ - Interior 4"));
            listaCep.add(new Cep("26000001", "26599999", "26000001", "RJ - Regiao Metropolitana 4"));
            listaCep.add(new Cep("26600000", "28999999", "26600001", "RJ - Interior 5"));
        }

        if (boRIO_GRANDE_NORTE) {
            listaCep.add(new Cep("59000001", "59139999", "59000001", "RN - Capital"));
            listaCep.add(new Cep("59140001", "59167999", "59140001", "RN - Regiao Metropolitana"));
            listaCep.add(new Cep("59168000", "59181999", "59168001", "RN - Interior"));
            listaCep.add(new Cep("59182000", "59184999", "59182001", "RN - Regiao Metropolitana 2"));
            listaCep.add(new Cep("59185000", "59279999", "59185001", "RN - Interior 2"));
            listaCep.add(new Cep("59280000", "59299999", "59280001", "RN - Regiao Metropolitana 3"));
            listaCep.add(new Cep("59300000", "59569999", "59300001", "RN - Interior 3"));
            listaCep.add(new Cep("59570000", "59577999", "59570001", "RN - Regiao Metropolitana 4"));
            listaCep.add(new Cep("59578000", "59999999", "59578001", "RN - Interior 4"));
        }

        if (boRONDONIA) {
            listaCep.add(new Cep("76800001", "76849999", "76800001", "RO - Capital"));
            listaCep.add(new Cep("76850000", "76999999", "76850001", "RO - Interior"));
        }

        if (boRORAIMA) {
            listaCep.add(new Cep("69300001", "69339999", "69300001", "RR - Capital"));
            listaCep.add(new Cep("69340000", "69399999", "69340001", "RR - Interior"));
        }

        if (boRIO_GRANDE_SUL) {
            listaCep.add(new Cep("90000001", "91999999", "90000001", "RS - Capital"));
            listaCep.add(new Cep("92000001", "92849999", "92000001", "RS - Regiao Metropolitana"));
            listaCep.add(new Cep("92850000", "92989999", "92850001", "RS - Interior"));
            listaCep.add(new Cep("92990000", "93879999", "92990001", "RS - Regiao Metropolitana 2"));
            listaCep.add(new Cep("93880000", "93899999", "93880001", "RS - Interior 2"));
            listaCep.add(new Cep("93900000", "93939999", "93900001", "RS - Regiao Metropolitana 3"));
            listaCep.add(new Cep("93940000", "93949999", "93940001", "RS - Interior 3"));
            listaCep.add(new Cep("93950000", "93989999", "93950001", "RS - Regiao Metropolitana 4"));
            listaCep.add(new Cep("93990000", "94000000", "93990001", "RS - Interior 4"));
            listaCep.add(new Cep("94000001", "94999999", "94000001", "RS - Regiao Metropolitana 5"));
            listaCep.add(new Cep("95000000", "95744999", "95000001", "RS - Interior 5"));
            listaCep.add(new Cep("95745000", "95747999", "95745001", "RS - Regiao Metropolitana 6"));
            listaCep.add(new Cep("95748000", "95779999", "95748001", "RS - Interior 6"));
            listaCep.add(new Cep("95780000", "95782999", "95780001", "RS - Regiao Metropolitana 7"));
            listaCep.add(new Cep("95783000", "95839999", "95783001", "RS - Interior 7"));
            listaCep.add(new Cep("95840000", "95859999", "95840001", "RS - Regiao Metropolitana 8"));
            listaCep.add(new Cep("95860000", "96699999", "95860001", "RS - Interior 8"));
            listaCep.add(new Cep("96700000", "96734999", "96700001", "RS - Regiao Metropolitana 9"));
            listaCep.add(new Cep("96735000", "96739999", "96735001", "RS - Interior 9"));
            listaCep.add(new Cep("96740000", "96749999", "96740001", "RS - Regiao Metropolitana 10"));
            listaCep.add(new Cep("96750000", "99999999", "96750001", "RS - Interior 10"));
        }

        if (boSANTA_CATARINA) {
            listaCep.add(new Cep("88000001", "88099999", "88000001", "SC - Capital"));
            listaCep.add(new Cep("88100001", "88209999", "88100001", "SC - Regiao Metropolitana"));
            listaCep.add(new Cep("88210000", "88229999", "88210000", "SC - Interior"));
            listaCep.add(new Cep("88230000", "88294999", "88230000", "SC - Regiao Metropolitana 2"));
            listaCep.add(new Cep("88295000", "88459999", "88295000", "SC - Interior 2"));
            listaCep.add(new Cep("88460000", "88474999", "88460000", "SC - Regiao Metropolitana 3"));
            listaCep.add(new Cep("88475000", "88484999", "88475000", "SC - Interior 3"));
            listaCep.add(new Cep("88485000", "88499999", "88485000", "SC - Regiao Metropolitana 4"));
            listaCep.add(new Cep("88500000", "89999999", "88500000", "SC - Interior 4"));
        }

        if (boSERGIPE) {
            listaCep.add(new Cep("49000001", "49099999", "49000001", "SE - Capital"));
            listaCep.add(new Cep("49100000", "49119999", "49100000", "SE - Regiao Metropolitana"));
            listaCep.add(new Cep("49120000", "49139999", "49120000", "SE - Interior"));
            listaCep.add(new Cep("49140000", "49169999", "49140000", "SE - Regiao Metropolitana 2"));
            listaCep.add(new Cep("49170000", "49999999", "49170000", "SE - Interior 2"));
        }

        if (boSAO_PAULO) {
            listaCep.add(new Cep("01000001", "05999999", "01000001", "SP - Capital"));
            listaCep.add(new Cep("06000001", "06949999", "06000001", "SP - Regiao Metropolitana"));
            listaCep.add(new Cep("06950000", "07000000", "06950000", "SP - Interior"));
            listaCep.add(new Cep("07000001", "08899999", "07000001", "SP - Regiao Metropolitana 2"));
            listaCep.add(new Cep("08900000", "09000000", "08900000", "SP - Interior 2"));
            listaCep.add(new Cep("09000001", "09999999", "09000001", "SP - Regiao Metropolitana 3"));
            listaCep.add(new Cep("10000000", "11000000", "10000000", "SP - Interior 3"));
            listaCep.add(new Cep("11000001", "11249999", "11000001", "SP - Regiao Metropolitana 4"));
            listaCep.add(new Cep("11250000", "11300000", "11250000", "SP - Interior 4"));
            listaCep.add(new Cep("11300001", "11499999", "11300001", "SP - Regiao Metropolitana 5"));
            listaCep.add(new Cep("11500000", "12200000", "11500000", "SP - Interior 5"));
            listaCep.add(new Cep("12200001", "12249999", "12200001", "SP - Regiao Metropolitana 6"));
            listaCep.add(new Cep("12250000", "12300000", "12250000", "SP - Interior 6"));
            listaCep.add(new Cep("12300001", "12349999", "12300001", "SP - Regiao Metropolitana 7"));
            listaCep.add(new Cep("12350000", "12900000", "12350000", "SP - Interior 7"));
            listaCep.add(new Cep("12900001", "12929999", "12900001", "SP - Regiao Metropolitana 8"));
            listaCep.add(new Cep("12930000", "12940000", "12930000", "SP - Interior 8"));
            listaCep.add(new Cep("12940001", "12954999", "12940001", "SP - Regiao Metropolitana 9"));
            listaCep.add(new Cep("12955000", "13000000", "12955000", "SP - Interior 9"));
            listaCep.add(new Cep("13000001", "13219999", "13000001", "SP - Regiao Metropolitana 10"));
            listaCep.add(new Cep("13220000", "13250000", "13220000", "SP - Interior 10"));
            listaCep.add(new Cep("13250001", "13259999", "13250001", "SP - Regiao Metropolitana 11"));
            listaCep.add(new Cep("13260000", "13270000", "13260000", "SP - Interior 11"));
            listaCep.add(new Cep("13270001", "13289999", "13270001", "SP - Regiao Metropolitana 12"));
            listaCep.add(new Cep("13290000", "13300000", "13290000", "SP - Interior 12"));
            listaCep.add(new Cep("13300001", "13314999", "13300001", "SP - Regiao Metropolitana 13"));
            listaCep.add(new Cep("13315000", "13320000", "13315000", "SP - Interior 13"));
            listaCep.add(new Cep("13320001", "13349999", "13320001", "SP - Regiao Metropolitana 14"));
            listaCep.add(new Cep("13350000", "13380000", "13350000", "SP - Interior 14"));
            listaCep.add(new Cep("13380001", "13389999", "13380001", "SP - Regiao Metropolitana 15"));
            listaCep.add(new Cep("13390000", "13400000", "13390000", "SP - Interior 15"));
            listaCep.add(new Cep("13400001", "13439999", "13400001", "SP - Regiao Metropolitana 16"));
            listaCep.add(new Cep("13440000", "13445000", "13440000", "SP - Interior 17"));
            listaCep.add(new Cep("13445001", "13489999", "13445001", "SP - Regiao Metropolitana 18"));
            listaCep.add(new Cep("13490000", "13500000", "13490000", "SP - Interior 18"));
            listaCep.add(new Cep("13500001", "13509999", "13500001", "SP - Regiao Metropolitana 19"));
            listaCep.add(new Cep("13510000", "13600000", "13510000", "SP - Interior 19"));
            listaCep.add(new Cep("13600001", "13609999", "13600001", "SP - Regiao Metropolitana 20"));
            listaCep.add(new Cep("13610000", "13800000", "13610000", "SP - Interior 20"));
            listaCep.add(new Cep("13800001", "13819999", "13800001", "SP - Regiao Metropolitana 21"));
            listaCep.add(new Cep("13820000", "13824999", "13820000", "SP - Interior 21"));
            listaCep.add(new Cep("13825000", "13834999", "13825000", "SP - Regiao Metropolitana 22"));
            listaCep.add(new Cep("13835000", "13840000", "13835000", "SP - Interior 22"));
            listaCep.add(new Cep("13840001", "13856999", "13840001", "SP - Regiao Metropolitana 23"));
            listaCep.add(new Cep("13857000", "13910000", "13857000", "SP - Interior 23"));
            listaCep.add(new Cep("13910001", "13929999", "13910001", "SP - Regiao Metropolitana 24"));
            listaCep.add(new Cep("13930000", "18000000", "13930000", "SP - Interior 24"));
            listaCep.add(new Cep("18000001", "18119999", "18000001", "SP - Regiao Metropolitana 25"));
            listaCep.add(new Cep("18120000", "19999999", "18120000", "SP - Interior 25"));
            listaCep.add(new Cep("37640000", "37649999", "37640000", "SP - EXTREMAMG"));
        }
        if (boTOCANTINS) {
            listaCep.add(new Cep("77000001", "77270999", "77000001", "TO - Capital"));
            listaCep.add(new Cep("77271000", "77999999", "77271001", "TO - Interior"));
        }
        return listaCep;
    }

    private void addContador() {
        GerarCSV.setContador(contador + 1);
    }

    public static String getArquivo() {
        return arquivo;
    }

    public static void setArquivo(String arquivo) {
        GerarCSV.arquivo = arquivo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setLog(boolean log) {
        this.setGeraLog(log);
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void pararExecucao() {
        this.pararExecucao = true;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public boolean isGeraLog() {
        return geraLog;
    }

    public void setGeraLog(boolean geraLog) {
        this.geraLog = geraLog;
    }

    public boolean isBoACRE() {
        return boACRE;
    }

    public void setBoACRE(boolean boACRE) {
        this.boACRE = boACRE;
    }

    public boolean isBoALAGOAS() {
        return boALAGOAS;
    }

    public void setBoALAGOAS(boolean boALAGOAS) {
        this.boALAGOAS = boALAGOAS;
    }

    public boolean isBoAMAZONAS() {
        return boAMAZONAS;
    }

    public void setBoAMAZONAS(boolean boAMAZONAS) {
        this.boAMAZONAS = boAMAZONAS;
    }

    public boolean isBoAMAPA() {
        return boAMAPA;
    }

    public void setBoAMAPA(boolean boAMAPA) {
        this.boAMAPA = boAMAPA;
    }

    public boolean isBoBAHIA() {
        return boBAHIA;
    }

    public void setBoBAHIA(boolean boBAHIA) {
        this.boBAHIA = boBAHIA;
    }

    public boolean isBoCEARA() {
        return boCEARA;
    }

    public void setBoCEARA(boolean boCEARA) {
        this.boCEARA = boCEARA;
    }

    public boolean isBoDISTRITO_FEDERAL() {
        return boDISTRITO_FEDERAL;
    }

    public void setBoDISTRITO_FEDERAL(boolean boDISTRITO_FEDERAL) {
        this.boDISTRITO_FEDERAL = boDISTRITO_FEDERAL;
    }

    public boolean isBoESPIRITO_SANTO() {
        return boESPIRITO_SANTO;
    }

    public void setBoESPIRITO_SANTO(boolean boESPIRITO_SANTO) {
        this.boESPIRITO_SANTO = boESPIRITO_SANTO;
    }

    public boolean isBoGOIAS() {
        return boGOIAS;
    }

    public void setBoGOIAS(boolean boGOIAS) {
        this.boGOIAS = boGOIAS;
    }

    public boolean isBoMARANHAO() {
        return boMARANHAO;
    }

    public void setBoMARANHAO(boolean boMARANHAO) {
        this.boMARANHAO = boMARANHAO;
    }

    public boolean isBoMINAS_GERAIS() {
        return boMINAS_GERAIS;
    }

    public void setBoMINAS_GERAIS(boolean boMINAS_GERAIS) {
        this.boMINAS_GERAIS = boMINAS_GERAIS;
    }

    public boolean isBoMATO_GROSSO_SUL() {
        return boMATO_GROSSO_SUL;
    }

    public void setBoMATO_GROSSO_SUL(boolean boMATO_GROSSO_SUL) {
        this.boMATO_GROSSO_SUL = boMATO_GROSSO_SUL;
    }

    public boolean isBoMATO_GROSSO() {
        return boMATO_GROSSO;
    }

    public void setBoMATO_GROSSO(boolean boMATO_GROSSO) {
        this.boMATO_GROSSO = boMATO_GROSSO;
    }

    public boolean isBoPARA() {
        return boPARA;
    }

    public void setBoPARA(boolean boPARA) {
        this.boPARA = boPARA;
    }

    public boolean isBoPARAIBA() {
        return boPARAIBA;
    }

    public void setBoPARAIBA(boolean boPARAIBA) {
        this.boPARAIBA = boPARAIBA;
    }

    public boolean isBoPERNAMBUCO() {
        return boPERNAMBUCO;
    }

    public void setBoPERNAMBUCO(boolean boPERNAMBUCO) {
        this.boPERNAMBUCO = boPERNAMBUCO;
    }

    public boolean isBoPIAUI() {
        return boPIAUI;
    }

    public void setBoPIAUI(boolean boPIAUI) {
        this.boPIAUI = boPIAUI;
    }

    public boolean isBoPARANA() {
        return boPARANA;
    }

    public void setBoPARANA(boolean boPARANA) {
        this.boPARANA = boPARANA;
    }

    public boolean isBoRIO_JANEIRO() {
        return boRIO_JANEIRO;
    }

    public void setBoRIO_JANEIRO(boolean boRIO_JANEIRO) {
        this.boRIO_JANEIRO = boRIO_JANEIRO;
    }

    public boolean isBoRIO_GRANDE_NORTE() {
        return boRIO_GRANDE_NORTE;
    }

    public void setBoRIO_GRANDE_NORTE(boolean boRIO_GRANDE_NORTE) {
        this.boRIO_GRANDE_NORTE = boRIO_GRANDE_NORTE;
    }

    public boolean isBoRONDONIA() {
        return boRONDONIA;
    }

    public void setBoRONDONIA(boolean boRONDONIA) {
        this.boRONDONIA = boRONDONIA;
    }

    public boolean isBoRORAIMA() {
        return boRORAIMA;
    }

    public void setBoRORAIMA(boolean boRORAIMA) {
        this.boRORAIMA = boRORAIMA;
    }

    public boolean isBoRIO_GRANDE_SUL() {
        return boRIO_GRANDE_SUL;
    }

    public void setBoRIO_GRANDE_SUL(boolean boRIO_GRANDE_SUL) {
        this.boRIO_GRANDE_SUL = boRIO_GRANDE_SUL;
    }

    public boolean isBoSANTA_CATARINA() {
        return boSANTA_CATARINA;
    }

    public void setBoSANTA_CATARINA(boolean boSANTA_CATARINA) {
        this.boSANTA_CATARINA = boSANTA_CATARINA;
    }

    public boolean isBoSERGIPE() {
        return boSERGIPE;
    }

    public void setBoSERGIPE(boolean boSERGIPE) {
        this.boSERGIPE = boSERGIPE;
    }

    public boolean isBoSAO_PAULO() {
        return boSAO_PAULO;
    }

    public void setBoSAO_PAULO(boolean boSAO_PAULO) {
        this.boSAO_PAULO = boSAO_PAULO;
    }

    public boolean isBoTOCANTINS() {
        return boTOCANTINS;
    }

    public void setBoTOCANTINS(boolean boTOCANTINS) {
        this.boTOCANTINS = boTOCANTINS;
    }

    public String getTxtAltura() {
        return txtAltura;
    }

    public void setTxtAltura(String txtAltura) {
        this.txtAltura = txtAltura;
    }

    public String getTxtLargura() {
        return txtLargura;
    }

    public void setTxtLargura(String txtLargura) {
        this.txtLargura = txtLargura;
    }

    public String getTxtComprimento() {
        return txtComprimento;
    }

    public void setTxtComprimento(String txtComprimento) {
        this.txtComprimento = txtComprimento;
    }

	public static Double getVltotalProcessamento() {
		return vltotalProcessamento;
	}

	public static void setVltotalProcessamento(Double vltotalProcessamento) {
		GerarCSV.vltotalProcessamento = vltotalProcessamento;
	}

	public static Double getContador() {
		return contador;
	}

	public static void setContador(Double contador) {
		GerarCSV.contador = contador;
	}

}
