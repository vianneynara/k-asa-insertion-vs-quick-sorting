package asa;

// Class file to compare the performance of Insertion Sort and Quick Sort for
// "Algoritma dan Struktur Data" class.
// Repository owner: vianneynara

import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Main class to compare the performance of Insertion Sort and Quick Sort.
 * <p>
 * How to use:
 * 1. Run the main method with {@code makeData()} method once.
 * 2. Uncomment ONE statement of array reading for the test.
 * 3. Run the main method again.
 * 4. Take the average of the time it took to sort the array, put it into spreadsheets.
 *
 * @author vianneynara
 * */
public class InsertionSortVsQuickSort {
	private static int testCount = 0;
	private static ArrayList<Double> insertionSortTime = new ArrayList<>();
	private static ArrayList<Double> quickSortTime = new ArrayList<>();

	public static void main(String[] args) {
		/* Creates 5 .bjir files with random integers for testing */
//		makeData();

		/* Uncomment ONE statement of array reading for the test */
//		int[] a = readData("generated-100.bjir");
//		int[] a = readData("generated-1000.bjir");
		int[] a = readData("generated-10000.bjir");
//		int[] a = readData("generated-100000.bjir");
//		int[] a = readData("generated-1000000.bjir");

		/* Testing insertion vs quicksort */
		// To make it fair, please run this 5 times and take the average
		// of the time it took to sort the array.
		testInsertionVsQuickSort(a);
		testInsertionVsQuickSort(a);
		testInsertionVsQuickSort(a);
		testInsertionVsQuickSort(a);
		testInsertionVsQuickSort(a);

		System.out.println("=".repeat(80));
		/* Print average time difference */
		System.out.println("Average execution time for InsertionSort	: "
			+ insertionSortTime.stream().mapToDouble(Double::doubleValue).average().orElse(0) + " nanoseconds");
		System.out.println("Average execution time for QuickSort		: "
			+ quickSortTime.stream().mapToDouble(Double::doubleValue).average().orElse(0) + " nanoseconds");
	}

	/**
	 * This method simplify the testing process.
	 * */
	private static void testInsertionVsQuickSort(int[] arr) {
		System.out.println("/".repeat(80));
		System.out.println("Test [" + (++testCount) + "] with " + arr.length + " integers\n");
		{
			int[] arrCopy = arr.clone();
			double start = System.nanoTime();
			InsertionSort.ascending(arrCopy);
			double end = System.nanoTime();
			insertionSortTime.add(end - start);
			System.out.print("InsertionSort -> ");
			System.out.printf("Sorted in %,.2f Nano Seconds (%s)\n",
				end - start, formattedTimeDiff(start, end));
		}

		{
			int[] arrCopy = arr.clone();
			double start = System.nanoTime();
			QuickSort.ascending(arrCopy);
			double end = System.nanoTime();
			quickSortTime.add(end - start);
			System.out.print("QuickSort -> ");
			System.out.printf("Sorted in %,.2f Nano Seconds (%s)\n",
				end - start, formattedTimeDiff(start, end));
		}
		System.out.println();
	}

	/**
	 * To format the time difference into a more readable time format.
	 * */
	private static String formattedTimeDiff(double timeStart, double timeEnd) {
		double totalDetik = ((timeEnd - timeStart) * 0.000_000_001);
		int jam = (int) totalDetik / 3600,
			menit = (int) (totalDetik % 3600) / 60,
			detik = (int) totalDetik % 60;
		return String.format("%02d jam, %02d menit, %02d detik", jam, menit, detik);
	}

	/**
	 * Run once.
	 */
	private static void makeData() {
		DataGeneratorHandler.generateAndSave(100, 1000);
		DataGeneratorHandler.generateAndSave(1000, 1000);
		DataGeneratorHandler.generateAndSave(10000, 1000);
		DataGeneratorHandler.generateAndSave(100000, 1000);
		DataGeneratorHandler.generateAndSave(1000000, 1000);
	}

