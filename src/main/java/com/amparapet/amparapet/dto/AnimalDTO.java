package com.amparapet.amparapet.dto;

public class AnimalDTO {
    private Long id;
    private String nome;
    private String idade;
    private String especie;
    private String raca;
    private String descricao;

    public AnimalDTO() {}


    public AnimalDTO(Long id, String nome, String idade, String especie, String raca, String descricao) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.especie = especie;
        this.raca = raca;
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIdade() { return idade; }
    public void setIdade(String idade) { this.idade = idade; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }
}
