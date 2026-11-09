package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.MainActivity;
import com.example.nextstepapp.R;
import com.example.nextstepapp.session.SessionManager;
import com.google.android.material.button.MaterialButton;

public class PerfilActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private EditText etStudentName, etEmail, etTelefone, etNascimento, etEscola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        sessionManager = new SessionManager(this);

        // Inicializar Campos Editáveis
        etStudentName = findViewById(R.id.etStudentName);
        etEmail = findViewById(R.id.etEmail);
        etTelefone = findViewById(R.id.etTelefone);
        etNascimento = findViewById(R.id.etNascimento);
        etEscola = findViewById(R.id.etEscola);

        // Botão Voltar
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Ícone de notificação na barra superior
        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    startActivity(new Intent(this, NotificacoesActivity.class))
            );
        }

        // Perfil (já está na tela de perfil)
        View btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                // Já está no perfil
            });
        }

        // Botão Salvar Alterações
        MaterialButton btnSalvarPerfil = findViewById(R.id.btnSalvarPerfil);
        if (btnSalvarPerfil != null) {
            btnSalvarPerfil.setOnClickListener(v -> {
                String nome = etStudentName.getText().toString();
                String email = etEmail.getText().toString();
                String telefone = etTelefone.getText().toString();
                String nascimento = etNascimento.getText().toString();
                String escola = etEscola.getText().toString();

                Toast.makeText(this, "Dados cadastrais salvos com sucesso!", Toast.LENGTH_SHORT).show();
            });
        }

        // Alterar senha
        View rowAlterarSenha = findViewById(R.id.rowAlterarSenha);
        if (rowAlterarSenha != null) {
            rowAlterarSenha.setOnClickListener(v ->
                    Toast.makeText(this, "Funcionalidade de alterar senha em breve.", Toast.LENGTH_SHORT).show()
            );
        }

        // Termos de uso
        View rowTermos = findViewById(R.id.rowTermos);
        if (rowTermos != null) {
            rowTermos.setOnClickListener(v ->
                    Toast.makeText(this, "Exibindo Termos de Uso e Privacidade.", Toast.LENGTH_SHORT).show()
            );
        }

        // Botão Sair da Conta
        MaterialButton btnLogout = findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                if (sessionManager != null) {
                    sessionManager.limparSessao();
                }
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        // Bottom Navigation
        View navInicio = findViewById(R.id.nav_inicio);
        if (navInicio != null) {
            navInicio.setOnClickListener(v -> {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });
        }

        View navAgenda = findViewById(R.id.nav_agenda);
        if (navAgenda != null) {
            navAgenda.setOnClickListener(v ->
                    startActivity(new Intent(this, AgendaActivity.class))
            );
        }

        View navPresencas = findViewById(R.id.nav_presencas);
        if (navPresencas != null) {
            navPresencas.setOnClickListener(v ->
                    startActivity(new Intent(this, PresencaActivity.class))
            );
        }

        View navNotificacoes = findViewById(R.id.nav_notificacoes);
        if (navNotificacoes != null) {
            navNotificacoes.setOnClickListener(v ->
                    startActivity(new Intent(this, NotificacoesActivity.class))
            );
        }
    }
}
