import java.util.*;
import java.util.concurrent.*;

import static java.lang.System.*;

public class Main {


    public static void main(String[] args) {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }
        List<Future> future = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        long startTs = currentTimeMillis(); // start time
        for (String text : texts) {

            final Callable<String> textStrings = () -> {

                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                return (text.substring(0, 100) + " -> " + maxSize);
            };
            future.add(threadPool.submit(textStrings));
        }

        long endTs = currentTimeMillis(); // end time
        out.println("Time: " + (endTs - startTs) + "ms");


        try {
            for (Future eachFuture : future) {
                System.out.println(eachFuture.get());
            }
        }catch (InterruptedException| ExecutionException e){
            e.printStackTrace();
        }
        threadPool.shutdown();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}