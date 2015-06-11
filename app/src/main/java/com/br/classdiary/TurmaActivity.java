package com.br.classdiary;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TurmaActivity extends ListActivity {

    private List<Map<String, Object>> turmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //consumindo JSON


        String[] de = {"turmaId", "turmaNome"};
        int[] para = {R.id.turmaId, R.id.turmaNome};

        SimpleAdapter adapter = new SimpleAdapter(this, listarTurmas(),
                R.layout.activity_turma, de, para);
        setListAdapter(adapter);

        //getListView().setOnItemClickListener(this);
    }



    private List<Map<String, Object>> listarTurmas() {
        turmas = new ArrayList<Map<String,Object>>();

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("turmaId", 1);
        item.put("turmaNome", "Web 3.0");
        turmas.add(item);

        item = new HashMap<String, Object>();
        item.put("turmaId", 2);
        item.put("turmaNome", "Gerencia Projeto");
        turmas.add(item);

        item = new HashMap<String, Object>();
        item.put("turmaId", 3);
        item.put("turmaNome", "Governança TI");
        turmas.add(item);

        return turmas;
    }

    /*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        TextView textView = (TextView) view;
        String mensagem = "Turma selecionada: " + textView.getText();
        Toast.makeText(getApplicationContext(), mensagem,
                Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(this, GastoListActivity.class));
    }*/

}
