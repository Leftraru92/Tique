package uai.diploma.tique.fragment;

import java.util.ArrayList;

import uai.diploma.tique.modelo.Categorias;

public interface IWebServiceFragment {

    void onWebServiceResult(ArrayList<?> lista);
    void onWebServiceResult(Object lista);
}
