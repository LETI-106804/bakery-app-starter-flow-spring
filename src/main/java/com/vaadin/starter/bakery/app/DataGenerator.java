package com.vaadin.starter.bakery.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Customer;
import com.vaadin.starter.bakery.backend.data.entity.HistoryItem;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.OrderRepository;
import com.vaadin.starter.bakery.backend.repositories.PickupLocationRepository;
import com.vaadin.starter.bakery.backend.repositories.ProductRepository;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;

/**
 * Classe responsável por gerar dados de demonstração para a aplicação Bakery App Starter.
 * 
 * Este componente Spring inicializa os dados de exemplo, incluindo utilizadores, produtos, locais de recolha e encomendas.
 * Os dados gerados simulam o funcionamento da padaria, permitindo testes e demonstrações sem necessidade de entrada manual.
 * 
 * @author Vaadin
 */
@SpringComponent
public class DataGenerator implements HasLogger {

    /** Tipos de recheios de produtos. */
    private static final String[] FILLING = new String[] { "Strawberry", "Chocolate", "Blueberry", "Raspberry",
            "Vanilla" };
    /** Tipos de produtos disponíveis. */
    private static final String[] TYPE = new String[] { "Cake", "Pastry", "Tart", "Muffin", "Biscuit", "Bread", "Bagel",
            "Bun", "Brownie", "Cookie", "Cracker", "Cheese Cake" };
    /** Lista de nomes próprios para clientes. */
    private static final String[] FIRST_NAME = new String[] { "Ori", "Amanda", "Octavia", "Laurel", "Lael", "Delilah",
            "Jason", "Skyler", "Arsenio", "Haley", "Lionel", "Sylvia", "Jessica", "Lester", "Ferdinand", "Elaine",
            "Griffin", "Kerry", "Dominique" };
    /** Lista de apelidos para clientes. */
    private static final String[] LAST_NAME = new String[] { "Carter", "Castro", "Rich", "Irwin", "Moore", "Hendricks",
            "Huber", "Patton", "Wilkinson", "Thornton", "Nunez", "Macias", "Gallegos", "Blevins", "Mejia", "Pickett",
            "Whitney", "Farmer", "Henry", "Chen", "Macias", "Rowland", "Pierce", "Cortez", "Noble", "Howard", "Nixon",
            "Mcbride", "Leblanc", "Russell", "Carver", "Benton", "Maldonado", "Lyons" };

    /** Gerador de números aleatórios para dados simulados. */
    private final Random random = new Random(1L);

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private PickupLocationRepository pickupLocationRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * Construtor que injeta dependências dos repositórios e PasswordEncoder.
     * 
     * @param orderRepository repositório de encomendas
     * @param userRepository repositório de utilizadores
     * @param productRepository repositório de produtos
     * @param pickupLocationRepository repositório de locais de recolha
     * @param passwordEncoder codificador de passwords
     */
    @Autowired
    public DataGenerator(OrderRepository orderRepository, UserRepository userRepository,
            ProductRepository productRepository, PickupLocationRepository pickupLocationRepository,
            PasswordEncoder passwordEncoder) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.pickupLocationRepository = pickupLocationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método chamado após a construção do bean para gerar os dados de demonstração.
     * Se existirem utilizadores, os dados não são gerados novamente.
     */
    @PostConstruct
    public void loadData() {
        if (userRepository.count() != 0L) {
            getLogger().info("Using existing database");
            return;
        }

        getLogger().info("Generating demo data");

        getLogger().info("... generating users");
        User baker = createBaker(userRepository, passwordEncoder);
        User barista = createBarista(userRepository, passwordEncoder);
        createAdmin(userRepository, passwordEncoder);
        // Um conjunto de utilizadores sem restrições que podem ser eliminados
        createDeletableUsers(userRepository, passwordEncoder);

        getLogger().info("... generating products");
        // Conjunto de produtos para criar encomendas
        Supplier<Product> productSupplier = createProducts(productRepository, 8);
        // Produtos sem relações que podem ser eliminados
        createProducts(productRepository, 4);

        getLogger().info("... generating pickup locations");
        Supplier<PickupLocation> pickupLocationSupplier = createPickupLocations(pickupLocationRepository);

        getLogger().info("... generating orders");
        createOrders(orderRepository, productSupplier, pickupLocationSupplier, barista, baker);

        getLogger().info("Generated demo data");
    }

