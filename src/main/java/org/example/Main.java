package org.example;

import org.example.db.*;
import org.example.db.models.NewUser;
import org.example.db.models.User;

import java.util.Objects;
import java.util.Scanner;

import static org.example.utils.BudgetCalculator.*;
import static org.example.utils.BudgetHandler.*;

public class Main {
    private static User currentUser = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DB db = DB.createDB();
        String outText = "";
        System.out.println("\n\n\n");
        if (Objects.equals(currentUser, null)) {
            outText = ("""
                    Добро пожаловать в систему управления личными финансами!
                    Отправьте короткую ссылку для перехода на исходный ресурс либо введите номер пункта меню для выбора действия:
                    
                    00 -> Войти в систему по логину и паролю
                    01 -> Зарегистрироваться в системе
                    # -> Завершить программу
                    """);
        } else outText = String.format("""
                %s, добро пожаловать в систему управления личными финансами!
                Отправьте короткую ссылку для перехода на исходный ресурс либо введите номер пункта меню для выбора действия:
                1 -> Добавить операцию
                2 -> Управлять категориями
                3 -> Вывести общую сумму доходов/расходов
                4 -> Отобразить детализацию по всем операциям
                5 -> Выполнить подсчёт по выбранным категориям
                6 -> Перевести деньги другому пользователю
                
                0 -> Выйти из системы
                # -> Завершить программу
                """, currentUser.login);
        System.out.println(outText);
        String userChoice = scanner.nextLine();

        switch (userChoice) {
            //выход
            case "0": {
                currentUser = null;
                System.out.println("Вы успешно вышли из системы!");
                break;
            }
            //вход
            case "00": {
                System.out.println("Введите ваш логин:");
                String curLogin = scanner.nextLine();
                if (db.checkLogin(curLogin)) {
                    System.out.println("Введите ваш пароль:");
                    String curPassword = scanner.nextLine();
                    User curUser = db.authUser(curLogin, curPassword);
                    if (curUser == null) {
                        System.out.println("Вход в систему не удался: неверный пароль");
                    } else {
                        currentUser = curUser;
                        System.out.println("Вы успешно вошли в систему!");
                    }
                } else {
                    System.out.println("Вход в систему не удался: логин не найден");
                }
                break;
            }
            //регистрация
            case "01": {
                System.out.println("Введите ваш логин:");
                String curLogin = scanner.nextLine();
                System.out.println("Введите ваш пароль:");
                String curPassword = scanner.nextLine();
                currentUser = db.addUser(new NewUser(curLogin, curPassword));
                System.out.println("Регистрация прошла успешно! Возврат в главное меню...\n");
                sleepDelay(500);
                break;
            }
            //Добавить операцию
            case "1": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                    sleepDelay(500);
                } else {
                    addOperation(db, currentUser);
                }
                break;
            }
            //Управлять категориями
            case "2": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                    sleepDelay(500);
                } else {
                    editCategoryGateway(db, currentUser);
                }
                break;
            }
            //Вывести общую сумму доходов/расходов
            case "3": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                    sleepDelay(500);
                } else {
                    printTotalBudgetAmount(db, currentUser);
                }
                break;
            }
            //Отобразить детализацию по всем операциям
            case "4": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                    sleepDelay(500);
                } else {
                    printDetailedBudgetAmount(db, currentUser);
                }
                break;
            }
            //Выполнить подсчёт по выбранным категориям
            case "5": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                    sleepDelay(500);
                } else {
                    printCalculationByCategory(db, currentUser);
                }
                break;
            }
            //Перевести деньги другому пользователю
            case "6": {
                if (currentUser == null) {
                    System.out.println("Для использования данной функции необходимо войти в систему.");
                    sleepDelay(500);
                } else {
                    transferMoney(db, currentUser);
                }
                break;
            }
            //завершение
            case "#": {
                System.out.flush();
                System.out.println("Завершение программы.");
                sleepDelay(500);
                System.out.flush();
                System.out.println("Завершение программы..");
                sleepDelay(500);
                System.out.flush();
                System.out.println("Завершение программы...");
                sleepDelay(500);
                System.out.println("Сохранение данных....");
                sleepDelay(1000);
                return;

            }
            //открытие ссылки
            default:
                System.out.println("Похоже, вы ввели неверную команду, попробуйте ещё раз.");
                break;
        }
        main(args);
    }

    private static void sleepDelay(int timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}