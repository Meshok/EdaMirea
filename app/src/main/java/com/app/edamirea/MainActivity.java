package com.app.edamirea;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static final String COMMA_DELIMITER = ";";
    private final ArrayList<Meal> allMeals = new ArrayList<>();

    private LinearLayout mainLayout = null;

    private void getDataFromCsv() {
        try (InputStream inputStream = getAssets().open("data.csv")) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "windows-1251");
            Scanner scanner = new Scanner(inputStreamReader);
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] values = line.split(COMMA_DELIMITER);
                allMeals.add(new Meal(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MaterialCardView makeCard(Meal meal) {
        Context context = MainActivity.this;
        MaterialCardView card = new MaterialCardView(context);
        card.setLayoutParams(new LinearLayout.LayoutParams(480, 500, 0.45f));
        card.setPadding(0, 0, 0, 0);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(getResources().getColor(R.color.dirtPurple, null));
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ImageView imageView = new ImageView(context);
        int id = getResources().getIdentifier("id" + meal.getId(), "drawable", getPackageName());
        imageView.setImageResource(id);
        imageView.setPadding(0, 0, 0, 0);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 340));

        TextView mealName = new TextView(context);
        mealName.setTextColor(getResources().getColor(R.color.black, null));
        mealName.setText(meal.getName());
        mealName.setPadding(8, 0, 8, 0);
        mealName.setTextSize(13.0f);
        mealName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mealName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 103));

        TextView mealPrice = new TextView(context);
        mealPrice.setTextColor(getResources().getColor(R.color.black, null));
        mealPrice.setText(String.format("Цена: %.2f руб.", meal.getPrice()));
        mealPrice.setTextSize(13.0f);
        mealPrice.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mealPrice.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        layout.addView(imageView);
        layout.addView(mealName);
        layout.addView(mealPrice);

        card.addView(layout);

        return card;
    }

    private void showMeals(ArrayList<Meal> meals, String title) {
        Context context = MainActivity.this;
        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextColor(getResources().getColor(R.color.white, null));
        titleView.setPadding(40, 0, 0, 5);
        titleView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        titleView.setTextSize(30);
        titleView.setTypeface(titleView.getTypeface(), Typeface.ITALIC);
        mainLayout.addView(titleView);

        for (int i = 0; i < meals.size(); i += 2) {
            LinearLayout horLayout = new LinearLayout(context);
            horLayout.setOrientation(LinearLayout.HORIZONTAL);
            horLayout.setPadding(40, 12, 40, 12);
            horLayout.setWeightSum(1.0f);
            horLayout.addView(makeCard(meals.get(i)));
            Space space = new Space(context);
            space.setLayoutParams(new LinearLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT, 0.1f));
            horLayout.addView(space);
            if (i + 1 < meals.size()) {
                horLayout.addView(makeCard(meals.get(i + 1)));
            } else {
                Space second_space = new Space(context);
                second_space.setLayoutParams(new LinearLayout.LayoutParams(480, ViewGroup.LayoutParams.MATCH_PARENT, 0.45f));
                horLayout.addView(second_space);
            }
            mainLayout.addView(horLayout);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.scrollLayout);

        try {
            getDataFromCsv();
            ArrayList<Meal> soups = new ArrayList<>();
            ArrayList<Meal> drinks = new ArrayList<>();
            ArrayList<Meal> salads = new ArrayList<>();
            ArrayList<Meal> second_dishes = new ArrayList<>();
            ArrayList<Meal> side_dishes = new ArrayList<>();
            for (Meal meal : allMeals) {
                switch (meal.getType()) {
                    case soup:
                        soups.add(meal);
                        break;
                    case drink:
                        drinks.add(meal);
                        break;
                    case salad:
                        salads.add(meal);
                        break;
                    case second_course:
                        second_dishes.add(meal);
                        break;
                    case side_dish:
                        side_dishes.add(meal);
                }
            }

            showMeals(soups, "Супы");
            showMeals(second_dishes, "Вторые блюда");
            showMeals(side_dishes, "Гарниры");
            showMeals(salads, "Салаты");
            showMeals(drinks, "Напитки");

        } catch (RuntimeException e) {
            TextView textView = new TextView(MainActivity.this);
            textView.setTextSize(30.0f);
            textView.setText(e.getMessage());
            textView.setTextColor(getResources().getColor(R.color.white, null));
            mainLayout.addView(textView);
        }
    }
}