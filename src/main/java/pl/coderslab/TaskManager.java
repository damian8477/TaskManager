package pl.coderslab;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TaskManager {

    private static final Scanner scan = new Scanner(System.in);
    private static final Path path = Paths.get("tasks.csv");
    private static final String[] menuTab = {"add", "remove", "list", "exit"};
    private static String[][] tasks;

    public static void main(String[] args) {
        getList();
        presentMenu();
        menu();
    }

    private static void menu() {
        boolean isContinue = true;
        while (isContinue) {
            String command = scan.nextLine().trim();
            switch (command) {
                case "add":
                    add();
                    break;
                case "remove":
                    remove();
                    break;
                case "list":
                    printList();
                    break;
                case "exit":
                    System.out.println(ConsoleColors.RED_BOLD + "Bye bye bro");
                    isContinue = false;
                    break;
                default:
                    if (!command.equals("")) {
                        System.out.println(ConsoleColors.RED_BOLD + "Please, read menu");
                        presentMenu();
                    }
            }
        }
    }

    private static void remove() {
        System.out.println("Please select number to remove");
        try {
            int id = scan.nextInt();
            removeFromTask(id);
            presentMenu();
        } catch (InputMismatchException e) {
            System.out.println("Incorrect argument passed. Please give number greater or equals 0");
        }

    }

    private static void removeFromTask(int id) {
        try {
            tasks = ArrayUtils.remove(tasks, id);
            updateCSV();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Incorrect argument passed. Please give number greater or equals 0");
        }

    }

    private static void add() {
        System.out.println("Please add task description");
        String taskDescription = scan.nextLine();
        System.out.println("Please add task due date");
        String date = scan.nextLine();
        System.out.println("Is your task is important: true/false");
        boolean isImportant;
        try {
            isImportant = getBoolean(scan.nextLine());
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
            return;
        }
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[]{taskDescription, date, String.valueOf(isImportant)};
        updateCSV();
        presentMenu();
    }

    private static boolean getBoolean(String str){
        if(str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")){
            return true;
        }
        throw new InputMismatchException("Bad value, enter true or false");
    }

    private static void updateCSV() {
        writeToFile("", StandardOpenOption.TRUNCATE_EXISTING);
        for (String[] task : tasks) {
            String str = StringUtils.join(task, ", ");
            writeToFile(str + "\n", StandardOpenOption.APPEND);
        }
    }

    private static void writeToFile(String str, StandardOpenOption standardOpenOption) {
        try {
            Files.writeString(path, str, standardOpenOption);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void getList() {
        tasks = new String[0][3];
        try {
            for (String s : Files.readAllLines(path)) {
                String[] nextTab = s.trim().split(",");
                tasks = Arrays.copyOf(tasks, tasks.length + 1);
                tasks[tasks.length - 1] = nextTab;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not exist");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void printList() {
        for (int i = 0; i < tasks.length; i++) {
            System.out.println(i + ": " + StringUtils.join(tasks[i], " "));
        }
        presentMenu();
    }


    private static void presentMenu() {
        System.out.println(ConsoleColors.BLUE_BOLD + "\nPlease select the option: ");
        for (String s : menuTab) {
            System.out.println(ConsoleColors.WHITE + s);
        }
    }


}
