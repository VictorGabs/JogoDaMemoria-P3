package com.p3.jogodamemoria;

public class Carta {
    public String valor;
    public int linha;
    public int coluna;
    public Boolean foiCombinada;

    public Carta(String valor, int linha, int coluna) {
        this.valor = valor;
        this.linha = linha;
        this.coluna = coluna;
        this.foiCombinada = false;
    }
}
