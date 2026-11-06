package com.example.nextstepapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;

public class ChatIAActivity extends AppCompatActivity {

    private LinearLayout layoutMessages;
    private ScrollView scrollChat;
    private EditText etChatMessage;
    private ImageView btnSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ia);

        layoutMessages = findViewById(R.id.layoutMessages);
        scrollChat = findViewById(R.id.scrollChat);
        etChatMessage = findViewById(R.id.etChatMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        btnSendMessage.setOnClickListener(v -> enviarMensagemUsuario());

        setupSuggestionChips();
    }

    private void setupSuggestionChips() {
        View chip1 = findViewById(R.id.chip1);
        if (chip1 != null) chip1.setOnClickListener(v -> enviarPerguntaPronta("Como baixo meu certificado?"));

        View chip2 = findViewById(R.id.chip2);
        if (chip2 != null) chip2.setOnClickListener(v -> enviarPerguntaPronta("Como confirmo presença?"));

        View chip3 = findViewById(R.id.chip3);
        if (chip3 != null) chip3.setOnClickListener(v -> enviarPerguntaPronta("Onde fica a USP Butantã?"));

        View chip4 = findViewById(R.id.chip4);
        if (chip4 != null) chip4.setOnClickListener(v -> enviarPerguntaPronta("Quais são os cursos disponíveis?"));
    }

    private void enviarPerguntaPronta(String pergunta) {
        etChatMessage.setText(pergunta);
        enviarMensagemUsuario();
    }

    private void enviarMensagemUsuario() {
        String texto = etChatMessage.getText().toString().trim();
        if (TextUtils.isEmpty(texto)) return;

        // Adicionar balão do usuário (Direita, Roxo)
        adicionarBalaoUsuario(texto);
        etChatMessage.setText("");
        rolarParaFim();

        // Simular resposta da IA com delay de 600ms
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String respostaIa = gerarRespostaIA(texto);
            adicionarBalaoIA(respostaIa);
            rolarParaFim();
        }, 600);
    }

    private void adicionarBalaoUsuario(String texto) {
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setGravity(Gravity.END);
        row.setPadding(0, 8, 0, 8);

        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(60, 0, 0, 0);
        tv.setLayoutParams(params);
        tv.setBackgroundResource(R.drawable.bg_chat_user);
        tv.setPadding(32, 24, 32, 24);
        tv.setText(texto);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(14);

        row.addView(tv);
        layoutMessages.addView(row);
    }

    private void adicionarBalaoIA(String texto) {
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setGravity(Gravity.START);
        row.setPadding(0, 8, 0, 8);

        ImageView iconView = new ImageView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(64, 64);
        iconParams.setMargins(0, 8, 12, 0);
        iconView.setLayoutParams(iconParams);
        iconView.setImageResource(R.drawable.ic_chat);
        iconView.setBackgroundResource(R.drawable.light_purple_circle);
        iconView.setPadding(12, 12, 12, 12);
        iconView.setColorFilter(Color.parseColor("#5C33E6"));

        TextView tv = new TextView(this);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(0, 0, 60, 0);
        tv.setLayoutParams(tvParams);
        tv.setBackgroundResource(R.drawable.bg_chat_ai);
        tv.setPadding(32, 24, 32, 24);
        tv.setText(texto);
        tv.setTextColor(Color.parseColor("#3E2768"));
        tv.setTextSize(14);

        row.addView(iconView);
        row.addView(tv);
        layoutMessages.addView(row);
    }

    private String gerarRespostaIA(String mensagem) {
        String msgLower = mensagem.toLowerCase();

        if (msgLower.contains("certificado")) {
            return "Os certificados são gerados automaticamente para os cursos em que você atinge pelo menos 75% de frequência. Você pode visualizar e baixar seus certificados acessando a aba 'Cursos' ou no seu perfil.";
        } else if (msgLower.contains("presença") || msgLower.contains("presenca") || msgLower.contains("frequência")) {
            return "Para registrar sua presença, vá na aba 'Evolução' no menu inferior e apresente seu QR Code na entrada da aula. Você também pode utilizar o botão 'Confirmar Presença Manual' caso necessário.";
        } else if (msgLower.contains("onde") || msgLower.contains("local") || msgLower.contains("usp") || msgLower.contains("butantã") || msgLower.contains("butanta")) {
            return "Nossas atividades e aulas ocorrem no Campus Butantã da USP, localizado na Av. Prof. Luciano Gualberto, 374 - Butantã, São Paulo/SP.";
        } else if (msgLower.contains("curso") || msgLower.contains("aula") || msgLower.contains("horário") || msgLower.contains("horario")) {
            return "Atualmente temos os cursos:\n• Introdução à Programação Mobile (Aos Sábados, 10:00 - 11:00)\n• Inovação e Empreendedorismo (Sábados, 14:00 - 16:00)\n• Habilidades Profissionais (Sábados, 18:00 - 19:00)\n• Análise de Dados.\nVocê pode ver detalhes e se inscrever na aba 'Cursos'!";
        } else if (msgLower.contains("ong") || msgLower.contains("próxima etapa") || msgLower.contains("proxima etapa")) {
            return "A Próxima Etapa é uma ONG dedicada a capacitar jovens e estudantes através de cursos, mentorias e visitas técnicas em universidades e empresas para acelerar sua jornada profissional!";
        } else {
            return "Obrigado por perguntar! Para essa dúvida específica, você também pode verificar com os monitores da ONG Próxima Etapa no local da aula ou acessar as abas 'Agenda' e 'Cursos' no app.";
        }
    }

    private void rolarParaFim() {
        scrollChat.post(() -> scrollChat.fullScroll(View.FOCUS_DOWN));
    }
}
