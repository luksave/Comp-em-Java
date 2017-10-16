package analisadores;

import tabelas.*;
import dados.*;
import java.util.Stack;


public class Sintatico {
	
	public String analisadorSintatico() {
		
		//Pilha de estados do analisador sint�tico.
		Stack<Integer> estados = new Stack<Integer>();
		
		//Para navegar nas tabelas:
		int state;

		//O primeiro s�mbolo do buffer.
		Simbolo simbolo = Lexico.getLex(0);		//simbolos s�o os lexemas que s�o retornados.
		
		//O primeiro estado na pilha.
		estados.push(0);
		
		while(true) {
			
			//Encontre o estado no topo da pilha.
			state = estados.peek();
			
			//Se haver� empilhamento:
				//Pela regra definida na tabela ACTION, se o numero � maior que 0, indica empilhamento.
				//Confira tamb�m se n�o � o estado de aceita��o.
			if((getACTION(state, simbolo.getLexema()) > 0) && (getACTION(state, simbolo.getLexema()) != 151)) { 
				estados.push(getACTION(state, simbolo.getLexema()));
				simbolo = Lexico.getLex(Lexico.pos);	//Procura o proximo Lexema.
				
			}
			
			//Se haver� redu��o:
				//Pela regra definida na tabela ACTION, se o numero � menor que 0, indica empilhamento.
				//Se o numero for 0 � um falso empilhamento. O tratamento � feito aqui????
			else if(getACTION(state, simbolo.getLexema()) <= 0) {
				//Para voltar ao numero verdadeiro da produ��o a ser reduzida.
				int reduce = getACTION(state, simbolo.getLexema()) * (-1);
	
				//Encontra a senten�a que est� sendo reduzida.
				String sentenca = getSentenca(reduce);
				
				//Para encontrar a quantidade de s�mbolos de Beta.
				int qtdSimbolosBeta = 0;
				boolean isBeta = false;
				
				//Para guarda qual o n�o terminal.
				StringBuilder nonterminal = new StringBuilder();
				
				//Recupera a quantidade de simbolos Beta.
				for(int i = 0; i < sentenca.length(); i++) {
					//S� passa a considerar palavras ap�s ->.
					if(sentenca.charAt(i) == '-' && sentenca.charAt(i + 1) == '>')
						isBeta = true;
				
					if(sentenca.charAt(i) == ' ' && isBeta == true)
						qtdSimbolosBeta++;
				
				}
				
				int j = 0;
				//Para recuperar o n�o-terminal
				while(sentenca.charAt(j) != ' ') {
					nonterminal.append(sentenca.charAt(j));
					j++;
				}
				
				//Desempilha qtdSimbolosBeta de estados.
				for(int i = 0; i <= qtdSimbolosBeta; i++) 
					estados.pop();
				
				//Fa�a o estado t ser o topo da pilha.
				state = estados.peek();
				
				//Empilhe GOTO[t, A].
				estados.push(getGOTO(state, nonterminal.toString()));

				//Imprima a produ��o A -> B.
				System.out.println("Senten�a reduzida:"+sentenca);
				return sentenca;
			}
			
			//Se h� aceita��o
			else if(getACTION(state, simbolo.getLexema()) == 151) {
				System.out.println("Aceita��o");
				return "Aceita��o";
			}
			
			//Sen�o, h� erro.
			else{
				//Mostre o erro.
				System.out.println("Chame uma rotina de tratamento de erro");
				return "Erro";
			}
		}
		
	
		
	}
	
