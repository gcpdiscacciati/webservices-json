package gcpd.webservices.cep;

import static poo.es.EntradaESaida.exibirTexto;
import static poo.es.EntradaESaida.lerString;
import static poo.es.EntradaESaida.msgErro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

public class ConsultaCEP {
	
	final static String INFORMA = "Informe o CEP",
			CONSULTA = "Consulta CEP",
			ERRO = "ERRO",
			ERRO_NAO_ENCONTRADO = "CEP Informado não foi encontrado.",
			ERRO_FORMATO = "CEP Inválido! Informe apenas números ou no formato XXXXX-XXX";	

	public ConsultaCEP() {
		// TODO Auto-generated constructor stub
	}

	private static void consulta() throws IOException {
		String cepString = "";

		do {
			// Recebe a string do usuário
			cepString = lerString(INFORMA, CONSULTA);
			
			if(cepString != null) {
				
				// Valida se a string fornecida está no formato correto
				if(cepString.matches("\\d{8}") || cepString.matches("\\d{5}-\\d{3}")) {
					cepString.replace("-", "");
					URL url = new URL("https://viacep.com.br/ws/" + cepString + "/json");
					URLConnection con = url.openConnection();
					BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
					String line;
					StringBuilder source = new StringBuilder();
					
					// Formata os dados recebidos em uma string
					while((line = input.readLine()) != null)
						source.append(line);
					input.close();
					
					// Transforma a string em um objeto JSON
					JSONObject respJSON = new JSONObject(source.toString());
					
					// Caso a resposta tenha sido bem sucedida, imprime em uma caixa de texto
					if(!respJSON.has("erro")) {
						StringBuilder dadosConsulta = new StringBuilder();
						dadosConsulta.append("CEP: " + respJSON.getString("cep") + "\n");
						dadosConsulta.append("Logradouro: " + respJSON.getString("logradouro") + "\n");
						dadosConsulta.append("Bairro: " + respJSON.getString("bairro") + "\n");
						dadosConsulta.append("Cidade: " + respJSON.getString("localidade"));
						exibirTexto(dadosConsulta.toString(), CONSULTA, 5, 20);
					}
					else {
						msgErro(ERRO_NAO_ENCONTRADO, ERRO);
					}
				}
				else {
					msgErro(ERRO_FORMATO, ERRO);
				}
			}
			else {
				System.exit(0);
			}
			
		}while(cepString != null);
		System.exit(0);
	}
	
	public static void main(String[] args) {
		try {
			consulta();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

}
