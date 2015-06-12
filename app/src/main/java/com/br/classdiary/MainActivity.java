package com.br.classdiary;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alunoOnclick(View view) {
        emConstrucao();
    }

    public void disciplinaOnclick(View view) {
        emConstrucao();
    }

    private void emConstrucao() {
        Toast toast = Toast.makeText(this, "Em construção", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void chamadaOnclick(View view) {
        emConstrucao();
    }

    public void professorOnclick(View view) {
        emConstrucao();
    }

    public void turmaOnclick(View view) {

        Toast toast = Toast.makeText(this, "Sincronizando...aguarde...", Toast.LENGTH_LONG);
        toast.show();

        startActivity(new Intent(this, TurmaActivity.class));
    }

    public void configuracaoOnClick(View view) {
        emConstrucao();
    }
}
