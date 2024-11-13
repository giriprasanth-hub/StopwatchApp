// MainActivity.java
package com.example.stopwatchapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timerDisplay;
    private Button startButton, pauseButton, resetButton;

    private long startTime = 0L;  // Track when the stopwatch was started
    private long elapsedTime = 0L;  // Track total elapsed time when paused
    private Handler timerHandler = new Handler();
    private boolean isRunning = false;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            // Calculate elapsed time in milliseconds
            long currentTime = System.currentTimeMillis();
            long totalTime = elapsedTime + (currentTime - startTime);

            // Convert to minutes, seconds, and milliseconds
            int minutes = (int) (totalTime / 1000) / 60;
            int seconds = (int) (totalTime / 1000) % 60;
            int milliseconds = (int) (totalTime % 1000);

            // Update the display
            timerDisplay.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));

            // Run the handler every 10 milliseconds for accuracy
            timerHandler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views by ID
        timerDisplay = findViewById(R.id.timerDisplay);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

        // Start Button logic
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTime = System.currentTimeMillis();  // Record the start time
                    timerHandler.post(timerRunnable);  // Start the timer
                    isRunning = true;
                }
            }
        });

        // Pause Button logic
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    elapsedTime += System.currentTimeMillis() - startTime;  // Update elapsed time
                    timerHandler.removeCallbacks(timerRunnable);  // Pause the timer
                    isRunning = false;
                }
            }
        });

        // Reset Button logic
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler.removeCallbacks(timerRunnable);  // Stop the timer
                isRunning = false;
                startTime = 0L;
                elapsedTime = 0L;  // Reset elapsed time
                timerDisplay.setText("00:00:000");  // Reset display
            }
        });
    }
}
