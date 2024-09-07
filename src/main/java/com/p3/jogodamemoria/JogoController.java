package com.p3.jogodamemoria;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;

public class JogoController {
    @FXML
    public GridPane jogoGrid;

    Jogo jogo = new Jogo();

    Carta tabuleiro [][]  = jogo.getTabuleiro();

    Carta primeiraCarta = null;
    Carta segundaCarta = null;

    int indexCarta1 = 0;
    int indexCarta2 = 0;

    @FXML
    public void initialize() {
        jogo.inicializarTabuleiro();
        for (int i = 0; i < jogoGrid.getChildren().size(); i++) {
            ImageView imageView = (ImageView) jogoGrid.getChildren().get(i);
            imageView.setUserData(i);
        }
    }
    public void cartaListener(MouseEvent click){
        ImageView imageView = (ImageView) click.getSource();
        int linhaSelecionada = Integer.parseInt(imageView.getId().split("x")[0]);
        int colunaSelecionada = Integer.parseInt(imageView.getId().split("x")[1]);
        String imagem = tabuleiro[linhaSelecionada][colunaSelecionada].valor;
        String caminho = imageView.getImage().getUrl();
        int fimUrl = caminho.lastIndexOf("/");
        String path = caminho.substring(0, fimUrl + 1);
        imageView.setImage(new Image(path + imagem+".jpg"));
        if(!jogoAcabou()){
            checandoJogada(linhaSelecionada,colunaSelecionada, Integer.parseInt(imageView.getUserData().toString()));
        }
    }

    public void checandoJogada(int linhaSelecionada, int colunaSelecionada, int index) {
        if(primeiraCarta==null){
            primeiraCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
            indexCarta1 = index;
        }else if(segundaCarta==null){
            segundaCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
            indexCarta2 = index;
        }else{
            if (jogo.jogada(null, primeiraCarta, segundaCarta) == 1) {
                tabuleiro[primeiraCarta.getLinha()][primeiraCarta.getColuna()].setFoiCombinada(true);
                tabuleiro[segundaCarta.getLinha()][segundaCarta.getColuna()].setFoiCombinada(true);
                primeiraCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
                indexCarta1 = index;
                segundaCarta = null;
            }else if (jogo.jogada(null, primeiraCarta, segundaCarta) == 0) {
                ImageView imageCarta1 = (ImageView) jogoGrid.getChildren().get(indexCarta1);
                ImageView imageCarta2 = (ImageView) jogoGrid.getChildren().get(indexCarta2);
                String caminho = imageCarta1.getImage().getUrl();
                int fimUrl = caminho.lastIndexOf("/");
                String path = caminho.substring(0, fimUrl + 1)+"BackgroundCarta.png";
                imageCarta1.setImage(new Image(path));
                imageCarta2.setImage(new Image(path));
                primeiraCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
                indexCarta1 = index;
                segundaCarta = null;
            }else{
                primeiraCarta = tabuleiro[linhaSelecionada][colunaSelecionada];
                indexCarta1 = index;
                segundaCarta = null;
            }
        }
    }

    public boolean jogoAcabou(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(!tabuleiro[i][j].getFoiCombinada()) {
                    return false;
                }
            }
        }
        return true;
    }
}