	/**
	 * Wrapper for reading data from file.
	 * */
	private static int[] readData(String filename) {
		try {
			return DataGeneratorHandler.readFromFile(filename);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}

/**
 * Class to handle data generation and reading from file.
 * */
class DataGeneratorHandler {
	/**
	 * Generate random integers and save to file with ".bjir" extension.
	 *
	 * @param size the size of the array to be generated
	 * @param range the range of the random integers
	 * */
	public static void generateAndSave(int size, int range) {
		int[] arr = generateInts(size, range);
		String filename = "src/asa/generated-" + size + ".bjir";

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
			out.writeObject(arr);
			System.out.println("Generated " + size + " random integers and saved to " + filename
				+ "; Saved to the same path as this file btw.");
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}

	/**
	 * Reads the array from the ".bjir" file.
	 *
	 * @param filename the filename to be read including the extension ".bjir"
	 * @return the array of integers
	 * @throws FileNotFoundException if the file is not found
	 * */
	public static int[] readFromFile(String filename) throws FileNotFoundException {
		// read from the passed filename value in this same directory and return the array
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("src/asa/" + filename))) {
			int[] arr = (int[]) in.readObject();
			System.out.println("Read " + arr.length + " integers from " + filename);
			return arr;
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Generate random integers.
	 *
	 * @return the array of random integers
	 * */
	private static int[] generateInts(int size, int range) {
		int[] arr = new int[size];
		for (int i = 0; i < size; i++) {
			arr[i] = (int) (Math.random() * range) + 1;
		}
		return arr;
	}
}

/**
 * Stores the insertion sort algorithm.
 * */
class InsertionSort {
	/**
	 * Sort the array in ascending order.
	 *
	 * @param arr the array to be sorted
	 */
	public static void ascending(int[] arr) {
		for (int i = 0; i <= arr.length - 1; i++) {
			int current = arr[i];
			int j = i - 1;
			while (j >= 0 && arr[j] > current) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = current;
		}
	}

	/**
	 * Sort the array in descending order.
	 *
	 * @param arr the array to be sorted
	 * */
	public static void descending(int[] arr) {
		for (int i = 0; i <= arr.length - 1; i++) {
			int current = arr[i];
			int j = i - 1;
			while (j >= 0 && arr[j] < current) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = current;
		}
	}
}

class QuickSort {
	/* Ascending and descending wrappers */
	public static void ascending(int[] arr) {
		quickSortAsc(arr, 0, arr.length - 1);
	}

	public static void descending(int[] arr) {
		quickSortDesc(arr, 0, arr.length - 1);
	}

	/* The underlying algorithms */

	private static void quickSortAsc(int[] arr, int low, int high) {
		if (low < high) {
			int pi = partitionAsc(arr, low, high);

			quickSortAsc(arr, low, pi - 1);
			quickSortAsc(arr, pi + 1, high);
		}
	}

	private static int partitionAsc(int[] arr, int low, int high) {
		int pivot = arr[high];
		int i = (low - 1);

		for (int j = low; j < high; j++) {
			if (arr[j] <= pivot) {
				i++;

				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}

		int temp = arr[i + 1];
		arr[i + 1] = arr[high];
		arr[high] = temp;

		return i + 1;
	}

	private static void quickSortDesc(int[] arr, int low, int high) {
		if (low < high) {
			int pi = partitionDesc(arr, low, high);

			quickSortDesc(arr, low, pi - 1);
			quickSortDesc(arr, pi + 1, high);
		}
	}

	private static int partitionDesc(int[] arr, int low, int high) {
		int pivot = arr[high];
		int i = (low - 1);

		for (int j = low; j < high; j++) {
			if (arr[j] >= pivot) {
				i++;

				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}

		int temp = arr[i + 1];
		arr[i + 1] = arr[high];
		arr[high] = temp;

		return i + 1;
	}
}
