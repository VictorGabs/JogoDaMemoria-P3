package com.p3.jogodamemoria;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EstadoJogo {

    private String caminhoArquivo;

    public EstadoJogo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    // Método para salvar o estado do jogo em um arquivo
    public void salvarEstado(Carta[][] tabuleiro) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (int i = 0; i < tabuleiro.length; i++) {
                for (int j = 0; j < tabuleiro[i].length; j++) {
                    Carta carta = tabuleiro[i][j];
                    if (carta.getFoiCombinada()) {
                        // Salva o valor da carta, linha e coluna
                        writer.write(carta.getValor() + "," + carta.getLinha() + "," + carta.getColuna());
                        writer.newLine();
                    }
                }
            }
        }
    }

    // Método para carregar o estado do jogo de um arquivo
    public List<Carta> carregarEstado() throws IOException {
        List<Carta> cartasCombinadas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                String valor = partes[0];
                int linhaCarta = Integer.parseInt(partes[1]);
                int colunaCarta = Integer.parseInt(partes[2]);
                Carta carta = new Carta(valor, linhaCarta, colunaCarta);
                carta.setFoiCombinada(true); // Marca a carta como já combinada
                cartasCombinadas.add(carta);
            }
        }
        return cartasCombinadas;
    }

    // Método para aplicar o estado carregado ao tabuleiro atual
    public void aplicarEstadoCarregado(Carta[][] tabuleiro, List<Carta> cartasCombinadas) {
        for (Carta cartaCarregada : cartasCombinadas) {
            tabuleiro[cartaCarregada.getLinha()][cartaCarregada.getColuna()] = cartaCarregada;
        }
    }
}
