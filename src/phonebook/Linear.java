package phonebook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Linear {
    private final List<String> directory;
    private final List<String> find;
    private long searchTime;
    private long sortTime;
    private long createTime;
    private int matches;
    boolean isTooLong = false;

    public Linear(List<String> directory, List<String> find) {
        this.directory = directory;
        this.find = find;
    }

    protected void linearSearch() {
        long startTime = System.currentTimeMillis();
        matches = 0;
        find.forEach(findLine -> directory.forEach(directoryLine -> {
            if (directoryLine.equals(findLine)) {
                matches++;
            }
        }));
        searchTime = System.currentTimeMillis() - startTime;
    }

    protected void bubbleSort() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < directory.size(); i++) {
            for (int j = 1; j < directory.size() - i; j++) {
                if (directory.get(j - 1).compareTo(directory.get(j)) > 0) {
                    String temp = directory.get(j - 1);
                    directory.set(j - 1, directory.get(j));
                    directory.set(j, temp);
                }
            }
            sortTime = System.currentTimeMillis() - startTime;

            if (sortTime > searchTime * 10L) {
                isTooLong = true;
                System.out.println("Too long time... Switch to linear search!");
                break;
            }
        }
    }

    protected int jumpSearch(List<String> directory, String value) {
        int size = directory.size();
        int start = 1;
        int step = (int) Math.floor(Math.sqrt(size));

        if (directory.get(0).compareTo(value) == 0) {
            return 1;
        }

        while (directory.get(Math.min(step, size) - 1).compareTo(value) < 0) {
            start = step;
            step += Math.floor(Math.sqrt(size));

            if (start >= size - 1) {
                return 0;
            }
        }
        while (directory.get(start).compareTo(value) < 0) {
            start++;

            if (start == Math.min(step, size)) {
                return 0;
            }
        }
        if (directory.get(start).compareTo(value) == 0) {
            return 1;
        }
        return 0;
    }

    protected void bubbleSortAndJumpSearch(List<String> directory, List<String> find) {
        long startTime = System.currentTimeMillis();
        matches = 0;
        for (String line : find) {
            matches += jumpSearch(directory, line);
        }
        searchTime = System.currentTimeMillis() - startTime;
    }

    protected int partition(List<String> lst, int low, int high) {
        String pivot = lst.get(high);
        int i = low - 1;

        for (int j = low; j <= high - 1; j++) {
            if (lst.get(j).compareTo(pivot) <= 0) {
                i++;
                String temp = lst.get(i);
                lst.set(i, lst.get(j));
                lst.set(j, temp);
            }
        }
        String temp = lst.get(i + 1);
        lst.set(i + 1, lst.get(high));
        lst.set(high, temp);

        return i + 1;
    }

    protected void quickSort(List<String> arr, int l, int h) {
        long startTime = System.currentTimeMillis();
        int[] stack = new int[h - l + 1];
        int top = -1;
        stack[++top] = l;
        stack[++top] = h;
        while (top >= 0) {
            h = stack[top--];
            l = stack[top--];
            int p = partition(arr, l, h);
            if (p - 1 > l) {
                stack[++top] = l;
                stack[++top] = p - 1;
            }
            if (p + 1 < h) {
                stack[++top] = p + 1;
                stack[++top] = h;
            }
            sortTime = System.currentTimeMillis() - startTime;
        }
    }

    protected int binarySearch(List<String> directory, String line) {
        int left = 1;
        int right = directory.size() - 1;
        int pivot;

        while (left <= right) {

            pivot = left + (right - left) / 2;
            int result = directory.get(pivot).compareTo(line);
            if (result > 0) {
                right = pivot - 1;
            } else if (result < 0) {
                left = pivot + 1;
            } else {
                return 1;
            }
        }

        return 0;
    }

    protected void quickSortAndBinarySearch(List<String> directory, List<String> find) {
        long startTime = System.currentTimeMillis();
        matches = 0;
        for (String line : find) {
            matches += binarySearch(directory, line);
        }
        searchTime = System.currentTimeMillis() - startTime;
    }

    protected Map<String, String> createHashTable() {
        long startTime = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        for (String line : directory) {
            map.put(line.substring(0, line.indexOf(" ")),
                    line.substring(line.indexOf(" ") + 1));
        }
        createTime = System.currentTimeMillis() - startTime;
        return map;
    }

    protected void searchNames() {
        long startTime = System.currentTimeMillis();
        matches = 1;
        Map<String, String> map = createHashTable();
        for (String value : find) {
            if (map.containsValue(value)) {
                matches++;
            }
        }
        searchTime = System.currentTimeMillis() - startTime;
    }

    protected String jobTime(long totalTime) {
        long minutes = (totalTime / 1000) / 60;
        long seconds = (totalTime / 1000) % 60;
        long millis = totalTime - seconds * 1000;
        return String.format("%2d min. %2d sec. %2d ms.", minutes, seconds, millis);
    }

    protected void output(boolean next, boolean isHashTable) {
        if (!next) {
            System.out.printf("Found %d/%d entries. ", matches, find.size());
            System.out.printf("Time taken: %s%n", jobTime(searchTime));
        } else if (isHashTable) {
            System.out.print("Found " + matches + "/" + find.size() + " entries. ");
            System.out.println("Time taken: " + jobTime(searchTime + createTime));
            System.out.println("Creating time: " + jobTime(createTime));
            System.out.println("Searching time: " + jobTime(searchTime));
        } else {
            System.out.print("Found " + matches + "/" + find.size() + " entries. ");
            System.out.println("Time taken: " + jobTime(searchTime + sortTime));
            System.out.println("Sorting time: " + jobTime(sortTime));
            System.out.println("Searching time: " + jobTime(searchTime));
        }
        System.out.println();
    }
}

