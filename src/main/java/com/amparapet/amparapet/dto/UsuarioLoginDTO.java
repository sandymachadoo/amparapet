package com.amparapet.amparapet.dto;


public class UsuarioLoginDTO {
    private String email;
    private String senha;

    public UsuarioLoginDTO() {}

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
