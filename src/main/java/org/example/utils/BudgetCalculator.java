package org.example.utils;

import org.example.db.DB;
import org.example.db.models.Category;
import org.example.db.models.NewOperation;
import org.example.db.models.Operation;
import org.example.db.models.User;

import java.util.*;

public class BudgetCalculator {
    static Scanner scanner = new Scanner(System.in);
    public static void printTotalBudgetAmount(DB db, User user) {
        float profit = 0;
        float notProfit = 0;
        for (Operation operation : db.getAllUserOperations(user)) {
            if (operation.category.isProfit) {
                profit += operation.amount;
            } else {
                notProfit += operation.amount;
            }
        }
        System.out.println("Анализ вашего бюджета:\n" +
                "Доходы:" + profit +
                "\nРасходы: "+ notProfit +
                "\nОстаток: " + (profit - notProfit));

    }
    public static void printDetailedBudgetAmount(DB db, User user) {
        float profit = 0;
        float notProfit = 0;
        HashMap<Category, Float> profitCategories = new HashMap<>();
        HashMap<Category, Float> nonProfitCategories = new HashMap<>();
        for (Operation operation : db.getAllUserOperations(user)) {
            if (operation.category.isProfit) {
                profit += operation.amount;
                Float curOpAmount = profitCategories.get(operation.category);
                if (curOpAmount == null) {
                    profitCategories.put(operation.category, operation.amount);
                } else {
                    curOpAmount += operation.amount;
                    profitCategories.put(operation.category, curOpAmount);
                }

            } else {
                notProfit += operation.amount;
                Float curOpAmount = nonProfitCategories.get(operation.category);
                if (curOpAmount == null) {
                    nonProfitCategories.put(operation.category, operation.amount);
                } else {
                    curOpAmount += operation.amount;
                    nonProfitCategories.put(operation.category, curOpAmount);
                }

            }
        }
        StringBuilder profitString = new StringBuilder();
        StringBuilder nonProfitString = new StringBuilder();

        for (Map.Entry<Category, Float> entry : profitCategories.entrySet()) {
            profitString.append(entry.getKey().name).append(": ").append(entry.getValue()).append("\n");
        }
        for (Map.Entry<Category, Float> entry : nonProfitCategories.entrySet()) {
            nonProfitString.append(entry.getKey().name).append(": ").append(entry.getValue()).append(", Оставшийся бюджет: ").append(entry.getKey().quota-entry.getValue()).append("\n");
        }

        System.out.println("Анализ вашего бюджета:\n" +
                "Общий доход:" + profit +
                "\nДоходы по категориям: \n" + profitString +
                "\n\nОбщие расходы: "+ notProfit +
                "\nБюджет по категориям: \n" + nonProfitString +
                "\n\nОстаток: " + (profit - notProfit));




    }
    public static void printCalculationByCategory(DB db, User user) {
        System.out.println("Введите номер интересующей вас категории (при выборе нескольких - перечислите через запятую):");
        for (Category category: user.categories) {
            System.out.println(user.categories.indexOf(category) + " -> " + category.name + ". Лимит категории: " + category.quota + ".\n");
        }
        String userInput = scanner.nextLine();
        ArrayList<Category> selectedCategories = new ArrayList<>();
        String[] selectedCategoriesString = userInput.replace(" ", "").split(",");
        try {
            for (String str: selectedCategoriesString) {
                selectedCategories.add(user.categories.get(Integer.parseInt(str)));
            }
        } catch (Exception e) {
            System.out.println("Похоже, вы ввели неверное число, попробуйте ещё раз.");
            return;
        }
        ArrayList<Integer> selectedCategoriesID = new ArrayList<>();
        for (Category cat: selectedCategories) {
            selectedCategoriesID.add(cat.id);
        }
        float profit = 0;
        float notProfit = 0;
        HashMap<Category, Float> profitCategories = new HashMap<>();
        HashMap<Category, Float> nonProfitCategories = new HashMap<>();
        for (Operation operation : db.getAllUserOperations(user)) {
            if (selectedCategoriesID.contains(operation.category.id)) {
                if (operation.category.isProfit) {
                    profit += operation.amount;
                    Float curOpAmount = profitCategories.get(operation.category);
                    if (curOpAmount == null) {
                        profitCategories.put(operation.category, operation.amount);
                    } else {
                        curOpAmount += operation.amount;
                        profitCategories.put(operation.category, curOpAmount);
                    }

                } else {
                    notProfit += operation.amount;
                    Float curOpAmount = nonProfitCategories.get(operation.category);
                    if (curOpAmount == null) {
                        nonProfitCategories.put(operation.category, operation.amount);
                    } else {
                        curOpAmount += operation.amount;
                        nonProfitCategories.put(operation.category, curOpAmount);
                    }

                }
            }
        }
        StringBuilder profitString = new StringBuilder();
        StringBuilder nonProfitString = new StringBuilder();

        for (Map.Entry<Category, Float> entry : profitCategories.entrySet()) {
            profitString.append(entry.getKey().name).append(": ").append(entry.getValue()).append("\n");
        }
        for (Map.Entry<Category, Float> entry : nonProfitCategories.entrySet()) {
            nonProfitString.append(entry.getKey().name).append(": ").append(entry.getValue()).append(", Оставшийся бюджет: ").append(entry.getKey().quota-entry.getValue()).append("\n");
        }
        if (profit!= 0 && notProfit != 0) {
            System.out.println("Анализ вашего бюджета по выбранным категориям:\n" +
                    "Общий доход по выбранным категориям:" + profit +
                    "\nДоходы по категориям: \n" + profitString +
                    "\n\nОбщие расходы по выбранным категориям: "+ notProfit +
                    "\nБюджет по категориям: \n" + nonProfitString +
                    "\n\nОстаток: " + (profit - notProfit));
        } else if (profit!=0) {
            System.out.println("Анализ вашего бюджета по выбранным категориям:\n" +
                    "Общий доход по выбранным категориям:" + profit +
                    "\nДоходы по категориям: \n" + profitString);
        } else if (notProfit != 0) {
            System.out.println("Анализ вашего бюджета по выбранным категориям:\n" +
                    "\nОбщие расходы по выбранным категориям: "+ notProfit +
                    "\nБюджет по категориям: \n" + nonProfitString);

        } else {
            System.out.println("Не найдено операций по выбранным категориям");
        }

    }


}

