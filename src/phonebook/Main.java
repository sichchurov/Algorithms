package phonebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class Main {

    public static void main(String[] args) throws IOException {
        List<String> directory = Files.readAllLines(Path.of("source/found.txt"));
        List<String> phoneAndName = Files.readAllLines(Path.of("source/directory.txt"));
        List<String> find = Files.readAllLines(Path.of("source/find.txt"));

        Linear list = new Linear(directory, find);
        Linear hashMap = new Linear(phoneAndName, find);

        System.out.println("Start searching (linear search)...");
        list.linearSearch();
        list.output(false, false);

        System.out.println("Start searching (bubble sort + jump search)...");
        list.bubbleSort();
        if (list.isTooLong) {
            list.linearSearch();
        } else {
            list.bubbleSortAndJumpSearch(directory, find);
        }
        list.output(true, false);

        System.out.println("Start searching (quick sort + binary search)...");
        int low = 0;
        int high = directory.size() - 1;
        list.quickSort(directory, low, high);
        list.quickSortAndBinarySearch(directory, find);
        list.output(true, false);

        System.out.println("Start searching (hash table)...");
        hashMap.searchNames();
        hashMap.output(true, true);
    }
}
