package com.amparapet.amparapet.dto;

public class AdocaoDTO {

    private Long id;
    private String telefone;
    private String tipoResidencia;
    private String estado;
    private String cidade;
    private AnimalDTO animal;
    private UsuarioDTO usuario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getTipoResidencia() { return tipoResidencia; }
    public void setTipoResidencia(String tipoResidencia) { this.tipoResidencia = tipoResidencia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public AnimalDTO getAnimal() { return animal; }
    public void setAnimal(AnimalDTO animal) { this.animal = animal; }

    public UsuarioDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }
}
