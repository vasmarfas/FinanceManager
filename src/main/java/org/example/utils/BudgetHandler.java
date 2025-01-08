package org.example.utils;

import org.example.db.DB;
import org.example.db.models.Category;
import org.example.db.models.NewOperation;
import org.example.db.models.User;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class BudgetHandler {
    static Scanner scanner = new Scanner(System.in);

    public static User addOperation(DB db, User user) {
        ArrayList<Category> userCategories = db.getAllUserCategories(user);
        if (userCategories.isEmpty()) {
            System.out.println("Сначала вам нужно создать категорию операции");
        }
        System.out.println("""
                            Введите тип добавляемой операции:
                            1 -> Доход
                            2 -> Расход""");
        boolean isProfit = true;
        boolean isCategoryExists = false;
        ArrayList<Category> currentCategories = new ArrayList<>();
        String userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            for (Category category: userCategories) {
                if (category.isProfit) {
//                    isCategoryExists = true;
//                    break;
                    currentCategories.add(category);
                }
            }
        } else if (userInput.equals("2")) {
            isProfit = false;
            for (Category category: userCategories) {
                if (!category.isProfit) {
//                    isCategoryExists = true;
//                    break;
                    currentCategories.add(category);
                }
            }

        } else {
            System.out.println("Похоже, вы ввели неверную команду, попробуйте ещё раз.");
            return user;
        }
        if (currentCategories.isEmpty()) {
//        if (!isCategoryExists) {
            if (isProfit) {
                System.out.println("Сначала вам нужно создать категорию операции дохода");
                return user;
            } else {
                System.out.println("Сначала вам нужно создать категорию операции расхода");
                return user;
            }
        }
        System.out.println("Введите сумму операции:");
        String sumType = scanner.nextLine();
        float amount = 0;
        try {
            amount = Float.parseFloat(sumType);
        } catch (NumberFormatException e) {
            System.out.println("Похоже, вы ввели неверное число, попробуйте ещё раз.");
            return user;
        }
        System.out.println("Введите номер категории операции. Ваши категории:");
        for (Category category: currentCategories) {
            System.out.println(currentCategories.indexOf(category) + " -> " + category.name + ". Лимит категории: " + category.quota + ".\n");
        }
        try {
            Category curCategory = currentCategories.get(Integer.parseInt(scanner.nextLine()));
            db.addOperation(new NewOperation(user, curCategory, amount));
            System.out.println("Операция успешно добавлена!");
        } catch (Exception e) {
            System.out.println("Похоже, вы ввели неверное число, попробуйте ещё раз.");
            return user;
        }
        return user;
    }

    public static User editCategoryGateway(DB db, User user) {
        User refreshUser = user;
        System.out.println("""
                Введите, что вы хотите сделать:
                1 -> Добавить новую категорию
                2 -> Изменить бюджет на категорию""");
        switch (scanner.nextLine()) {
            case "1" : {
                refreshUser = addCategory(db, user);
                break;
            }
            case "2" : {
                refreshUser = editCategoryQuota(db, user);
                break;
            }
            default:
                System.out.println("Похоже, вы ввели неверное число, попробуйте ещё раз."); break;
        }
        return refreshUser;
    }

    private static User addCategory(DB db, User user) {
        System.out.println("Введите название новой категории:");
        String newName = scanner.nextLine();
        System.out.println("""
                Введите тип категории:
                1 -> Доходы
                2 -> Расходы""");
        boolean newIsProfit = false;
        String userIsProfit = scanner.nextLine();
        if (userIsProfit.equals("1")) newIsProfit = true;
        else if (userIsProfit.equals("2")) {}
        else {
            System.out.println("Похоже, вы ввели неверное значение, попробуйте ещё раз.");
            return user;
        }
        System.out.println("Введите бюджет на данную категорию:");
        float newQuota = 0;
        try {
            newQuota = Float.parseFloat(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Похоже, вы ввели неверное число, попробуйте ещё раз.");
            return user;
        }
        int newId = 1;
        if (!user.categories.isEmpty()) {
            newId = user.categories.get(user.categories.size()-1).id + 1;
        }
        Category newCategory = new Category(
                newId,
                newName,
                newQuota,
                newIsProfit
        );
        user.categories.add(newCategory);
        db.updateUser(user);
        System.out.println("Категория " + newCategory.name + " успешно добавлена!");
        return user;
    }
    private static User editCategoryQuota(DB db, User user) {
        ArrayList<Category> userCategories = db.getAllUserCategories(user);
        StringBuilder textCategories = new StringBuilder("Введите номер категории операции. Ваши категории:\n");
        for (Category category: userCategories) {
            textCategories.append(userCategories.indexOf(category)).append(" -> ").append(category.name).append(". Лимит категории: ").append(category.quota).append(".\n");
        }
        System.out.println(textCategories);
        try {
            Category curCategory = userCategories.get(Integer.parseInt(scanner.nextLine()));
            System.out.println("Текущий бюджет на категорию: " + curCategory.quota + ". \n" +
                    "Введите новое значение бюджета либо оставьте пустым для отмены изменений:");
            try {
                String userInput = scanner.nextLine();
                if (userInput.isEmpty()) {
                    System.out.println("Бюджет категории остался без изменений.");
                } else {
                    user.categories.get(userCategories.indexOf(curCategory)).quota = Float.parseFloat(userInput);
                    db.updateUser(user);
                    System.out.println("Бюджет на категорию " + curCategory.name + " успешно изменён. Новый бюджет: " + user.categories.get(userCategories.indexOf(curCategory)).quota);

                }
            } catch (Exception e) {
                System.out.println("Похоже, вы ввели неверное значение, попробуйте ещё раз.");
                return user;
            }
        } catch (Exception e) {
            System.out.println("Похоже, вы ввели неверное число, попробуйте ещё раз.");
            return user;
        }
        return user;
    }

    public static User transferMoney(DB db, User user) {
        Category transferOutCategory = null;
        float newQuota = 0;
        for (Category cat : user.categories) {
            if (Objects.equals(cat.name, "Исходящие переводы") && !cat.isProfit) {
                transferOutCategory = cat;
                break;
            }
        }
        if (transferOutCategory == null) {
            System.out.println("Будет создана категория расходов <Исходящие переводы>. Введите бюджет на данную категорию:");
            String userInput = scanner.nextLine();
            try {
                newQuota = Float.parseFloat(userInput);
                int newId = 1;
                if (!user.categories.isEmpty()) {
                    newId = user.categories.get(user.categories.size()-1).id + 1;
                }
                Category newCategory = new Category(
                        newId,
                        "Исходящие переводы",
                        newQuota,
                        false
                );
                user.categories.add(newCategory);
                db.updateUser(user);
            } catch (Exception e) {
                System.out.println("Похоже, вы ввели неверное значение, попробуйте ещё раз.");
            }
        }

        System.out.println("Введите логин получателя перевода:");
        String reseiverLogin = scanner.nextLine();
        if (db.checkLogin(reseiverLogin)) {
            System.out.println("Получатель найден, введите сумму для перевода:");
        } else {
            System.out.println("Получатель не найден, попробуйте снова.");
            return user;
        }
        String sumToSend = scanner.nextLine();
        float amountToSend = 0;
        try {
            if (Objects.equals(sumToSend, "0")) {
                System.out.println("Сумма перевода равна 0, операция не будет выполнена. Возврат в главное меню...");
                return user;
            } else {
                amountToSend = Float.parseFloat(sumToSend);
            }
        } catch (Exception e) {
            System.out.println("Похоже, вы ввели неверное значение, попробуйте ещё раз.");
            return user;
        }

        if (amountToSend == 0) {
            System.out.println("Похоже, вы ввели неверное значение, попробуйте ещё раз.");
        } else {
            boolean isSent = db.transferToUserByLogin(reseiverLogin, amountToSend);
            if (isSent) {
                System.out.println("Операция успешно выполнена! Отправлено " + amountToSend + " пользователю " + reseiverLogin);
            }
        }
        return user;
    }

}

