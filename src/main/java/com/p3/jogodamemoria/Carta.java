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

    public Carta() {

    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public Boolean getFoiCombinada() {
        return foiCombinada;
    }

    public void setFoiCombinada(Boolean foiCombinada) {
        this.foiCombinada = foiCombinada;
    }
}
