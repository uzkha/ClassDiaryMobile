package com.br.classdiary.Connection;


import android.os.StrictMode;

import com.br.classdiary.model.Turma;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class TurmaRest {

    private static final String URL_WS = "http://10.0.2.2:8080/classdiary/api/turma/";

    public List<Turma> getListaTurma() throws Exception {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        String[] resposta = new WebServiceCliente().get(URL_WS + "getTurmas");


        if (resposta[0].equals("200")) {
            Gson gson = new Gson();
            ArrayList<Turma> listaTurma = new ArrayList<Turma>();
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(resposta[1]).getAsJsonArray();

            for (int i = 0; i < array.size(); i++) {
                listaTurma.add(gson.fromJson(array.get(i), Turma.class));
            }
            return listaTurma;
        } else {
            throw new Exception("Falha ao sincronizar dados com o servidor: " + resposta[1]);
        }
    }
}