package com.br.classdiary;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.br.classdiary.Connection.TurmaRest;
import com.br.classdiary.factory.DatabaseHelper;
import com.br.classdiary.model.Turma;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TurmaActivity extends ListActivity {

    private List<Map<String, Object>> turmas;
    private DatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //consumindo JSON para atualizacao das turmas
        List<Turma> listaTurmas = null;
        try {

            // prepara acesso ao banco de dados
            helper = new DatabaseHelper(this);

            listaTurmas = sincronizarTurmaRest();

            deletarTurmas();

            inserirTurmas(listaTurmas);

            carregarView();

        } catch (Exception e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();

            finish();
        }



    }

    private void carregarView() {

        String[] de = {"turmaId", "turmaNome"};
        int[] para = {R.id.turmaId, R.id.turmaNome};

        SimpleAdapter adapter =
                new SimpleAdapter(this, listarTurmas(),
                        R.layout.activity_turma, de, para);
        setListAdapter(adapter);

    }

    private void inserirTurmas(List<Turma> listaTurmas) {

        SQLiteDatabase db = helper.getWritableDatabase();

        for(Turma turma : listaTurmas) {

            ContentValues values = new ContentValues();
            values.put("_id", turma.getId());
            values.put("nome", turma.getNome());

            long resultado = db.insert("turma", null, values);
            if(resultado == -1 ){
                Toast.makeText(this, "Ocorreu um erro ao salvar a turma " + turma.getNome(),
                        Toast.LENGTH_LONG).show();
            }

        }

    }

    private void deletarTurmas() {

        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete("turma",null,null);

    }



    private List<Turma> sincronizarTurmaRest() throws Exception {

        TurmaRest turmaRest = new TurmaRest();
        List<Turma> listaTurmas = new ArrayList<Turma>();
        listaTurmas = turmaRest.getListaTurma();

        return listaTurmas;
    }


    private List<Map<String, Object>> listarTurmas() {

        //inicia consulta
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT _id, nome FROM turma",  null);

        //move cursor para primeiro registro
        cursor.moveToFirst();

        turmas = new ArrayList<Map<String,Object>>();

        for (int i = 0; i < cursor.getCount(); i++) {

            Map<String, Object> item = new HashMap<String, Object>();

            int id      = cursor.getInt(0);
            String nome  = cursor.getString(1);

            item.put("turmaId", id);
            item.put("turmaNome", nome);

            turmas.add(item);
            //move cursor proximo registro
            cursor.moveToNext();
        }

        //fecha cursor
        cursor.close();

        return turmas;

        //forma manual
        /*Map<String, Object> item = new HashMap<String, Object>();
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
        turmas.add(item);*/


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


    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

}
