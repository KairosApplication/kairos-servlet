package com.kairos.model;

import com.kairos.model.enums.TipoPlano;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class Empresa {

//    Atributos

    private int id;
    private String cnpj;
    private TipoPlano tipoPlano;

//    Construtores

    public Empresa(String cnpj, TipoPlano tipoPlano) {
        this.cnpj = cnpj;
        this.tipoPlano = tipoPlano;
    }

//    toString
    @Override
    public String toString() {
        return "-= Empresa =-" +
               "ID: " + getId() +
               "\nCNPJ: " + getCnpj() +
               "\nTipo do plano: " + getTipoPlano();
    }
}
