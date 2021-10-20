package server.utils;

import server.entities.Account;

import java.io.*;
import java.util.*;

/**
 * server.utils
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/14/2021 - 4:29 PM
 * @Description
 */
public class AccountManager {
    private final String ACCOUNT_FILE = "db_users.txt";
    private Map<String, Account> userAccounts;

    public AccountManager() {
        userAccounts = new HashMap<>();
        this.importAccountsFromFile(ACCOUNT_FILE);
    }

    public boolean addUser(String username, String password) {
        if (userAccounts.get(username) != null)
            return false;
        Account account = new Account(username, password);
        userAccounts.put(username, account);
        saveAccountToFile(ACCOUNT_FILE, account);
        return true;
    }

    public Account getUser(String username, String password) {
        Account user = userAccounts.get(username);
        if (user == null) return null;
        if (!user.getPassword().equals(password)) return null;
        return user;
    }

    private void importAccountsFromFile(String filename) {
        File file = new File(filename);
        try {
            if (!file.exists()) file.createNewFile();
            BufferedReader bufferedFile = new BufferedReader(new FileReader(filename));
            while (true) {
                String line = bufferedFile.readLine();
                if (line == null) break;
                Account account = new Account(line);
                userAccounts.put(account.getUsername(), account);
            }
            bufferedFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean saveAccountToFile(String filename, Account account) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            PrintWriter out = new PrintWriter(bw);
            out.println(account.toString());
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            return false;
        }
        return true;
    }

}
