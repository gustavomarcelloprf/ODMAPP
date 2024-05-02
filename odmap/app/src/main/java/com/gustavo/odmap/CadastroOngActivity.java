package com.gustavo.odmap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroOngActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextLink;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private EditText editTextCnpj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ong);

        // Inicialize os EditTexts
        editTextNome = findViewById(R.id.editTextNome);
        editTextLink = findViewById(R.id.editTextLink);
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        editTextCnpj = findViewById(R.id.editTextCnpj);

        // Configura o botão de salvar/cadastrar
        Button buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarMarcador();
            }
        });

        // Adiciona o TextWatcher para formatar e permitir apagar o CNPJ
        editTextCnpj.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String cnpj = s.toString();

                if (cnpj.isEmpty()) {
                    isUpdating = true;
                    editTextCnpj.setText("");
                    editTextCnpj.setSelection(0);
                    return;
                }

                if (cnpj.length() == 14) {
                    // Formata o CNPJ
                    cnpj = cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8)
                            + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14);

                    isUpdating = true;
                    editTextCnpj.setText(cnpj);
                    editTextCnpj.setSelection(cnpj.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void adicionarMarcador() {
        String nome = editTextNome.getText().toString();
        String link = editTextLink.getText().toString();
        double latitude = Double.parseDouble(editTextLatitude.getText().toString());
        double longitude = Double.parseDouble(editTextLongitude.getText().toString());
        String cnpj = editTextCnpj.getText().toString().replaceAll("[^0-9]", ""); // Remove caracteres não numéricos

        Intent intent = new Intent();
        intent.putExtra("nome", nome);
        intent.putExtra("link", link);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("cnpj", cnpj);
        setResult(RESULT_OK, intent);
        finish();
    }
}
