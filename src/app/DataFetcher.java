package app;

public class DataFetcher extends Thread {
    @Override
    public void run() {
        System.out.println("Fetching data in thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
