package org.example.db;

import com.google.gson.Gson;
import org.example.db.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DB {
    private static final String dbFileName = "db.json";
    private List<User> userDB = new ArrayList<>();
    private List<Operation> operationDB = new ArrayList<>();
    private List<Category> categoriesDB = new ArrayList<>();


    //    Operations
    public Operation addOperation(NewOperation userOperation) {
        int id = 1;
        if (!operationDB.isEmpty()) {
            id = operationDB.get(operationDB.size() - 1).id + 1;
        }
        Operation newOperation = new Operation(id, userOperation.user, userOperation.category, userOperation.amount);
        operationDB.add(newOperation);
        saveToFile();
        return newOperation;
    }

    public void updateOperation(Operation userOperation) {
        for (Operation operation : operationDB) {
            if (Objects.equals(operation.id, userOperation.id)) {
                int indexToReplace = operationDB.indexOf(operation);
                operationDB.set(indexToReplace, userOperation);
            }
        }
        saveToFile();
    }

    public Operation getOperationByID(int operationID) {
        for (Operation operation : operationDB) {
            if (Objects.equals(operation.id, operationID)) return operation;
        }
        return null;
    }

    public boolean removeOperationByID(int operationID) {
        for (Operation operation : operationDB) {
            if (Objects.equals(operation.id, operationID)) {
                operationDB.remove(operation);
                saveToFile();
                return true;
            }
        }
        saveToFile();
        return false;
    }

    public ArrayList<Operation> getAllUserOperations(User user) {
        ArrayList<Operation> outList = new ArrayList<>();
        for (Operation link : operationDB) {
            if (Objects.equals(link.user, user)) {
                outList.add(link);
            }
        }
        return outList;
    }

    //    Users
    public User addUser(NewUser user) {
        int id = 1;
        if (!userDB.isEmpty()) {
            id = userDB.get(userDB.size() - 1).id + 1;
        }
        User newUser = new User(id, user.login, user.password, user.categories);
        userDB.add(newUser);
        saveToFile();
        return newUser;
    }

    public User authUser(String login, String password) {
        for (User user : userDB) {
            if (Objects.equals(user.login, login)) {
                if (user.checkByPassword(password)) {
                    return user;
                }
            }
        }
        return null;
    }

    public boolean checkLogin(String login) {
        for (User user : userDB) {
            if (Objects.equals(user.login, login)) {
                return true;
            }
        }
        return false;
    }

    public void updateUser(User curUser) {
        for (User user : userDB) {
            if (Objects.equals(user.id, curUser.id)) {
                int indexToReplace = userDB.indexOf(user);
                userDB.set(indexToReplace, curUser);
            }
        }
        saveToFile();
    }

    public boolean transferToUserByLogin(String login, float amount) {
        User curUser = null;
        for (User user : userDB) {
            if (Objects.equals(user.login, login)) {
                curUser = user;
                break;
            }
        }
        if (curUser == null) return false;
        Category transferInCategory = null;
        for (Category cat : curUser.categories) {
            if (Objects.equals(cat.name, "Входящие переводы") && cat.isProfit) {
                transferInCategory = cat;
                break;
            }
        }
        if (transferInCategory == null) {

            float newQuota = 0;
            int newId = 1;
            if (!curUser.categories.isEmpty()) {
                newId = curUser.categories.get(curUser.categories.size() - 1).id + 1;
            }
            Category newCategory = new Category(
                    newId,
                    "Входящие переводы",
                    newQuota,
                    true
            );
            curUser.categories.add(newCategory);
            updateUser(curUser);
        }
        NewOperation incomeOp = new NewOperation(curUser, transferInCategory, amount);
        addOperation(incomeOp);
        saveToFile();
        return true;

    }

    //    Categories
    public ArrayList<Category> getAllUserCategories(User user) {
        return user.categories;
    }


    //    Utils
    private void saveToFile() {
        try (Writer writer = new FileWriter(dbFileName)) {
            Gson gson = new Gson();
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Загрузка данных из JSON файла
    private static DB loadFromFile() {

        try {
            Reader reader = new FileReader(dbFileName);
            Gson gson = new Gson();
            return gson.fromJson(reader, DB.class);
        } catch (FileNotFoundException e) {
            return new DB();
        } catch (Exception e) {
            e.printStackTrace();
            return new DB();
        }
    }

    public static DB createDB() {
        return loadFromFile();
    }

}
