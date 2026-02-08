package org.example;

import org.example.entity.Command;
import org.example.service.CommandService;
import org.example.service.DbService;
import org.example.service.IDbservice;
import org.example.exception.InvalidCommandException;
import org.example.exception.InvalidKeyException;
import org.example.exception.DbUnavaiableException;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CommandService commandService = new CommandService();
        IDbservice dbService = new DbService();

        System.out.println("In-memory DB started");
        System.out.println("Commands: PUT key value [ttl], GET key, DELETE key, START, STOP, SHOW, EXIT");

        while (true) {
            try {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) continue;

                Command command = commandService.parse(input);

                switch (command.type) {

                    case PUT:
                        if (command.ttl != -1) {
                            dbService.put(command.key, command.rawValue, command.ttl);
                        } else {
                            dbService.put(command.key, command.rawValue);
                        }
                        System.out.println("OK");
                        break;

                    case GET:
                        System.out.println(dbService.get(command.key));
                        break;

                    case DELETE:
                        dbService.delete(command.key);
                        System.out.println("OK");
                        break;

                    case START:
                        dbService.start();
                        System.out.println("DB started");
                        break;

                    case STOP:
                        dbService.stop();
                        System.out.println("DB stopped");
                        break;

                    case SHOW:
                        dbService.showDB();
                        break;

                    case EXIT:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Unknown command");
                }

            } catch (InvalidCommandException | InvalidKeyException | DbUnavaiableException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }
}
