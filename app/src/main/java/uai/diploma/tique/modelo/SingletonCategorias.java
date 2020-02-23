package uai.diploma.tique.modelo;

import java.util.ArrayList;

public class SingletonCategorias {

    static SingletonCategorias Singcategorias;
    ArrayList<Categorias> categorias;
    int succatSelected;

    private SingletonCategorias(){
    }

    public static SingletonCategorias getInstance(){
        if(Singcategorias == null){
            Singcategorias = new SingletonCategorias();
        }

        return Singcategorias;
    }

    public void setCategorias(ArrayList<Categorias> categorias) {
        this.categorias = categorias;
    }

    public ArrayList<Categorias> getCategorias(){
        return categorias;
    }

    public ArrayList<Categorias> getCategorias(int codeCat) {
        for (Categorias cat: categorias) {
            if (cat.getCode() == codeCat)
                return (ArrayList<Categorias>) cat.getSubCategorias();
        }
        return new ArrayList<Categorias>();
    }

    public void setSubCatSelected(int codeSubcat) {
        this.succatSelected = codeSubcat;
    }

    public int getSubCatSelected(){
        return succatSelected;
    }
}
