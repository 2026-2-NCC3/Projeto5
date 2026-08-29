package com.example.nextstepapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.nextstepapp.ui.CursosActivity;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etMatricula;
    private EditText etSenha;
    private Button btnEntrar;
    private TextView tvErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Liga as variáveis Java aos elementos do XML, usando o id de cada um
        etMatricula = findViewById(R.id.etMatricula);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvErro = findViewById(R.id.tvErro);

        // Define o que acontece quando o botão é clicado
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tentarLogin();
            }
        });
    }

    private void tentarLogin() {
        String matricula = etMatricula.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        // Validação simples: campos não podem estar vazios
        if (TextUtils.isEmpty(matricula) || TextUtils.isEmpty(senha)) {
            tvErro.setText("Preencha matrícula e senha.");
            return;
        }

        // Por enquanto (sem API ainda) aceitamos qualquer login não vazio
        // e passamos a matrícula pra próxima tela.
        Intent intent = new Intent(MainActivity.this, CursosActivity.class);
        intent.putExtra("MATRICULA", matricula);
        startActivity(intent);
    }
}