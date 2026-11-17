package com.example.nextstepapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;

public class AlterarSenhaActivity extends AppCompatActivity {

    private EditText etSenhaAtual, etNovaSenha, etConfirmarNovaSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setClickable(true);
            btnBack.setFocusable(true);
            btnBack.setOnClickListener(v -> finish());
        }

        etSenhaAtual = findViewById(R.id.etSenhaAtual);
        etNovaSenha = findViewById(R.id.etNovaSenha);
        etConfirmarNovaSenha = findViewById(R.id.etConfirmarNovaSenha);

        findViewById(R.id.btnConfirmarAlteracaoSenha).setOnClickListener(v -> alterarSenha());
    }

    private void alterarSenha() {
        String senhaAtual = etSenhaAtual.getText().toString();
        String novaSenha = etNovaSenha.getText().toString();
        String confirmarSenha = etConfirmarNovaSenha.getText().toString();

        if (TextUtils.isEmpty(senhaAtual) || TextUtils.isEmpty(novaSenha) || TextUtils.isEmpty(confirmarSenha)) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (novaSenha.length() < 8) {
            etNovaSenha.setError("A nova senha deve ter pelo menos 8 caracteres.");
            etNovaSenha.requestFocus();
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            etConfirmarNovaSenha.setError("As senhas não coincidem.");
            etConfirmarNovaSenha.requestFocus();
            return;
        }

        Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
