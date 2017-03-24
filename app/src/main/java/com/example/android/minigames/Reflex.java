package com.example.android.minigames;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class Reflex extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView[] easy_list = new ImageView[12];
    ImageView[] hard_list = new ImageView[25];
    int level, timer = 2000, counter = 0;
    boolean[] gameOverChecker;
    private Spinner difficulty;
    ViewSwitcher switcher;
    boolean easy_layout = true;
    GameOverHandler Handler = new GameOverHandler();
    Context reflexContext;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex);
        setUpSpinner();
        populateArray((LinearLayout) findViewById(R.id.e_layout), easy_list);
        populateArray((LinearLayout) findViewById(R.id.h_layout), hard_list);
        switcher = (ViewSwitcher) findViewById(R.id.game2switcher);
        reflexContext = this;
        buildInstructionDialog(this).show();
    }

    private AlertDialog buildInstructionDialog(Reflex reflex) {
        LayoutInflater inflater = reflex.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(reflex);
        builder.setTitle("Instruction")
                .setView(inflater.inflate(R.layout.reflex_instructions, null))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    private void setUpSpinner() {
        difficulty = (Spinner) findViewById(R.id.game2_difficulty);
        ArrayAdapter<CharSequence> difficultyadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.game2_difficulty,
                R.layout.spinner);
        difficultyadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(difficultyadapter);
        difficulty.setOnItemSelectedListener(this);
    }

    private void setUpClickEasy(ImageView view) {
        int random = (int) Math.round(Math.random() * 11);
        setUpSingleClick(view, 0, easy_list[random]);
    }

    private void setUpClickMedium(ImageView view) {
        int random = (int) Math.round(Math.random() * 11);
        if (Math.random() * 9 < 7.0) {
            setUpSingleClick(view, 1, easy_list[random]);
        } else
            setUpDontClick(view, 1, easy_list[random]);
    }

    private void setUpClickHard(ImageView view) {
        int random = (int) Math.round(Math.random() * 19);
        int selector = (int) Math.round(Math.random() * 9);
        if (selector < 7)
            setUpSingleClick(view, 2, hard_list[random]);
        else
            setUpDontClick(view, 2, hard_list[random]);
    }

    private void setUpClickExpert(ImageView view) {
        int random = (int) Math.round(Math.random() * 19);
        int selector = (int) Math.round(Math.random() * 14);
        if (selector < 9)
            setUpSingleClick(view, 3, hard_list[random]);
        else if (selector < 12)
            setUpDontClick(view, 3, hard_list[random]);
        else
            setUpLongClick(view, 3, hard_list[random]);
    }

    private void setUpSingleClick(ImageView view, final int level, final ImageView next_random) {
        if (counter < 2000) {
            final int count = counter++;
            incrementScore(count);
            view.setImageResource(R.mipmap.singleclick);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!gameOverChecker[count]) {
                        gameOverChecker[count] = true;
                        v.setOnClickListener(null);
                        ((ImageView) v).setImageResource(0);
                        switch (level) {
                            case 0:
                                setUpClickEasy(next_random);
                                break;
                            case 1:
                                setUpClickMedium(next_random);
                                break;
                            case 2:
                                setUpClickHard(next_random);
                                break;
                            case 3:
                                setUpClickExpert(next_random);
                        }
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Handler.post(new GameOverChecker(count, (ImageView) v));
                    return true;
                }
            });
            view.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    Handler.post(new GameOverChecker(count, (ImageView) v));
                    return true;
                }
            });
            if (timer > 1700)
                timer -= 100;
            else if (timer > 1500)
                timer -= 75;
            else if (timer > 700)
                timer -= 50;
            else if (timer > 550)
                timer -= 10;
            else
                timer -= 4;
            Handler.postDelayed(new GameOverChecker(count, view), timer);
        }
    }


    private void setUpDontClick(final ImageView view, int level, ImageView nextRandom) {
        final int count = counter++;
        incrementScore(count);
        view.setImageResource(R.mipmap.noclick);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setImageResource(0);
                view.setOnClickListener(null);
                Handler.post(new GameOverChecker(count, view));
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                view.setImageResource(0);
                view.setOnClickListener(null);
                Handler.post(new GameOverChecker(count, view));
                return true;
            }
        });
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Handler.post(new GameOverChecker(count, (ImageView) v));
                return true;
            }
        });
        Handler.postDelayed(new DontClickThread(count, level, view, nextRandom), timer * 2);
    }

    private void setUpLongClick(final ImageView view, final int level, final ImageView next_random) {
        if (counter < 2000) {
            final int count = counter++;
            incrementScore(count);
            view.setImageResource(R.mipmap.longclick);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!gameOverChecker[count]) {
                        gameOverChecker[count] = true;
                        v.setOnClickListener(null);
                        ((ImageView) v).setImageResource(0);
                        switch (level) {
                            case 0:
                                setUpClickEasy(next_random);
                                break;
                            case 1:
                                setUpClickMedium(next_random);
                                break;
                            case 2:
                                setUpClickHard(next_random);
                                break;
                            case 3:
                                setUpClickExpert(next_random);
                        }
                    }
                    return true;
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Handler.post(new GameOverChecker(count, view));
                }
            });
            view.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    Handler.post(new GameOverChecker(count, (ImageView) v));
                    return true;
                }
            });
            if (timer > 1700)
                timer -= 100;
            else if (timer > 1500)
                timer -= 75;
            else if (timer > 700) {
                timer -= 50;
            } else if (timer > 550)
                timer -= 10;
            else
                timer -= 4;
            Handler.postDelayed(new GameOverChecker(count, view), 2500);
        }
    }

    private void populateArray(LinearLayout parent, ImageView[] list) {
        int ll_count, imagecount;
        LinearLayout sub_parent;
        ll_count = parent.getChildCount();
        for (int i = 0; i < ll_count; i++) {
            sub_parent = (LinearLayout) parent.getChildAt(i);
            imagecount = sub_parent.getChildCount();
            for (int j = 0; j < imagecount; j++) {
                list[i * imagecount + j] = (ImageView) sub_parent.getChildAt(j);
            }
        }
    }

    public void start_game(View view) {
        int random;
        //level only for score calculation
        level = difficulty.getSelectedItemPosition() + 1;
        score = 0;
        counter = 0;
        timer = 2000;
        ImageView clickImage;
        view.setEnabled(false);
        findViewById(R.id.game2_difficulty).setEnabled(false);
        gameOverChecker = new boolean[2000];
        //pass random number + current difficulty + image to attach.
        switch (difficulty.getSelectedItemPosition()) {
            case 0:
                random = (int) Math.round(Math.random() * (easy_list.length - 1));
                clickImage = easy_list[random];
                setUpClickEasy(clickImage);
                break;
            case 1:
                random = (int) Math.round(Math.random() * (easy_list.length - 1));
                clickImage = easy_list[random];
                setUpClickMedium(clickImage);
                break;
            case 2:
                random = (int) Math.round(Math.random() * (hard_list.length - 1));
                clickImage = hard_list[random];
                setUpClickHard(clickImage);
                break;
            case 3:
                random = (int) Math.round(Math.random() * (hard_list.length - 1));
                clickImage = hard_list[random];
                setUpClickExpert(clickImage);
                break;
        }
    }

    private void incrementScore(int count) {
        score += Math.round(5 * count * (Math.sqrt(level) * 0.3 + 0.5));
        ((TextView) findViewById(R.id.score)).setText(String.valueOf(score));
    }

    //callback methods for spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 1 && easy_layout) {
            easy_layout = false;
            switcher.showNext();
        }
        if (position <= 1 && !easy_layout) {
            easy_layout = true;
            switcher.showPrevious();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class GameOverHandler extends Handler {
    }

    class GameOverChecker implements Runnable {
        int count;
        ImageView view;

        GameOverChecker(int count, ImageView view) {
            this.count = count;
            this.view = view;
        }

        @Override
        public void run() {
            if (!gameOverChecker[count]) {
                gameOverChecker[count] = true;
                findViewById(R.id.game2_difficulty).setEnabled(true);
                findViewById(R.id.G2_Start).setEnabled(true);
                //popup game over Checking if back butoon ispressed.
                if(!((Activity) reflexContext).isFinishing())
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(reflexContext);
                    builder1.setTitle("Game Over");
                    builder1.setMessage("Your score is " + score + "\nPress start to play again.");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                for (int i = 0; i < easy_list.length; i++) {
                    easy_list[i].setImageResource(0);
                    easy_list[i].setOnClickListener(null);
                    easy_list[i].setOnLongClickListener(null);
                    easy_list[i].setOnDragListener(null);
                }
                for (int i = 0; i < hard_list.length; i++) {
                    hard_list[i].setImageResource(0);
                    hard_list[i].setOnClickListener(null);
                    hard_list[i].setOnLongClickListener(null);
                    hard_list[i].setOnDragListener(null);
                }
                view.setImageResource(0);
                view.setOnClickListener(null);
                view.setOnLongClickListener(null);
                view.setOnDragListener(null);
            }
        }
    }

    class DontClickThread implements Runnable {
        int count, level;
        ImageView view, next_random;

        DontClickThread(int count, int level, ImageView view, ImageView next_random) {
            this.count = count;
            this.level = level;
            this.view = view;
            this.next_random = next_random;
        }

        @Override
        public void run() {
            if (!gameOverChecker[count]) {
                gameOverChecker[count] = true;
                view.setImageResource(0);
                view.setOnClickListener(null);
                switch (level) {
                    case 0:
                        setUpClickEasy(next_random);
                        break;
                    case 1:
                        setUpClickMedium(next_random);
                        break;
                    case 2:
                        setUpClickHard(next_random);
                        break;
                    case 3:
                        setUpClickExpert(next_random);
                }
            }
        }
    }
}

