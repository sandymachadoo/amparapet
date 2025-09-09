package com.amparapet.amparapet.dto;

public class AdocaoDTO {
    private String telefone;
    private String tipoResidencia;
    private String estado;
    private String cidade;
    private AnimalDTO animal;   // Substitui animalId
    private UsuarioDTO usuario;


    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getTipoResidencia() { return tipoResidencia; }
    public void setTipoResidencia(String tipoResidencia) { this.tipoResidencia = tipoResidencia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public AnimalDTO getAnimal() { return animal; }   // ✅ getter atualizado
    public void setAnimal(AnimalDTO animal) { this.animal = animal; } // ✅ setter atualizado

    public UsuarioDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }
}
