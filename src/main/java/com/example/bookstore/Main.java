package com.example.bookstore;

import com.example.bookstore.dao.UserDAO;
import com.example.bookstore.dao.UserDAOImpl;
import com.example.bookstore.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Main {

    private static final UserDAO userDAO = new UserDAOImpl();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String option = scanner.nextLine().trim();
            try {
                switch (option) {
                    case "1" -> createUser(scanner);
                    case "2" -> findUserById(scanner);
                    case "3" -> listUsers();
                    case "4" -> updateUser(scanner);
                    case "5" -> deleteUser(scanner);
                    case "0" -> running = false;
                    default -> System.out.println("Opcion invalida");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("""

                === CRUD Usuarios ===
                1. Crear usuario
                2. Buscar usuario por id
                3. Listar usuarios
                4. Actualizar usuario
                5. Eliminar usuario
                0. Salir
                Seleccione una opcion:""");
    }

    private static void createUser(Scanner scanner) throws SQLException {
        System.out.print("name: ");
        String name = scanner.nextLine().trim();
        System.out.print("email: ");
        String email = scanner.nextLine().trim();
        System.out.print("password: ");
        String password = scanner.nextLine().trim();
        System.out.print("role (ADMIN/EMPLOYEE): ");
        User.Role role = User.Role.valueOf(scanner.nextLine().trim().toUpperCase());
        System.out.print("is_active (true/false): ");
        boolean active = Boolean.parseBoolean(scanner.nextLine().trim());

        User created = userDAO.create(new User(name, email, password, role, active));
        System.out.println("Usuario creado: " + created);
    }

    private static void findUserById(Scanner scanner) throws SQLException {
        System.out.print("id_user: ");
        long id = Long.parseLong(scanner.nextLine().trim());
        Optional<User> user = userDAO.findById(id);
        System.out.println(user.isPresent() ? user.get() : "No encontrado");
    }

    private static void listUsers() throws SQLException {
        List<User> users = userDAO.findAll();
        if (users.isEmpty()) {
            System.out.println("No hay usuarios registrados");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser(Scanner scanner) throws SQLException {
        System.out.print("id_user a actualizar: ");
        long id = Long.parseLong(scanner.nextLine().trim());
        Optional<User> existing = userDAO.findById(id);
        if (existing.isEmpty()) {
            System.out.println("No encontrado");
            return;
        }

        User user = existing.get();
        System.out.print("name [" + user.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            user.setName(name);
        }
        System.out.print("email [" + user.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }
        System.out.print("password: ");
        String password = scanner.nextLine().trim();
        if (!password.isEmpty()) {
            user.setPassword(password);
        }
        System.out.print("role [" + user.getRole() + "]: ");
        String role = scanner.nextLine().trim();
        if (!role.isEmpty()) {
            user.setRole(User.Role.valueOf(role.toUpperCase()));
        }
        System.out.print("is_active [" + user.isActive() + "]: ");
        String active = scanner.nextLine().trim();
        if (!active.isEmpty()) {
            user.setActive(Boolean.parseBoolean(active));
        }

        boolean updated = userDAO.update(user);
        System.out.println(updated ? "Usuario actualizado" : "No se pudo actualizar");
    }

    private static void deleteUser(Scanner scanner) throws SQLException {
        System.out.print("id_user a eliminar: ");
        long id = Long.parseLong(scanner.nextLine().trim());
        boolean deleted = userDAO.delete(id);
        System.out.println(deleted ? "Usuario eliminado" : "No se pudo eliminar");
    }
}
