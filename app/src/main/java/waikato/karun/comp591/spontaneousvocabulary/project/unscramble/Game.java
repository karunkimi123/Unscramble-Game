package waikato.karun.comp591.spontaneousvocabulary.project.unscramble;

import android.content.ClipData;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Game extends AppCompatActivity implements HintFragment.OnFragmentInteractionListener {

    public static final String DRAG_EVENT = "DRAG EVENT";
    private final String word1 = "SATIRE";

    private final TextView[] myTextViews = new TextView[word1.length()];
    private float[] textviewXvalues = null;
    private TextView textViewtime;

    private CharSequence draggedChar;
    private int draggedPosition;

    private LinearLayout gameLayout;
    private HintFragment hintFragment;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_chooser);

        textViewtime = (TextView)findViewById(R.id.textView_time);
        textViewtime.setText("03:00");

        this.hintfragmentcreation();
        mHandler = new Handler();

        final CounterClass countdownTimer = new CounterClass(180000,1000);
        countdownTimer.start();

        String shuffledString = shuffle(word1);

        this.initializeGameLayout(shuffledString);
        this.textviewDragListeners();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            hintFragment.updateHints();
            mHandler.postDelayed(runnable,60000);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        runnable.run();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void hintfragmentcreation() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hintFragment = HintFragment.newInstance(null, null);
        fragmentTransaction.add(R.id.fragment_layout, hintFragment);
        fragmentTransaction.commit();
    }

    private void initializeGameLayout(String s) {
        gameLayout = (LinearLayout) findViewById(R.id.game_linear_layout);
        gameLayout.setWeightSum((float) word1.length());
        gameLayout.setWeightSum(1.0f);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = (float) 1 / word1.length();

        for (int i = 0; i < word1.length(); i++) {
            Log.d("Creating TextViews", "loop");
            final TextView textView = new TextView(this);
            textView.setText("" + s.charAt(i));
            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(data, shadowBuilder, v, 0);
                        v.setVisibility(View.INVISIBLE);
                        return true;
                    }
                    return false;
                }
            });
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.font_size));
            textView.setTextColor(Color.WHITE);
            textView.setLayoutParams(params);
            textView.setTag(R.id.position_tag, i);
            gameLayout.addView(textView);
            myTextViews[i] = textView;
        }

    }

    private void createPositions() {
        textviewXvalues = new float[myTextViews.length];
        for (int i = 0; i < textviewXvalues.length; i++) {
            textviewXvalues[i] = myTextViews[i].getX();
        }
    }

    private void textviewDragListeners() {
        gameLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                final int action = event.getAction();
                TextView draggedItem = (TextView) event.getLocalState();
                int emptyPos = (int) draggedItem.getTag(R.id.position_tag);


                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d(DRAG_EVENT, "action started");
                        if (textviewXvalues == null) createPositions();
                        draggedChar = draggedItem.getText();
                        draggedPosition = (int) draggedItem.getTag(R.id.position_tag);
                        Log.d("dragged item text", (String) draggedChar);
                        for (TextView t : myTextViews) {
                            Log.d("TextView", (String) t.getText());
                        }
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(DRAG_EVENT, "action entered");
                        for (TextView t : myTextViews) {
                            Log.d("TextView", (String) t.getText());
                        }
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.d(DRAG_EVENT, "action drag location");
                        for (TextView t : myTextViews) {
                            Log.d("TextView", (String) t.getText());
                        }
                        float currentX = event.getX();

                        if (emptyPos < textviewXvalues.length - 1 && currentX > textviewXvalues[emptyPos + 1]) {
                            TextView adjacentView = myTextViews[emptyPos + 1];
                            adjacentView.setVisibility(View.INVISIBLE);
                            CharSequence charSequence = adjacentView.getText();

                            TextView newView = myTextViews[emptyPos];
                            newView.setText(charSequence);
                            newView.setVisibility(View.VISIBLE);

                            draggedItem.setTag(R.id.position_tag, emptyPos + 1);
                        } else if (emptyPos > 0 && currentX < textviewXvalues[emptyPos]) {
                            TextView adjacentView = myTextViews[emptyPos - 1];
                            adjacentView.setVisibility(View.INVISIBLE);
                            CharSequence charSequence = adjacentView.getText();

                            TextView newView = myTextViews[emptyPos];
                            newView.setText(charSequence);
                            newView.setVisibility(View.VISIBLE);

                            draggedItem.setTag(R.id.position_tag, emptyPos - 1);
                        }
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(DRAG_EVENT, "action exited");
                        return true;
                    case DragEvent.ACTION_DROP:
                        handleDrop(emptyPos);
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:

                        if(dropEventNotHandled(event)){
                            handleDrop(emptyPos);
                        }

                        Log.d(DRAG_EVENT, "action ended");

                        draggedItem.setTag(R.id.position_tag, draggedPosition);

                        StringBuilder sb = new StringBuilder();
                        for (TextView t : myTextViews) {
                            sb.append(t.getText());
                        }

                        if (sb.toString().equals(word1)) {
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(500); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);

                            for (TextView t : myTextViews) {
                                t.setTextColor(Color.RED);
                                t.startAnimation(anim);
                            }
                        }

                        return true;
                    default:
                        Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                        break;
                }
                return false;
            }

            private void handleDrop(int emptyPos) {
                Log.d(DRAG_EVENT, "action dropped");
                TextView emptyView = myTextViews[emptyPos];
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(draggedChar);
                for (TextView t : myTextViews) {
                    Log.d("TextView", (String) t.getText());
                }
            }
        });
    }


    private boolean dropEventNotHandled(DragEvent dragEvent) {
        return !dragEvent.getResult();
    }


    public String shuffle(String input) {
        List<Character> characters = new ArrayList<Character>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while (characters.size() != 0) {
            int randPicker = (int) (Math.random() * characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            textViewtime.setText(hms);
        }

        @Override
        public void onFinish() {
            mHandler.removeCallbacks(runnable);
        }

    }

}
