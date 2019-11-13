package br.com.devmaker.rbn.model;

import java.io.Serializable;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class Premio  implements Serializable {
    private String tituloPremio;
    private String imagemPremio;

    public Premio() {
    }

    public Premio(String tituloPremio, String imagemPremio) {
        this.tituloPremio = tituloPremio;
        this.imagemPremio = imagemPremio;
    }

    public String getTituloPremio() {
        return tituloPremio;
    }

    public void setTituloPremio(String tituloPremio) {
        this.tituloPremio = tituloPremio;
    }

    public String getImagemPremio() {
        return imagemPremio;
    }

    public void setImagemPremio(String imagemPremio) {
        this.imagemPremio = imagemPremio;
    }
}
