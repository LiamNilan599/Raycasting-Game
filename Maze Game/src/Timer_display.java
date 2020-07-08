import java.util.Timer;
import java.util.TimerTask;

public class Timer_display
{
    static Timer timer;
    public int mins , secs;
    private int period;
    private int delay;

    public Timer_display()
    {
        delay = 1000;
        period = 1000;
        secs = 0;
        mins = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                ++secs;
                if (secs > 59)
                {
                    secs = 0;
                    ++mins;
                }
            }
        },delay,period);
    }
    public void Stop()
    {
        timer.cancel();
    }

    @Override
    public String toString()
    {
        return String.format("%d:%d", mins, secs);
    }
}