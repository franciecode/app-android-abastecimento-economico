package com.franciecode.abastecimentoeconomico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.franciecode.abastecimentoeconomico.R;

public abstract class BaseMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_abastecimentos:
                if (this instanceof AbastecimentosActivity) return false;
                Intent iAbastecimento = new Intent(BaseMenuActivity.this, AbastecimentosActivity.class);
                BaseMenuActivity.this.startActivity(iAbastecimento);
                break;
            case R.id.menu_abastecer:
                if (this instanceof MainActivity) return false;
                Intent iAbastecer = new Intent(this, MainActivity.class);
                startActivity(iAbastecer);
                break;
            case R.id.menu_veiculos:
                if (this instanceof VeiculosActivity) return false;
                Intent iVeiculos = new Intent(this, VeiculosActivity.class);
                startActivity(iVeiculos);
                break;
            case R.id.menu_chart:
                Intent iChart = new Intent(this, ChartActivity.class);
                startActivity(iChart);
                break;
        }
        return true;
    }

}
