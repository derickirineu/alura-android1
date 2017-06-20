package com.alura.derick.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alura.derick.agenda.basic.Aluno;
import com.alura.derick.agenda.dao.AlunoDAO;

public class FormularioActivity extends AppCompatActivity {

    // Responsável por guardar a referência de todos os campos de cadastro do formulário
    private FormularioHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        helper = new FormularioHelper(this);

        // Criar um objeto Intent para receber valores passado através de um Intent
        Intent intent = getIntent();
        // Recebe os dados que foram serializados
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");
        // Validação para verificar se será criado um novo aluno, ou alterado
        // Como foi criado um Intent pra receber alum valor que foi passado por outra Intent
        // Caso está Intent retorne NULL é porque será criado um novo aluno
        // Caso retorne algo ele retorna o formulario com os dados do aluno selecionado
        if (aluno != null){
            helper.preencherFormulario(aluno);
        }
    }

    /**
     * Método que cria um menu na action bar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // instância e infla o arquivo xml na tela (transformar em view)
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Trata o click do menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //pega o id do item selecionado
        switch (item.getItemId()){

            case R.id.menu_formulario_salvar:
                Aluno aluno = helper.pegarAluno();
                AlunoDAO dao = new AlunoDAO(this);

                if(aluno.getId() != null){
                    dao.alterarAluno(aluno);
                    Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() +" alterado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    dao.inserirAluno(aluno);
                    Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() +" salvo com sucesso!", Toast.LENGTH_SHORT).show();
                }

                dao.close();

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
