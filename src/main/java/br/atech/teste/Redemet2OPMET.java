package br.atech.teste;

import java.util.Date;

public class Redemet2OPMET {


    private long idMens;
    private Date dataRecebimento;
    private String descMens;
    private String idRedemet;
    private Date dtEnvioOpmet;
    private String rementente;

    /**
     * @return the idMens
     */
    public long getIdMens() {
        return idMens;
    }

    /**
     * @param idMens the idMens to set
     */
    public void setIdMens(long idMens) {
        this.idMens = idMens;
    }

    /**
     * @return the dataRecebimento
     */
    public Date getDataRecebimento() {
        return dataRecebimento;
    }

    /**
     * @param dataRecebimento the dataRecebimento to set
     */
    public void setDataRecebimento(Date dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    /**
     * @return the descMens
     */
    public String getDescMens() {
        return descMens;
    }

    /**
     * @param descMens the descMens to set
     */
    public void setDescMens(String descMens) {
        this.descMens = descMens;
    }

    /**
     * @return the idRedemet
     */
    public String getIdRedemet() {
        return idRedemet;
    }

    /**
     * @param idRedemet the idRedemet to set
     */
    public void setIdRedemet(String idRedemet) {
        this.idRedemet = idRedemet;
    }

    /**
     * @return the dtEnvioOpmet
     */
    public Date getDtEnvioOpmet() {
        return dtEnvioOpmet;
    }

    /**
     * @param dtEnvioOpmet the dtEnvioOpmet to set
     */
    public void setDtEnvioOpmet(Date dtEnvioOpmet) {
        this.dtEnvioOpmet = dtEnvioOpmet;
    }

    /**
     * @return the rementente
     */
    public String getRementente() {
        return rementente;
    }

    /**
     * @param rementente the rementente to set
     */
    public void setRementente(String rementente) {
        this.rementente = rementente;
    }
}
