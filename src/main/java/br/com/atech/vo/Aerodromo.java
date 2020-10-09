package br.com.atech.vo;

import com.vividsolutions.jts.geom.Point;

public class Aerodromo {
  public Aerodromo() {}
  
  public String getNome() { return nome; }
  
  private String nome;
  public void setNome(String nome) {
    this.nome = nome;
  }
  
  private Point latlon;
  private Double nivel;
  public Point getLatlon() {
    return latlon;
  }
  
  public void setLatlon(Point latlon) {
    this.latlon = latlon;
  }
  
  public String toString()
  {
    return "Aerodromo [nome=" + nome + "]";
  }
  
  public Double getNivel() {
    return nivel;
  }
  
  public void setNivel(Double nivel) {
    this.nivel = nivel;
  }
}
