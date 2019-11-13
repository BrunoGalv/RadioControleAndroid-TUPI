package br.com.devmaker.rbn.model;

import com.google.android.exoplayer2.SimpleExoPlayer;

public class ValueClass {

    private static ValueClass instance;

    public static ValueClass getInstance() {
        if (instance == null)
            instance = new ValueClass();
        return instance;
    }

    private ValueClass() {
    }

    private SimpleExoPlayer player;
    private String nomeMusica, url, autor, imagem;
    private boolean audioTocando;

    public boolean isAudioTocando() {
        return audioTocando;
    }

    public void setAudioTocando(boolean audioTocando) {
        this.audioTocando = audioTocando;
    }

    public String getNomeMusica() {
        return nomeMusica;
    }

    public void setNomeMusica(String nomeMusica) {
        this.nomeMusica = nomeMusica;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SimpleExoPlayer player) {
        this.player = player;
    }
}
