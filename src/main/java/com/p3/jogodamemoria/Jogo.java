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
    private ArrayList<Jogador> jogadores;
    public Carta [][] tabuleiro = new Carta[4][4];

    public void inicializarTabuleiro() {
        String[] imagens = {"kuriboh", "bustnatrix", "sparkman", "aviario"};
        Random randomGenerator = new Random();
        int count = 0;
        while (!isTabuleiroCheio()){
            if (count > 3){
                count = 0;
            }
            System.out.println(count);
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

    public void jogada(Jogador jogador, Carta carta){

    }
}