    /**
     * Preenche os dados de um cliente com nome, telefone e, ocasionalmente, detalhes especiais.
     * 
     * @param customer cliente a ser preenchido
     */
    private void fillCustomer(Customer customer) {
        String first = getRandom(FIRST_NAME);
        String last = getRandom(LAST_NAME);
        customer.setFullName(first + " " + last);
        customer.setPhoneNumber(getRandomPhone());
        if (random.nextInt(10) == 0) {
            customer.setDetails("Very important customer");
        }
    }

    /**
     * Gera um número de telefone aleatório no formato americano.
     * 
     * @return número de telefone gerado
     */
    private String getRandomPhone() {
        return "+1-555-" + String.format("%04d", random.nextInt(10000));
    }

    /**
     * Cria várias encomendas, distribuídas por dias e anos, simulando tendência de crescimento.
     * 
     * @param orderRepo repositório de encomendas
     * @param productSupplier fornecedor de produtos
     * @param pickupLocationSupplier fornecedor de locais de recolha
     * @param barista utilizador barista
     * @param baker utilizador padeiro
     */
    private void createOrders(OrderRepository orderRepo, Supplier<Product> productSupplier,
            Supplier<PickupLocation> pickupLocationSupplier, User barista, User baker) {
        int yearsToInclude = 2;
        LocalDate now = LocalDate.now();
        LocalDate oldestDate = LocalDate.of(now.getYear() - yearsToInclude, 1, 1);
        LocalDate newestDate = now.plusMonths(1L);

        // Cria a primeira encomenda do dia
        Order order = createOrder(productSupplier, pickupLocationSupplier, barista, baker, now);
        order.setDueTime(LocalTime.of(8, 0));
        order.setHistory(order.getHistory().subList(0, 1));
        order.setItems(order.getItems().subList(0, 1));
        orderRepo.save(order);

        for (LocalDate dueDate = oldestDate; dueDate.isBefore(newestDate); dueDate = dueDate.plusDays(1)) {
            int relativeYear = dueDate.getYear() - now.getYear() + yearsToInclude;
            int relativeMonth = relativeYear * 12 + dueDate.getMonthValue();
            double multiplier = 1.0 + 0.03 * relativeMonth;
            int ordersThisDay = (int) (random.nextInt(10) + 1 * multiplier);
            for (int i = 0; i < ordersThisDay; i++) {
                orderRepo.save(createOrder(productSupplier, pickupLocationSupplier, barista, baker, dueDate));
            }
        }
    }

    /**
     * Cria uma encomenda simulada para um dado dia, com produtos, cliente, histórico e estado.
     * 
     * @param productSupplier fornecedor de produtos
     * @param pickupLocationSupplier fornecedor de locais de recolha
     * @param barista utilizador barista
     * @param baker utilizador padeiro
     * @param dueDate data de entrega
     * @return encomenda criada
     */
    private Order createOrder(Supplier<Product> productSupplier, Supplier<PickupLocation> pickupLocationSupplier,
            User barista, User baker, LocalDate dueDate) {
        Order order = new Order(barista);

        fillCustomer(order.getCustomer());
        order.setPickupLocation(pickupLocationSupplier.get());
        order.setDueDate(dueDate);
        order.setDueTime(getRandomDueTime());
        order.changeState(barista, getRandomState(order.getDueDate()));

        int itemCount = random.nextInt(3);
        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i <= itemCount; i++) {
            OrderItem item = new OrderItem();
            Product product;
            do {
                product = productSupplier.get();
            } while (containsProduct(items, product));
            item.setProduct(product);
            item.setQuantity(random.nextInt(10) + 1);
            if (random.nextInt(5) == 0) {
                if (random.nextBoolean()) {
                    item.setComment("Lactose free");
                } else {
                    item.setComment("Gluten free");
                }
            }
            items.add(item);
        }
        order.setItems(items);

        order.setHistory(createOrderHistory(order, barista, baker));

