import java.util.Random;
import java.util.Scanner;

class CampoMinado {

    /**
     * Variaveis globais
     * Podem ser acessadas de qualquer parte da classe
     */
    static int[][] game; //matriz de inteiros referente ao jogo em si
    static char[][] gameInterative; //Matriz de caracteres referente ao que o usuario vai ver e interagir com o jogo
    static int linhas; //numero de linhas que o campo minado possuira
    static int colunas; //numero de colunas que o campo minado possuira

    public static void main(String args[]) {
        /**
         * Atribuindo valores as variaveis
         */
        Scanner input = new Scanner(System.in);
        System.out.print("Digite o numero de linhas: ");
        linhas = input.nextInt();
        System.out.print("Digite o numero de colunas: ");
        colunas = input.nextInt();
        System.out.print("Digite o numero de bombas: ");
        int bombas = input.nextInt(); //numero de bombas que tera o jogo

        criarJogo(linhas, colunas, bombas); // Metodo para criar o jogo recebe como paremetro o nunmero de linhas, numero de colunas e o numero de bombas
        imprimeMatrizMask(); //Metodo que printa o estado atual do jogo (Demonstra apenas a situação atual do jogo ou seja usa apenas o gameInterative)
        while(!ganhou()){ //Valida se ainda existem interações a serem feitas no campo minado, determina se o jogo acabou em vitoria
            System.out.println("Selecione a posição: \n");
            System.out.print("linha: ");
            int linhaEscolhida = input.nextInt(); //define o campo linha da posição que o usuario deseja jogar
            System.out.print("Coluna: ");
            int colunaEscolhida = input.nextInt(); //define o campo coluna da posição que o usuario deseja jogar
            if(interagir(linhaEscolhida, colunaEscolhida)){ //valida se a posição selecionada utilizando os parametros eh um campo livre, um campo não jogado ou uma bomba
                System.out.println("tu perdeu");
                imprimeMatrizMask(); //Metodo que printa o estado atual do jogo (Demonstra apenas a situação atual do jogo ou seja usa apenas o gameInterative)
                break;
            }
            imprimeMatrizMask(); //Metodo que printa o estado atual do jogo (Demonstra apenas a situação atual do jogo ou seja usa apenas o gameInterative)
        }

    }

    private static void criarJogo(int nLinhas, int nColunas, int bombas) {
        if (nLinhas >= 2 && nColunas >= 2) {
            game = new int[nLinhas][nColunas]; //inicializa a matriz game com os tamanhos que possuira
            gameInterative = new char[nLinhas][nColunas]; //inicia a matriz interativa com o mesmo tamanho da game
            popularMatriz(game, nLinhas, nColunas); //adiciona o valor 0 a todas as linhas da matriz game (Modelo inicial de jogo)
            popularMatrizInterativa(nLinhas, nColunas); // adiciona o valor '_' a todas as linhas da matriz interativa (modelo inicial de jogo)
            if (bombas > 0 && bombas < game.length) { // Valida o numero de bombas (deve ser um numero maior que 0 e menor que a matriz)
                inserirBombas(game, nLinhas, nColunas, bombas); // Insere bombas em espaços aleatorio da matriz (valor de bomba = -1)
            } else {
                System.out.println("numero de bombas invalido");
            }
        } else {
            System.out.print("Tamanho da matriz invalido");
        }
    }

    /**
     * Valor 0 é o valor default de um campo, significa que o espaço não posssui nenhuma bomba no momento
     * Este metodo insere o valor default para todos os espaços.
     */
    private static void popularMatriz(int[][] matriz, int nLinhas, int nColunas) {
        for (int i = 0; i < nLinhas; i++) { //Interage com as linhas
            for (int j = 0; j < nColunas; j++) { // Interage com as colunas
                matriz[i][j] = 0; // Atribui o valor default para a posicao
            }
        }
    }

    /**
     * Adiciona em espaços aleatorios o valor -1 que pertence a bomba
     */
    private static void inserirBombas(int[][] matriz, int nLinhas, int nColunas, int bombas) {
        for (int i = 0; i < bombas; i++) {
            Random generator = new Random();
            int linha = generator.nextInt(nLinhas);
            int coluna = generator.nextInt(nColunas);
            if (matriz[linha][coluna] == -1) {
                i--;
            } else {
                matriz[linha][coluna] = -1;
            }
        }
    }

    /**
     * Adiciona o valor 'desconhecido' (_) a todas as posicoes da matriz interativa
     */
    private static void popularMatrizInterativa(int nLinhas, int nColunas) {
        for (int i = 0; i < nLinhas; i++) {
            for (int j = 0; j < nColunas; j++) {
                gameInterative[i][j] = '_';
            }
        }
    }

    /**
     * metodo que imprime a matriz interativa para o usuario
     */
    private static void imprimeMatrizMask() {
        for (char[] vetor : gameInterative) { // Este for faz a interação de todas as linhas
            for (char atual : vetor) { // Este for faz a interação dos espaços de cada linha
                System.out.print(atual + " "); // printa do espaço atual
            }
            System.out.println(); //pula linha
        }
        System.out.println(); // pula uma linha no final das interações para ficar bonitinho
    }