	public String getSentenca(int line) {//Recebe a linha a ser reduzida e devolve a produ��o
		
		EnumeracaoDaGramatica[] Enumeracao = new EnumeracaoDaGramatica[30];
		Enumeracao[1]  = new EnumeracaoDaGramatica(1,"P' -> P");
		Enumeracao[2]  = new EnumeracaoDaGramatica(2,"P -> inicio V A");
		Enumeracao[3]  = new EnumeracaoDaGramatica(3,"V -> varinicio LV");
		Enumeracao[4]  = new EnumeracaoDaGramatica(4,"LV -> D LV");
		Enumeracao[5]  = new EnumeracaoDaGramatica(5,"LV -> varfim;");
		Enumeracao[6]  = new EnumeracaoDaGramatica(6,"D -> id TIPO;");
		Enumeracao[7]  = new EnumeracaoDaGramatica(7,"TIPO -> int");
		Enumeracao[8]  = new EnumeracaoDaGramatica(8,"TIPO -> real");
		Enumeracao[9]  = new EnumeracaoDaGramatica(9,"TIPO -> lit");
		Enumeracao[10] = new EnumeracaoDaGramatica(10,"A -> ES A");
		Enumeracao[11] = new EnumeracaoDaGramatica(11,"ES -> leia id;");
		Enumeracao[12] = new EnumeracaoDaGramatica(12,"ES -> escreva ARG;");
		Enumeracao[13] = new EnumeracaoDaGramatica(13,"ARG -> literal");
		Enumeracao[14] = new EnumeracaoDaGramatica(14,"ARG -> num");
		Enumeracao[15] = new EnumeracaoDaGramatica(15,"ARG -> id");
		Enumeracao[16] = new EnumeracaoDaGramatica(16,"A -> CMD A");
		Enumeracao[17] = new EnumeracaoDaGramatica(17,"CMD -> id rcb LD;");
		Enumeracao[18] = new EnumeracaoDaGramatica(18,"LD -> OPRD opm OPRD");
		Enumeracao[19] = new EnumeracaoDaGramatica(19,"LD -> OPRD");
		Enumeracao[20] = new EnumeracaoDaGramatica(20,"OPRD -> id");
		Enumeracao[21] = new EnumeracaoDaGramatica(21,"OPRD -> num");
		Enumeracao[22] = new EnumeracaoDaGramatica(22,"A -> COND A");
		Enumeracao[23] = new EnumeracaoDaGramatica(23,"COND -> CABE�ALHO CORPO");
		Enumeracao[24] = new EnumeracaoDaGramatica(24,"CABE�ALHO -> se (EXP_R) ent�o");
		Enumeracao[25] = new EnumeracaoDaGramatica(25,"EXP_R -> OPRD opr OPRD");
		Enumeracao[26] = new EnumeracaoDaGramatica(26,"CORPO -> ES CORPO");
		Enumeracao[27] = new EnumeracaoDaGramatica(27,"CORPO -> CMD CORPO");
		Enumeracao[28] = new EnumeracaoDaGramatica(28,"CORPO -> COND CORP");
		Enumeracao[29] = new EnumeracaoDaGramatica(29,"CORPO -> fimse");
		Enumeracao[30] = new EnumeracaoDaGramatica(29,"A -> fim");
	
		return Enumeracao[line].getSentenca();

	}
	//Tabela GOTO]
	
