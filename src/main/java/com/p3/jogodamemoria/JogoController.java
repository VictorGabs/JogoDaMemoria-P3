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

public class JogoController {
    @FXML
    public GridPane jogoGrid;

    Jogo jogo = new Jogo();
    @FXML
    protected void onHelloButtonClick() {
    }

    @FXML
    public void initialize() {

        try {
           jogo.inicializarTabuleiro();
            for (int linha = 0; linha < 4; linha++) {
                for (int coluna = 0; coluna < 4; coluna++) {
                    FileInputStream caminhoBackgroundCarta = new FileInputStream("C:\\Users\\victo\\IdeaProjects\\JogoDaMemoria\\src\\main\\resources\\assets\\imgs\\BackgroundCarta.png");
                    Image backgroundCarta = new Image(caminhoBackgroundCarta);
                    ImageView imageView = new ImageView(backgroundCarta);
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(150);
                    imageView.setUserData(linha+","+coluna);
                    imageView.setOnMouseClicked(click -> {cartaListener(click);});
                    jogoGrid.add(imageView, linha, coluna);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void cartaListener(MouseEvent click){
        Node source = (Node) click.getSource();
        int linhaSelecionada = Integer.parseInt(source.getUserData().toString().split(",")[0]);
        int colunaSelecionada = Integer.parseInt(source.getUserData().toString().split(",")[1]);
        String imagem = jogo.tabuleiro[linhaSelecionada][colunaSelecionada].valor;
        try {
            FileInputStream arquivoImagem = new FileInputStream("C:\\Users\\victo\\IdeaProjects\\JogoDaMemoria\\src\\main\\resources\\assets\\imgs\\"+imagem+".jpg");
            Image imagemSelecionada = new Image(arquivoImagem);
            ((ImageView)source).setImage(imagemSelecionada);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}