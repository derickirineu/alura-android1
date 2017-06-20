package com.alura.derick.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alura.derick.agenda.basic.Aluno;
import com.alura.derick.agenda.dao.AlunoDAO;

import java.util.List;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView lista_alunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        lista_alunos = (ListView) findViewById(R.id.lista_alunos);

        // Método que captura o evento do click no item da lista (Qual aluno foi clicado)
        lista_alunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {

                Aluno aluno = (Aluno) lista_alunos.getItemAtPosition(position);
                // Cria a intent para passar os dados do aluno clicado para o FormularioActivity
                Intent intentFormularioEdit = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                // Passa o aluno serializado para a activity
                intentFormularioEdit.putExtra("aluno", aluno);
                // Inicializa a activity 
                startActivity(intentFormularioEdit);
            }
        });

        Button botaoNovoAluno = (Button) findViewById(R.id.btn_lista_alunos);

        botaoNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentFormulario);
            }
        });

        // Implementa o Menu de contexto na lista de alunos
        registerForContextMenu(lista_alunos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    /**
     * Gera um menu de contexto
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        // Cria um MenuItem para receber a referência do menu
        MenuItem deletar =  menu.add("Deletar");
        //Método para saber quando o MenuItem foi clicado
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                Aluno aluno = (Aluno) lista_alunos.getItemAtPosition(info.position);

                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deletarAluno(aluno);
                dao.close();
                carregarLista();
                Toast.makeText(ListaAlunosActivity.this, "Aluno " + aluno.getNome() + " deletado", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }


    private void carregarLista() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.listarAlunos();
        dao.close();


        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        lista_alunos.setAdapter(adapter);
    }
}