	public int getGOTO(int line, String nonTerminal) {
		//Onde Line � o estado atual e nonTerminal � o n�o-terminal
		int G[] = {
				128, 80, 86, 65,133, 68,134,135,136,137,138,139,140,141,142,143,
				0  ,  1,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				1  ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				2  ,132,  3,132,132,132,132,132,132,132,132,132,132,132,132,132,
				3  ,132,132,  5,132,132,132,  6,132,  7,132,132,  8,  9,132,132,
				4  ,132,132,132, 15, 16,132,132,132,132,132,132,132,132,132,132,
				5  ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				6  ,132,132, 19,132,132,132,  6,132,  7,132,132,  8,  9,132,132,
				7  ,132,132, 20,132,132,132,  6,132,  7,132,132,  8,  9,132,132,
				8  ,132,132, 21,132,132,132,  6,132,  7,132,132,  8,  9,132,132,
				9  ,132,132,132,132,132,132, 23,132, 24,132,132, 25,  9, 22,132,
				10 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				11 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				12 ,132,132,132,132,132,132,132, 28,132,132,132,132,132,132,132,
				13 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				14 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				15 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				16 ,132,132,132, 34, 16,132,132,132,132,132,132,132,132,132,132,
				17 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				18 ,132,132,132,132,132, 36,132,132,132,132,132,132,132,132,132,
				19 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				20 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				21 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				22 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				23 ,132,132,132,132,132,132, 23,132, 24,132,132, 25,  9, 40,132,
				24 ,132,132,132,132,132,132, 23,132, 24,132,132, 25,  9, 41,132,
				25 ,132,132,132,132,132,132, 23,132, 24,132,132, 25,  9, 50,132,
				26 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				27 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				28 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				29 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				30 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				31 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				32 ,132,132,132,132,132,132,132,132,132, 44, 45,132,132,132,132,
				33 ,132,132,132,132,132,132,132,132,132,132,132,132,132, 48, 49,
				34 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				35 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				36 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				37 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				38 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				39 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				40 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				41 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				42 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				43 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				44 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				45 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				46 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				47 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				48 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				49 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				50 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				51 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				52 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				53 ,132,132,132,132,132,132,132,132,132,132, 55,132,132,132,132,
				54 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				55 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				56 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
				57 ,132,132,132,132,132,132,132,132,132,132, 58,132,132,132,132,
				58 ,132,132,132,132,132,132,132,132,132,132,132,132,132,132,132,
		};
		
		/*
		 * Legenda da TABELA GOTO: Primeira linha utilizei a Tabela ASCII para fazer
		 * referencia aos caracteres(Os n�o-terminais formados por mais de uma letra
		 * tem digitos especiais). 
		 * Os 59 estados(0 a 58 s�o a primeira coluna da tabela)
		 * A Tabela ASCII vai ate 127, entao usei numeros
		 * acima desse valor como auxiliares. Numeros: 128 e o vertice da tabela, tinha
		 * que colocar um numero peguei o 128;
		 * Os erros s�o marcados por 132
		 * Os n�o terminais de s� uma letra, por exemplo 'A', s�o referenciados por seu
		 * pr�prio inteiro na tabela ASCII; no caso de A, 65.
		 * Legenda dos n�o-terminais de mais de uma letra:
		 * 		LV    = 133.	TIPO  = 134.	ES        = 135.
	 	 * 		ARG   = 136.   	CMD   = 137.	LD        = 138.
	 	 * 		OPRD  = 139.	COND  = 140.	CABECALHO = 141.
	 	 * 		CORPO = 142.	EXP_R = 143.	
	 	 */
	
	
		TabelaSintatica tabelaGOTO[][] = new TabelaSintatica[60][16];
	
		int t = 0;
	
		for (int i = 0; i < 60; i++) {
			for (int j = 0; j < 16; j++) {
				tabelaGOTO[i][j] = new TabelaSintatica(G[t]); // Ta com erro pq eu n coloquei valores para S
				t++;
			}
		}
		int column = 0;
		//Em caso de n�o-terminais de mais de uma letra, verifique a qual decimal ele est� relacionado.
		switch(nonTerminal) {
			case "LV":
				column = 133;
			case "TIPO":
				column = 134;
			case "ES":
				column = 135;
			case "ARG":
				column = 136;
			case "CMD":
				column = 137;
			case "LD":
				column = 138;
			case "OPRD":
				column = 139;
			case "COND":
				column = 140;
			case "CABECALHO":
				column = 141;
			case "CORPO":
				column = 142;
			case "EXP_R":
				column = 143;
		}
		
		return tabelaGOTO[line+1][column].getElemento();
		//Line +1: A primeira linha da tabela � apenas de terminais.
		//Dessa forma, o estado 0 est� na linha 1, o estado 1 na linha 2
		//e assim por diante.
	}
	
