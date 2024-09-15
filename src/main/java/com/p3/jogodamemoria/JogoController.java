package com.p3.jogodamemoria;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JogoController {

    @FXML
    private TextField playerNameField;

    @FXML
    private ListView<String> playerList;

    @FXML
    private Label labelJogador;

    @FXML
    private Label labelPontos;

    @FXML
    private GridPane jogoGrid;  // Referência ao GridPane no jogoDaMemoriaView.fxml
    Jogo jogo = new Jogo();

    private List<Jogador> jogadores = new ArrayList<>();
    private Jogador jogadorAtual;
    private Carta[][] tabuleiro;
    private Carta primeiraCarta = null;
    private Carta segundaCarta = null;

    int indexCarta1 = 0;
    int indexCarta2 = 0;

    List<String> historicoJogadas = new ArrayList<>();

    @FXML
    public void initialize() {
//        // Inicializa com um jogador padrão
//        jogadorAtual = new Jogador("GABRIEL");
//        jogadores.add(jogadorAtual);
        atualizarViewJogador();
    }

    @FXML
    public void addPlayer() {
        String playerName = playerNameField.getText().trim();
        if (!playerName.isEmpty()) {
            Jogador novoJogador = new Jogador(playerName);
            jogadores.add(novoJogador);
            playerList.getItems().add(playerName);
            playerNameField.clear();
        }
    }

    public void atualizarViewJogador() {
        if (jogadorAtual != null) {
            labelJogador.setText(jogadorAtual.getNome());
            labelPontos.setText(String.valueOf(jogadorAtual.getPontos()));
        }
    }

    @FXML
    public void iniciarJogo() {
        if (!jogadores.isEmpty()) {
            jogadorAtual = jogadores.get(0);  // Inicia com o primeiro jogador
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("jogoDaMemoriaView.fxml"));
                Parent gameBoard = fxmlLoader.load();

                JogoController gameController = fxmlLoader.getController();
                gameController.inicializarJogo(jogadores);  // Passa os jogadores para o controlador da tela do jogo

                Stage stage = (Stage) playerList.getScene().getWindow();

                Scene scene = new Scene(gameBoard);
                stage.setScene(scene);
                stage.show();
                atualizarViewJogador();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nenhum jogador foi adicionado.");
        }
    }

    public void inicializarJogo(List<Jogador> jogadoresIniciais) {
        this.jogadores = jogadoresIniciais;
        this.jogadorAtual = jogadores.get(0);  // Inicia com o primeiro jogador

        Jogo jogo = new Jogo();
        tabuleiro = jogo.getTabuleiro();
        jogo.inicializarTabuleiro();

        for (int i = 0; i < jogoGrid.getChildren().size(); i++) {
            ImageView imageView = (ImageView) jogoGrid.getChildren().get(i);
            imageView.setUserData(i);  // Salva o índice do ImageView
        }
        atualizarViewJogador();
    }

    @FXML
    public void cartaListener(MouseEvent click) {
        ImageView imageView = (ImageView) click.getSource();
        int linhaSelecionada = Integer.parseInt(imageView.getId().split("x")[0]);
        int colunaSelecionada = Integer.parseInt(imageView.getId().split("x")[1]);
        String imagem = tabuleiro[linhaSelecionada][colunaSelecionada].valor;
        String caminho = imageView.getImage().getUrl();
        int fimUrl = caminho.lastIndexOf("/");
        String path = caminho.substring(0, fimUrl + 1);
        imageView.setImage(new Image(path + imagem + ".jpg"));

        if (!jogoAcabou()) {
            checandoJogada(linhaSelecionada, colunaSelecionada, Integer.parseInt(imageView.getUserData().toString()));
        }
    }

     private boolean cartasEmVerificacao = false;  // Controla se as cartas estão sendo verificadas

    public void checandoJogada(int linhaSelecionada, int colunaSelecionada, int index) {
        if (cartasEmVerificacao) {
            System.out.println("As cartas estão sendo verificadas. Aguarde.");
            return;
        }
        System.out.println("checandoJogada chamada - Linha: " + linhaSelecionada + ", Coluna: " + colunaSelecionada + ", Index: " + index);

        if (primeiraCarta == null) {
            primeiraCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
            indexCarta1 = index;
            System.out.println("Primeira carta selecionada: Linha: " + linhaSelecionada + ", Coluna: " + colunaSelecionada + ", Valor: " + primeiraCarta.getValor());
        } else if (segundaCarta == null) {
            if (index == indexCarta1) {
                System.out.println("Tentativa de selecionar a mesma carta. Ação ignorada.");
                return;  // Impede o clique na mesma carta
            }

            segundaCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
            indexCarta2 = index;
            System.out.println("Segunda carta selecionada: Linha: " + linhaSelecionada + ", Coluna: " + colunaSelecionada + ", Valor: " + segundaCarta.getValor());

            cartasEmVerificacao = true;

            if (jogo.jogada(null, primeiraCarta, segundaCarta) == 1) {
                System.out.println("Par revelado!");

                tabuleiro[primeiraCarta.getLinha()][primeiraCarta.getColuna()].setFoiCombinada(true);
                tabuleiro[segundaCarta.getLinha()][segundaCarta.getColuna()].setFoiCombinada(true);

                jogadorAtual.setPontos(jogadorAtual.getPontos() + 1);  // Adiciona 1 ponto ao jogador atual
                System.out.println("Pontos do jogador atual: " + jogadorAtual.getPontos());

                String jogada = "Par revelado nas posições: (" + primeiraCarta.getLinha() + ", " + primeiraCarta.getColuna() + ") e (" +
                        segundaCarta.getLinha() + ", " + segundaCarta.getColuna() + ")";
                historicoJogadas.add(jogada);
                salvarJogadaEmArquivo(jogada);
                System.out.println("Jogada salva: " + jogada);

                atualizarViewJogador();

                primeiraCarta = null;
                segundaCarta = null;

                cartasEmVerificacao = false;

            } else {
                System.out.println("Par incorreto - Resetando cartas");

                ImageView imageCarta1 = (ImageView) jogoGrid.getChildren().get(indexCarta1);
                ImageView imageCarta2 = (ImageView) jogoGrid.getChildren().get(indexCarta2);
                String caminho = imageCarta1.getImage().getUrl();
                int fimUrl = caminho.lastIndexOf("/");
                String path = caminho.substring(0, fimUrl + 1) + "BackgroundCarta.png";

                PauseTransition pause = new PauseTransition(Duration.seconds(1));  // Adiciona um pequeno atraso para ver as cartas
                pause.setOnFinished(e -> {
                    imageCarta1.setImage(new Image(path));
                    imageCarta2.setImage(new Image(path));
                    System.out.println("Cartas resetadas: " + imageCarta1.getId() + ", " + imageCarta2.getId());

                    alternarJogador();
                    System.out.println("Jogador alternado para: " + jogadorAtual.getNome());

                    primeiraCarta = null;
                    segundaCarta = null;

                    cartasEmVerificacao = false;
                });
                pause.play();
            }
        }
    }

    private void resetarCarta(ImageView carta, String path) {
        carta.setImage(new Image(path));
    }




    public void alternarJogador() {
        if (jogadores.size() > 1) {
            int indexAtual = jogadores.indexOf(jogadorAtual);  // Pega o índice do jogador atual
            int proximoIndex = (indexAtual + 1) % jogadores.size();  // Calcula o próximo índice de forma cíclica
            jogadorAtual = jogadores.get(proximoIndex);  // Alterna para o próximo jogador
        } else {
            System.out.println("Apenas um jogador. Não é possível alternar.");
        }
        atualizarViewJogador();
    }

    public void salvarJogadaEmArquivo(String jogada) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("historico_jogadas.txt", true))) {
            writer.write(jogada);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean jogoAcabou() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!tabuleiro[i][j].getFoiCombinada()) {
                    return false;
                }
            }
        }
        return true;
    }
}
