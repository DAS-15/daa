import java.util.concurrent.Semaphore;

public class ConcurrentDiningPhilosopher {

    private static final int N = 5; // number of philosophers
    private static final int THINKING = 2;
    private static final int HUNGRY = 1;
    private static final int EATING = 0;
    private static final int LEFT(int i) { return (i + N - 1) % N; }
    private static final int RIGHT(int i) { return (i + 1) % N; }

    private static int[] state = new int[N];
    private static Semaphore mutex = new Semaphore(1);
    private static Semaphore[] S = new Semaphore[N];

    static {
        for(int i = 0; i < N; i++) {
            S[i] = new Semaphore(0);
        }
    }

    private static void test(int i) throws InterruptedException {
        if(state[i] == HUNGRY && state[LEFT(i)] != EATING && state[RIGHT(i)] != EATING) {
            state[i] = EATING;
            Thread.sleep(2000);
            System.out.println("Philosopher " + (i + 1) + " takes fork " + (LEFT(i) + 1) + " and " + (i + 1));
            System.out.println("Philosopher " + (i + 1) + " is Eating");
            S[i].release();
        }
    }

    private static void takeFork(int i) throws InterruptedException{
        mutex.acquire();
        state[i] = HUNGRY;
        System.out.println("Philosopher " + (i + 1) + " is hungry");
        test(i);
        mutex.release();
        S[i].acquire();
        Thread.sleep(1000);
    }

    private static void putFork(int i) throws InterruptedException { 
        mutex.acquire();
        state[i] = THINKING;
        System.out.println("Philosopher " + (i + 1) + " putting fork " + (LEFT(i) + 1) + " and " + (i + 1) + " down");
        System.out.println("Philosopher " + (i + 1) + " is thinking");
        test(LEFT(i));
        test(RIGHT(i));
        mutex.release();
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        takeFork(index);
                        putFork(index);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
            System.out.println("Philosopher " + (i + 1) + " is thinking");
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}