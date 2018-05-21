package stempler.ofer.model;

/**
 * Created by ofer on 29/03/18.
 */
public class MyRun implements Runnable {

    private int k = 0;
    @Override
    public void run() {

        for (int i = 0; i < 5; i++) {
            k = k+1;
            System.out.println(Thread.currentThread().getName() + " k value: " + k);
        }
    }
}