	public int getACTION(int line, String terminal) {
		
		int A[] = {
				128, 133, 134, 135,  59, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146,  40,  41, 147, 148, 149, 150,  36,
				  0,   2, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				  1, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 151,
				  2, 132,   4, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				  3, 132, 132, 132, 132, 132, 132, 132,  11,  13,  12, 132, 132, 132, 132,  14, 132, 132, 132, 132, 132,  10, 132,
				  4, 132, 132,  17, 132, 132, 132, 132, 132,  18, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				  5,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  -2,
				  6, 132, 132, 132, 132, 132, 132, 132,  11,  13,  12, 132, 132, 132, 132,  14, 132, 132, 132, 132, 132,  10, 132,
				  7, 132, 132, 132, 132, 132, 132, 132,  11,  13,  12, 132, 132, 132, 132,  14, 132, 132, 132, 132, 132,  10, 132,
				  8, 132, 132, 132, 132, 132, 132, 132,  11,  13,  12, 132, 132, 132, 132,  14, 132, 132, 132, 132, 132,  10, 132,
				  9, 132, 132, 132, 132, 132, 132, 132,  11,  13,  12, 132, 132, 132, 132,  14, 132, 132, 132, 132,  26, 132, 132,
				 10,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, -30,
				 11, 132, 132, 132, 132, 132, 132, 132, 132,  27, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 12, 132, 132, 132, 132, 132, 132, 132, 132,  31, 132,  29,  30, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 13, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,  32, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 14, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,  33, 132, 132, 132, 132, 132, 132,
				 15,   0,   0,   0,   0,   0,   0,   0,  -3,  -3,  -3,   0,   0,   0,   0,  -3,   0,   0,   0,   0,  -3,   0,   0,
				 16, 132, 132,  17, 132, 132, 132, 132, 132,  18, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 17, 132, 132, 132,  35, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 18, 132, 132, 132, 132,  37,  38, 132, 132, 132, 132,  39, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 19,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, -10,
				 20,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, -16,
				 21,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, -22,
				 22,   0,   0,   0,   0,   0,   0,   0, -23, -23, -23,   0,   0,   0,   0, -23,   0,   0,   0,   0, -23, -23,   0,
				 23, 132, 132, 132, 132, 132, 132, 132,  11,  13,  12, 132, 132, 132, 132,  14, 132, 132, 132, 132,  26, 132, 132,
				 24, 132, 132, 132, 132, 132, 132, 132,  11,  13,  12, 132, 132, 132, 132,  14, 132, 132, 132, 132,  26, 132, 132,
				 25, 132, 132, 132, 132, 132, 132, 132,  11,  13,  12, 132, 132, 132, 132,  14, 132, 132, 132, 132,  26, 132, 132,
				 26,   0,   0,   0,   0,   0,   0,   0, -29, -29, -29,   0,   0,   0,   0, -29,   0,   0,   0,   0, -29, -29,   0,
				 27, 132, 132, 132,  42, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 28, 132, 132, 132,  42, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 29,   0,   0,   0, -13,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
				 30,   0,   0,   0, -14,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
				 31,   0,   0,   0, -15,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
				 32, 132, 132, 132, 132, 132, 132, 132, 132,  46, 132, 132,  47, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 33, 132, 132, 132, 132, 132, 132, 132, 132,  46, 132, 132,  47, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 34,   0,   0,   0,   0,   0,   0,   0,  -4,  -4,  -4,   0,   0,   0,   0,  -4,   0,   0,   0,   0,   0,  -4,   0,
				 35,   0,   0,   0,   0,   0,   0,   0,  -5,  -5,  -5,   0,   0,   0,   0,  -5,   0,   0,   0,   0,   0,  -5,   0,
				 36, 132, 132, 132,  51, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 37,   0,   0,   0,  -7,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
				 38,   0,   0,   0,  -8,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
				 39,   0,   0,   0,  -9,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
				 40,   0,   0,   0,   0,   0,   0,   0, -26, -26, -26,   0,   0,   0,   0, -26,   0,   0,   0,   0, -26, -26,   0,
				 41,   0,   0,   0,   0,   0,   0,   0, -27, -27, -27,   0,   0,   0,   0, -27,   0,   0,   0,   0, -27, -27,   0,
				 42,   0,   0,   0,   0,   0,   0,   0, -11, -11, -11,   0,   0,   0,   0, -11,   0,   0,   0,   0, -11, -11,   0,
				 43,   0,   0,   0,   0,   0,   0,   0, -12, -12, -12,   0,   0,   0,   0, -12,   0,   0,   0,   0, -12, -12,   0,
				 44, 132, 132, 132,  52, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 45, 132, 132, 132, -19, 132, 132, 132, 132, 132, 132, 132, 132, 132,  53, 132, 132, 132, 132, 132, 132, 132, 132,	
				 46,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, -20,   0, -20,   0,   0,   0,
				 47,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, -21,   0, -21,   0,   0,   0,
				 48, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,  54, 132, 132, 132, 132, 132,
				 49, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,  57, 132, 132, 132,
				 50,   0,   0,   0,   0,   0,   0,   0, -28, -28, -28,   0,   0,   0,   0, -28,   0,   0,   0,   0, -28, -28,   0,
				 51,   0,   0,  -6,   0,   0,   0,   0,   0,  -6,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
				 52,   0,   0,   0,   0,   0,   0,   0, -17, -17, -17,   0,   0,   0,   0, -17,   0,   0,   0,   0, -17, -17,   0,
				 53, 132, 132, 132, 132, 132, 132, 132, 132,  46, 132, 132,  47, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 54, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,  56, 132, 132, 132, 132,
				 55,   0,   0,   0, -18,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
				 56,   0,   0,   0,   0,   0,   0,   0, -24, -24, -24,   0,   0,   0,   0, -24,   0,   0,   0,   0, -24,   0,   0,
				 57, 132, 132, 132, 132, 132, 132, 132, 132,  46, 132, 132,  47, 132, 132, 132, 132, 132, 132, 132, 132, 132, 132,
				 58,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, -25,   0,   0,   0,   0,   0,
			};
		/*
		 * Legenda da TABELA ACTION: Primeira linha utilizei a Tabela ASCII para fazer
		 * referencia aos caracteres(Os terminais formados por mais de uma letra
		 * tem digitos especiais).
		 * Os Estados que s�o empilhados s�o positivos.
		 * Os Estados que ser�o reduzidos s�o negativos.  
		 * Os 59 estados(0 a 58 s�o a primeira coluna da tabela) s�o inseridos como 
		 * objetos Acao com string nula.
		 * A Tabela ASCII vai ate 127, entao usei numeros
		 * acima desse valor como auxiliares. Numeros: 128 e o vertice da tabela, tinha
		 * que colocar um numero peguei o 128;
		 * Os erros s�o marcados por 132
		 * A aceita��o � marcada por 151 
		 * Os terminais de s� uma letra (incluindo $), por exemplo '*', s�o referenciados 
		 * por seu pr�prio inteiro na tabela ASCII; no caso de *, 34.
		 * Legenda dos terminais de mais de uma letra:
		 * 		inicio   = 133.		varinicio  = 134.	varfim  = 135.
	 	 * 		int      = 136.   	real       = 137.	lit     = 138.
	 	 * 		leia     = 139.	    id         = 140.	escreva = 141.
	 	 * 		literal  = 142.	    num        = 143.	rcb     = 144.
	 	 * 		opm		 = 145.	    se		   = 146.   entao   = 147.
	 	 * 		opr		 = 148.	    fimse	   = 149.   fim     = 150.
		 *	
		 *	ERROS:
		 *
		 *		Os erros nas linhas que s� possuem empilhamento s�o sinalizados por 132 e cada
		 *	uma dessas linhas tem seu retorno de erro (Frase que indica o que houve).
		 *		Os erros nas linhas em que h� uma redu��o s�o marcados como 0. Para identificar
		 *	qual foi a redu��o propagada, � feita uma busca na linha at� encontrar a redu��o. 
	 	 */
		
		TabelaSintatica tabelaACTION[][] = new TabelaSintatica[60][23];
		
		int t = 0;
	
		for (int i = 0; i < 60; i++) {
			for (int j = 0; j < 23; j++) {
				tabelaACTION[i][j] = new TabelaSintatica(A[t]); // Ta com erro pq eu n coloquei valores para S
				t++;
			}
		}
		int column = 0;
		
		//Em caso de terminais de mais de uma letra, verifique a qual decimal ele est� relacionado.
		switch(terminal) {
		case "inicio":
			column = 133;
		case "varinicio":
			column = 134;
		case "varfim":
			column = 135;
		case "int":
			column = 136;
		case "real":
			column = 137;
		case "lit":
			column = 138;
		case "leia":
			column = 139;
		case "id":
			column = 140;
		case "escreva":
			column = 141;
		case "literal":
			column = 142;
		case "num":
			column = 143;
		case "rcb":
			column = 144;
		case "opm":
			column = 145;
		case "se":
			column = 146;
		case "entao":
			column = 147;
		case "opr":
			column = 148;
		case "fimse":
			column = 149;
		case "fim":
			column = 150;
		}
		
		return tabelaACTION[line+1][column].getElemento();
			//Line +1: A primeira linha da tabela � apenas de terminais.
			//Dessa forma, o estado 0 est� na linha 1, o estado 1 na linha 2
			//e assim por diante.
	}
	
	
	
	
	
	
}
