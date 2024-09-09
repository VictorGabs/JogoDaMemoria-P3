package com.p3.jogodamemoria;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JogoController {

    @FXML
    public GridPane jogoGrid;

    @FXML
    private Label labelJogador;

    @FXML
    private Label labelPontos;

    Jogo jogo = new Jogo();

    Carta tabuleiro[][] = jogo.getTabuleiro();
    Carta primeiraCarta = null;
    Carta segundaCarta = null;

    int indexCarta1 = 0;
    int indexCarta2 = 0;

    Jogador jogador1 = new Jogador("GABRIEL");
    Jogador jogador2 = new Jogador("ADVERSARIO");
    Jogador jogadorAtual = jogador1;

    public void atualizarViewJogador() {
        labelJogador.setText(jogadorAtual.getNome());
        labelPontos.setText(String.valueOf(jogadorAtual.getPontos()));
    }

    List<String> historicoJogadas = new ArrayList<>();

    @FXML
    public void initialize() {
        jogo.inicializarTabuleiro();
        for (int i = 0; i < jogoGrid.getChildren().size(); i++) {
            ImageView imageView = (ImageView) jogoGrid.getChildren().get(i);
            imageView.setUserData(i);
        }
        atualizarViewJogador();
    }

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

    public void checandoJogada(int linhaSelecionada, int colunaSelecionada, int index) {
        if (primeiraCarta == null) {
            primeiraCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
            indexCarta1 = index;
        } else if (segundaCarta == null) {
            segundaCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
            indexCarta2 = index;
        } else {
            if (jogo.jogada(null, primeiraCarta, segundaCarta) == 1) {
                tabuleiro[primeiraCarta.getLinha()][primeiraCarta.getColuna()].setFoiCombinada(true);
                tabuleiro[segundaCarta.getLinha()][segundaCarta.getColuna()].setFoiCombinada(true);

                jogadorAtual.setPontos(1);  // Adiciona 1 ponto ao jogador atual

                String jogada = "Par revelado nas posições: (" + primeiraCarta.getLinha() + ", " + primeiraCarta.getColuna() + ") e ("
                        + segundaCarta.getLinha() + ", " + segundaCarta.getColuna() + ")";
                historicoJogadas.add(jogada);
                salvarJogadaEmArquivo(jogada);

                atualizarViewJogador();

                primeiraCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
                indexCarta1 = index;
                segundaCarta = null;
            } else if (jogo.jogada(null, primeiraCarta, segundaCarta) == 0) {
                ImageView imageCarta1 = (ImageView) jogoGrid.getChildren().get(indexCarta1);
                ImageView imageCarta2 = (ImageView) jogoGrid.getChildren().get(indexCarta2);
                String caminho = imageCarta1.getImage().getUrl();
                int fimUrl = caminho.lastIndexOf("/");
                String path = caminho.substring(0, fimUrl + 1) + "BackgroundCarta.png";
                imageCarta1.setImage(new Image(path));
                imageCarta2.setImage(new Image(path));

                String jogada = "Par incorreto nas posições: (" + primeiraCarta.getLinha() + ", " + primeiraCarta.getColuna() + ") e ("
                        + segundaCarta.getLinha() + ", " + segundaCarta.getColuna() + ")";
                historicoJogadas.add(jogada);
                salvarJogadaEmArquivo(jogada);

                alternarJogador();

                primeiraCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
                indexCarta1 = index;
                segundaCarta = null;
            } else {
                alternarJogador();

                primeiraCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
                indexCarta1 = index;
                segundaCarta = null;
            }
        }
    }

    // TODO - Melhorar esse metodo para renderizar a tela das jogadas e alterar na view
//    public void exibirHistoricoJogadas() {
//        for (String jogada : historicoJogadas) {
//            System.out.println(jogada);
//        }
//    }

    public void alternarJogador() {
        if (jogadorAtual == jogador1) {
            jogadorAtual = jogador2;
        } else {
            jogadorAtual = jogador1;
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
