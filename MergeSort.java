public class MergeSort {
    public static void main(String[] args) {
        int[] arr = {6,4,9,5,3,8,2,4};
        mergeSort(arr, 0, arr.length-1);
        System.out.println(arr);
    }

    public static void mergeSort(int[] arr, int start, int end) {
        if(start < end) {
            int mid = start+(end - start)/2;
            mergeSort(arr, start, mid-1);
            mergeSort(arr, mid, end);
            System.out.println("start: " + start + " mid: " + mid + " end: " + end + " array: " + arr.toString());
            merge(arr, start, mid, end);
        }
    }

    public static void merge(int[] arr, int start, int mid, int end) {
        int leftArraySize = mid - start + 1;
        int rightArraySize = end - mid;

        int[] leftArray = new int[leftArraySize];
        int[] rightArray = new int[rightArraySize];

        for (int i = 0; i < leftArraySize; i++)
            leftArray[i] = arr[start + i];

        for (int j = 0; j < rightArraySize; j++)
            rightArray[j] = arr[mid + 1 + j];

        int i = 0, j = 0;
        int k = start;

        while (i < leftArraySize && j < rightArraySize) {
            if (leftArray[i] <= rightArray[j]) {
                arr[k] = leftArray[i];
                i++;
            } else {
                arr[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < leftArraySize) {
            arr[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < rightArraySize) {
            arr[k] = rightArray[j];
            j++;
            k++;
        }
    }
}
