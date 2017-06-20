package com.alura.derick.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.alura.derick.agenda.basic.Aluno;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by derick on 16/06/17.
 */

public class AlunoDAO extends SQLiteOpenHelper {

    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, nota REAL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Alunos";
        db.execSQL(sql);
        onCreate(db);
    }

    @NonNull
    private ContentValues getContentValuesAluno(Aluno aluno) {
        ContentValues values = new ContentValues();
        // chave e valor (nome da coluna e valor a ser inserido)
        values.put("nome", aluno.getNome());
        values.put("endereco", aluno.getEndereco());
        values.put("telefone", aluno.getTelefone());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        return values;
    }

    /**
     * Metodo para inserir Aluno e com auxilio do SQLiteOpenHelper para evitar problemas com SQL Injector
     * @param aluno
     */
    public void inserirAluno(Aluno aluno) {
        // Devolve uma referência do banco de dados
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = getContentValuesAluno(aluno);

        // O insert exige com parâmetro o nome da table e os valores a serem inseridos, pra isso devemos criar um
        // ContentValues para armazenar os valores e inserir no banco
        database.insert("Alunos", null, values);
    }

    public List<Aluno> listarAlunos() {

        String sql = "SELECT * FROM Alunos;";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);

        List<Aluno> alunos = new ArrayList<Aluno>();

        while (cursor.moveToNext()){

            Aluno aluno = new Aluno();
            aluno.setId(cursor.getInt(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));

            alunos.add(aluno);

        }

        cursor.close();

        return alunos;
    }

    public void deletarAluno(Aluno aluno) {
        SQLiteDatabase database = getWritableDatabase();

        String[] params = {aluno.getId().toString()};
        // Tabela , clausula where, parâmetros
        database.delete("Alunos", "id = ?", params);
    }

    public void alterarAluno(Aluno aluno) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = getContentValuesAluno(aluno);

        String[] params = {aluno.getId().toString()};
        // Tabela , clausula where, parâmetros
        database.update("Alunos", values, "id = ?", params);
    }
}