    /**
     * realiza a interação com a posição definida pelo usuario caso seja o default (0) alterará para 1 (ja interagido)
     * Caso seja bomba retornará que estourou
     */
    private static boolean interagir(int i, int j) {
        boolean estourou = false; //Variavel que dira se o espaço interagido é uma bomba ou não
        if (i < linhas && j < colunas) { // valida se a posição pedida é realmente uma posição valida
            switch (game[i][j]) { //swtich do valor que esta nessa posição
                case 0: // caso o valor seja 0
                    game[i][j] = 1; // altera o valor para 1
                    gameInterative[i][j] = 'x'; // altera o valor de _ (desconhecido) para para x (ja interagido sem bomba)
                    if (procuraBomba(i, j)){ // metodo que valida se existe uma bomba por perto (chato pa krlh)
                        System.out.println("Tem bomba perto!");
                    }
                    break;
                case -1: // caso o valor seja -1
                    gameInterative[i][j] = 'b'; // altera o valor de _ (desconhecido) para b (bomba)
                    estourou = true; //altera a variavel que diz que voce perdeu para true
                    break;
            }
            return estourou; //retorna se voce acertou uma bomba ou não
        }
        System.out.println("posicao invalida");
        return false;
    }

    /**
     * Metodo que valida se ainda é possivel jogar ou se todos os espaços ja foram descobertos e voce ganhou
     * caso ainda exista algum campo que esteja com valor 0 o jogo deve continuar
     */
    private static boolean ganhou(){
        boolean ganhou = true; // variavel que valida se o jogo ja foi concluido sem que voce tenha acertado a bomba
        for (int[] vetor : game) { // for que interage com as linhas
            for (int atual : vetor) { //for que interage com os vetores de cada linha
                if (atual == 0){ //valida se o campo esta não interagido
                    ganhou = false; // altera o valor de ganhou para falso e sai do for
                    break;
                }
            }
            if (!ganhou){ // valida se o campo foi alterado para false e caso sim sai do for
                break;
            }
        }
        if (ganhou){ // valida se todos os campos foram interagidos (estao diferentes de 0)
            System.out.println("PARABENS!!! VOCE GANHOU"); //printa que o jogo acabou
        }
        return ganhou;
    }

    /**
     * testa se as posições em volta da posição escolhida tem a bomba
     * dentro das posições delimitadas pela matriz
     */
    private static boolean procuraBomba(int i, int j) {
        if (i == 0) {
            if (j == 0) {
                if (    game[i + 1][j] == -1 ||
                        game[i][j + 1] == -1 ||
                        game[i + 1][j + 1] == -1) {
                    return true;
                }
            } else if (j == colunas - 1) {
                if (    game[i + 1][j] == -1 ||
                        game[i][j - 1] == -1 ||
                        game[i + 1][j - 1] == -1) {
                    return true;
                }
            } else {
                if (    game[i + 1][j] == -1 ||
                        game[i][j + 1] == -1 ||
                        game[i][j - 1] == -1 ||
                        game[i + 1][j + 1] == -1 ||
                        game[i + 1][j - 1] == -1) {
                    return true;
                }
            }
        } else if (i == linhas - 1) {
            if (j == 0) {
                if (    game[i - 1][j] == -1 ||
                        game[i][j + 1] == -1 ||
                        game[i - 1][j + 1] == -1) {
                    return true;
                }
            } else if (j == colunas - 1) {
                if (    game[i - 1][j] == -1 ||
                        game[i][j - 1] == -1 ||
                        game[i - 1][j - 1] == -1) {
                    return true;
                }
            } else {
                if (    game[i - 1][j] == -1 ||
                        game[i][j + 1] == -1 ||
                        game[i][j - 1] == -1 ||
                        game[i - 1][j + 1] == -1 ||
                        game[i - 1][j - 1] == -1) {
                    return true;
                }
            }
        } else {
            if (j == 0) {
                if (    game[i - 1][j] == -1 ||
                        game[i][j + 1] == -1 ||
                        game[i - 1][j + 1] == -1 ||
                        game[i + 1][j] == -1 ||
                        game[i + 1][j + 1] == -1) {
                    return true;
                }
            } else if (j == colunas - 1) {
                if (    game[i - 1][j] == -1 ||
                        game[i][j - 1] == -1 ||
                        game[i - 1][j - 1] == -1 ||
                        game[i + 1][j] == -1 ||
                        game[i + 1][j - 1] == -1) {
                    return true;
                }
            } else {
                if (    game[i - 1][j] == -1 ||
                        game[i][j + 1] == -1 ||
                        game[i][j - 1] == -1 ||
                        game[i - 1][j + 1] == -1 ||
                        game[i - 1][j - 1] == -1 ||
                        game[i + 1][j] == -1 ||
                        game[i + 1][j - 1] == -1 ||
                        game[i + 1][j + 1] == -1) {
                    return true;
                }
            }
        }
        return false;
    }


}