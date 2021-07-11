package com.ciecursoandroid.abastecimentoeconomico.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_veiculo", indices = {@Index(value = {"nome"}, unique = true)})
public class Veiculo implements Parcelable {
    @Ignore
    public static final String TIPO_VEICULO_CARRO = "carro";
    @Ignore
    public static final String TIPO_VEICULO_MOTO = "moto";
    @Ignore
    public static final String TIPO_VEICULO_BASICO = "basico";
    @Ignore
    public static final String TIPO_VEICULO_KMSLITRO = "kmslitro";
    @PrimaryKey(autoGenerate = true)
    long id;
    String nome;
    Float kmsLitroCidadeGasolina;
    Float kmsLitroRodoviaGasolina;
    Float kmsLitroCidadeAlcool;
    Float kmsLitroRodoviaAlcool;
    String tipo;
    Boolean deleted = false;
    @Ignore
    int totalRegistros;
    /**
     * modelo de veicul falso para os calculos do tipo Basico e Kms/Litro
     */
    boolean fakeModel = false;

    public Veiculo() {
    }

    public boolean isFakeModel() {
        return fakeModel;
    }

    public void setFakeModel(boolean fakeModel) {
        this.fakeModel = fakeModel;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    protected Veiculo(Parcel in) {
        id = in.readLong();
        nome = in.readString();
        if (in.readByte() == 0) {
            kmsLitroCidadeGasolina = null;
        } else {
            kmsLitroCidadeGasolina = in.readFloat();
        }
        if (in.readByte() == 0) {
            kmsLitroRodoviaGasolina = null;
        } else {
            kmsLitroRodoviaGasolina = in.readFloat();
        }
        if (in.readByte() == 0) {
            kmsLitroCidadeAlcool = null;
        } else {
            kmsLitroCidadeAlcool = in.readFloat();
        }
        if (in.readByte() == 0) {
            kmsLitroRodoviaAlcool = null;
        } else {
            kmsLitroRodoviaAlcool = in.readFloat();
        }
        tipo = in.readString();
        byte tmpDeleted = in.readByte();
        deleted = tmpDeleted == 0 ? null : tmpDeleted == 1;
    }

    public static final Creator<Veiculo> CREATOR = new Creator<Veiculo>() {
        @Override
        public Veiculo createFromParcel(Parcel in) {
            return new Veiculo(in);
        }

        @Override
        public Veiculo[] newArray(int size) {
            return new Veiculo[size];
        }
    };

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getKmsLitroCidadeGasolina() {
        return kmsLitroCidadeGasolina;
    }

    public void setKmsLitroCidadeGasolina(Float kmsLitroCidadeGasolina) {
        this.kmsLitroCidadeGasolina = kmsLitroCidadeGasolina;
    }

    public Float getKmsLitroRodoviaGasolina() {
        return kmsLitroRodoviaGasolina;
    }

    public void setKmsLitroRodoviaGasolina(Float kmsLitroRodoviaGasolina) {
        this.kmsLitroRodoviaGasolina = kmsLitroRodoviaGasolina;
    }

    public Float getKmsLitroCidadeAlcool() {
        return kmsLitroCidadeAlcool;
    }

    public void setKmsLitroCidadeAlcool(Float kmsLitroCidadeAlcool) {
        this.kmsLitroCidadeAlcool = kmsLitroCidadeAlcool;
    }

    public Float getKmsLitroRodoviaAlcool() {
        return kmsLitroRodoviaAlcool;
    }

    public void setKmsLitroRodoviaAlcool(Float kmsLitroRodoviaAlcool) {
        this.kmsLitroRodoviaAlcool = kmsLitroRodoviaAlcool;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(nome);
        if (kmsLitroCidadeGasolina == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(kmsLitroCidadeGasolina);
        }
        if (kmsLitroRodoviaGasolina == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(kmsLitroRodoviaGasolina);
        }
        if (kmsLitroCidadeAlcool == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(kmsLitroCidadeAlcool);
        }
        if (kmsLitroRodoviaAlcool == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(kmsLitroRodoviaAlcool);
        }
        parcel.writeString(tipo);
        parcel.writeByte((byte) (deleted == null ? 0 : deleted ? 1 : 2));
    }
}
