package com.p3.jogodamemoria;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jogo {

    //TODO: METODO DE SALVAR O JOGO NO ARQUIVO E LER O ARQUIVO PRA SETTAR O JOGO SALVO NA VIEW

    private ArrayList<Jogador> jogadores;
    private Carta [][] tabuleiro = new Carta[4][4];

    public void inicializarTabuleiro() {
        String[] imagens = {"kuriboh", "bustnatrix", "sparkman", "aviario", "bubbleman", "neos", "blue-eyes", "mago-negro"};
        Random randomGenerator = new Random();
        int count = 0;
        while (!isTabuleiroCheio()){
            if (count > 7){
                count = 0;
            }
            String imagemSelecionada = imagens[count];
            int linhaAleatoria1 = randomGenerator.nextInt(4);
            int colunaAleatoria1 = randomGenerator.nextInt(4);
            while (tabuleiro[linhaAleatoria1][colunaAleatoria1] != null){
                linhaAleatoria1 = randomGenerator.nextInt(4);
                colunaAleatoria1 = randomGenerator.nextInt(4);
            }
            int linhaAleatoria2 = randomGenerator.nextInt(4);
            int colunaAleatoria2 = randomGenerator.nextInt(4);
            while ((linhaAleatoria1 == linhaAleatoria2 && colunaAleatoria1 == colunaAleatoria2) || tabuleiro[linhaAleatoria2][colunaAleatoria2] != null){
                linhaAleatoria2 = randomGenerator.nextInt(4);
                colunaAleatoria2 = randomGenerator.nextInt(4);
            }
            tabuleiro[linhaAleatoria1][colunaAleatoria1] = new Carta(imagemSelecionada, linhaAleatoria1, colunaAleatoria1);
            tabuleiro[linhaAleatoria2][colunaAleatoria2] = new Carta(imagemSelecionada, linhaAleatoria2, colunaAleatoria2);
            count++;
        }
    }

    public Boolean isTabuleiroCheio(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tabuleiro[i][j] == null){
                    return false;
                }
            }
        }
        return true;
    }

    public int jogada(Jogador jogador, Carta carta1, Carta carta2){
        if(carta1.getValor().equals(carta2.getValor())){
            if (carta1.getFoiCombinada() == false && carta2.getFoiCombinada() == false){
                //TODO: Metodo/funcionalidade de salvar jogada
                //TODO: Metodo/funcionalidade de acrescentar ponto a ao jogador
                return 1;
            } else {
                return -1;
            }
        }
        //TODO:Metodo/Funcionalidade de trocar o jogador para o próximo (lembrando de mudar a view dos pontos de acordo com o jogador)
        return 0;
    }

    public Carta[][] getTabuleiro() {
        return tabuleiro;
    }
}
