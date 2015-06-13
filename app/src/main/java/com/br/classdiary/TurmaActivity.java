package com.br.classdiary;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.br.classdiary.rest.TurmaRest;
import com.br.classdiary.dao.DatabaseHelper;
import com.br.classdiary.model.Turma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TurmaActivity extends ListActivity implements AdapterView.OnItemClickListener {

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
        getListView().setOnItemClickListener(this);

        // registramos aqui o novo menu de contexto
        registerForContextMenu(getListView());



    }

    private void inserirTurmas(List<Turma> listaTurmas) throws Exception {

        SQLiteDatabase db = helper.getWritableDatabase();

        for(Turma turma : listaTurmas) {

            ContentValues values = new ContentValues();
            values.put("_id", turma.getId());
            values.put("nome", turma.getNome());

            long resultado = db.insert("turma", null, values);
            if(resultado == -1 ){
                throw new Exception("Falha ao inserir os dados");
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
    }



    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view, int position, long id) {
       Toast.makeText(this, "Pressione o registro para obter o menu de opções.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remover) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            turmas.remove(info.position);
            getListView().invalidateViews();


            // remover do banco de dados
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_turma, menu);
    }


}