        return order;
    }

    /**
     * Gera o histórico de estados para uma encomenda, simulando eventos típicos do fluxo.
     * 
     * @param order encomenda para a qual será gerado o histórico
     * @param barista utilizador barista
     * @param baker utilizador padeiro
     * @return lista de itens do histórico
     */
    private List<HistoryItem> createOrderHistory(Order order, User barista, User baker) {
        ArrayList<HistoryItem> history = new ArrayList<>();
        HistoryItem item = new HistoryItem(barista, "Order placed");
        item.setNewState(OrderState.NEW);
        LocalDateTime orderPlaced = order.getDueDate().minusDays(random.nextInt(5) + 2L).atTime(random.nextInt(10) + 7,
                00);
        item.setTimestamp(orderPlaced);
        history.add(item);
        if (order.getState() == OrderState.CANCELLED) {
            item = new HistoryItem(barista, "Order cancelled");
            item.setNewState(OrderState.CANCELLED);
            item.setTimestamp(orderPlaced.plusDays(random
                    .nextInt((int) orderPlaced.until(order.getDueDate().atTime(order.getDueTime()), ChronoUnit.DAYS))));
            history.add(item);
        } else if (order.getState() == OrderState.CONFIRMED || order.getState() == OrderState.DELIVERED
                || order.getState() == OrderState.PROBLEM || order.getState() == OrderState.READY) {
            item = new HistoryItem(baker, "Order confirmed");
            item.setNewState(OrderState.CONFIRMED);
            item.setTimestamp(orderPlaced.plusDays(random.nextInt(2)).plusHours(random.nextInt(5)));
            history.add(item);

            if (order.getState() == OrderState.PROBLEM) {
                item = new HistoryItem(baker, "Can't make it. Did not get any ingredients this morning");
                item.setNewState(OrderState.PROBLEM);
                item.setTimestamp(order.getDueDate().atTime(random.nextInt(4) + 4, 0));
                history.add(item);
            } else if (order.getState() == OrderState.READY || order.getState() == OrderState.DELIVERED) {
                item = new HistoryItem(baker, "Order ready for pickup");
                item.setNewState(OrderState.READY);
                item.setTimestamp(order.getDueDate().atTime(random.nextInt(2) + 8, random.nextBoolean() ? 0 : 30));
                history.add(item);
                if (order.getState() == OrderState.DELIVERED) {
                    item = new HistoryItem(baker, "Order delivered");
                    item.setNewState(OrderState.DELIVERED);
                    item.setTimestamp(order.getDueDate().atTime(order.getDueTime().minusMinutes(random.nextInt(120))));
                    history.add(item);
                }
            }
        }

        return history;
    }

    /**
     * Verifica se uma lista de itens já contém um determinado produto.
     * 
     * @param items lista de itens da encomenda
     * @param product produto a verificar
     * @return true se o produto já existe na lista, false caso contrário
     */
    private boolean containsProduct(List<OrderItem> items, Product product) {
        for (OrderItem item : items) {
            if (item.getProduct() == product) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gera uma hora de entrega aleatória entre 8h e 16h.
     * 
     * @return hora de entrega aleatória
     */
    private LocalTime getRandomDueTime() {
        int time = 8 + 4 * random.nextInt(3);

        return LocalTime.of(time, 0);
    }

    /**
     * Determina um estado aleatório para uma encomenda com base na sua data.
     * 
     * @param due data da encomenda
     * @return estado para a encomenda
     */
    private OrderState getRandomState(LocalDate due) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate twoDays = today.plusDays(2);

        if (due.isBefore(today)) {
            if (random.nextDouble() < 0.9) {
                return OrderState.DELIVERED;
            } else {
                return OrderState.CANCELLED;
            }
        } else {
            if (due.isAfter(twoDays)) {
                return OrderState.NEW;
            } else if (due.isAfter(tomorrow)) {
                double resolution = random.nextDouble();
                if (resolution < 0.8) {
                    return OrderState.NEW;
                } else if (resolution < 0.9) {
                    return OrderState.PROBLEM;
                } else {
                    return OrderState.CANCELLED;
                }
            } else {
                double resolution = random.nextDouble();
                if (resolution < 0.6) {
                    return OrderState.READY;
                } else if (resolution < 0.8) {
                    return OrderState.DELIVERED;
                } else if (resolution < 0.9) {
                    return OrderState.PROBLEM;
                } else {
                    return OrderState.CANCELLED;
                }
            }

        }
    }

    /**
     * Obtém um elemento aleatório de um array.
     * 
     * @param array array de elementos
     * @param <T> tipo dos elementos
     * @return elemento escolhido aleatoriamente
     */
    private <T> T getRandom(T[] array) {
        return array[random.nextInt(array.length)];
    }

    /**
     * Cria e guarda locais de recolha e devolve um fornecedor aleatório.
     * 
     * @param pickupLocationRepository repositório de locais de recolha
     * @return fornecedor de local de recolha aleatório
     */
    private Supplier<PickupLocation> createPickupLocations(PickupLocationRepository pickupLocationRepository) {
        List<PickupLocation> pickupLocations = Arrays.asList(
                pickupLocationRepository.save(createPickupLocation("Store")),
                pickupLocationRepository.save(createPickupLocation("Bakery")));
        return () -> pickupLocations.get(random.nextInt(pickupLocations.size()));
    }

    /**
     * Cria um local de recolha com o nome especificado.
     * 
     * @param name nome do local
     * @return instância de PickupLocation
     */
    private PickupLocation createPickupLocation(String name) {
        PickupLocation store = new PickupLocation();
        store.setName(name);
        return store;
    }

    /**
     * Cria produtos simulados e devolve um fornecedor aleatório.
     * 
     * @param productsRepo repositório de produtos
     * @param numberOfItems número de produtos a criar
     * @return fornecedor de produto aleatório
     */
    private Supplier<Product> createProducts(ProductRepository productsRepo, int numberOfItems) {
        List<Product> products  = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++) {
            Product product = new Product();
            product.setName(getRandomProductName());
            double doublePrice = 2.0 + random.nextDouble() * 100.0;
            product.setPrice((int) (doublePrice * 100.0));
            products.add(productsRepo.save(product));
        }
        return () -> {
            double cutoff = 2.5;
            double g = random.nextGaussian();
            g = Math.min(cutoff, g);
            g = Math.max(-cutoff, g);
            g += cutoff;
            g /= (cutoff * 2.0);
            return products.get((int) (g * (products.size() - 1)));
        };
    }

    /**
     * Gera um nome de produto aleatório, combinando recheios e tipo.
     * 
     * @return nome do produto
     */
    private String getRandomProductName() {
        String firstFilling = getRandom(FILLING);
        String name;
        if (random.nextBoolean()) {
            String secondFilling;
            do {
                secondFilling = getRandom(FILLING);
            } while (secondFilling.equals(firstFilling));

            name = firstFilling + " " + secondFilling;
        } else {
            name = firstFilling;
        }
        name += " " + getRandom(TYPE);

        return name;
    }

    /**
     * Cria e guarda um utilizador com perfil de padeiro (BAKER).
     * 
     * @param userRepository repositório de utilizadores
     * @param passwordEncoder codificador de password
     * @return utilizador criado
     */
    private User createBaker(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return userRepository.save(
                createUser("baker@vaadin.com", "Heidi", "Carter", passwordEncoder.encode("baker"), Role.BAKER, false));
    }

    /**
     * Cria e guarda um utilizador com perfil de barista (BARISTA).
     * 
     * @param userRepository repositório de utilizadores
     * @param passwordEncoder codificador de password
     * @return utilizador criado
     */
    private User createBarista(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return userRepository.save(createUser("barista@vaadin.com", "Malin", "Castro",
                passwordEncoder.encode("barista"), Role.BARISTA, true));
    }

    /**
     * Cria e guarda um utilizador com perfil de administrador (ADMIN).
     * 
     * @param userRepository repositório de utilizadores
     * @param passwordEncoder codificador de password
     * @return utilizador criado
     */
    private User createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return userRepository.save(
                createUser("admin@vaadin.com", "Göran", "Rich", passwordEncoder.encode("admin"), Role.ADMIN, true));
    }

    /**
     * Cria utilizadores extra que podem ser eliminados para testes.
     * 
     * @param userRepository repositório de utilizadores
     * @param passwordEncoder codificador de password
     */
    private void createDeletableUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        userRepository.save(
                createUser("peter@vaadin.com", "Peter", "Bush", passwordEncoder.encode("peter"), Role.BARISTA, false));
        userRepository
                .save(createUser("mary@vaadin.com", "Mary", "Ocon", passwordEncoder.encode("mary"), Role.BAKER, true));
    }

    /**
     * Cria uma instância de utilizador com dados e perfil especificados.
     * 
     * @param email email do utilizador
     * @param firstName primeiro nome
     * @param lastName apelido
     * @param passwordHash password codificada
     * @param role perfil do utilizador
     * @param locked se o utilizador está bloqueado
     * @return utilizador criado
     */
    private User createUser(String email, String firstName, String lastName, String passwordHash, String role,
            boolean locked) {
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPasswordHash(passwordHash);
        user.setRole(role);
        user.setLocked(locked);
        return user;
    }
}
