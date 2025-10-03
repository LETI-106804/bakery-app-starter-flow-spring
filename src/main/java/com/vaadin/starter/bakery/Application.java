package com.vaadin.starter.bakery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.vaadin.starter.bakery.app.security.SecurityConfiguration;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.MainView;

/**
 * <p>
 * Classe principal da aplicação web Bakery Starter baseada em Spring Boot.
 * Responsável por inicializar a aplicação, configurar pacotes para scan,
 * entidades, repositórios JPA e integração com o Vaadin.
 * </p>
 *
 * <p>
 * Anotações utilizadas:
 * <ul>
 *   <li>{@link SpringBootApplication} - Define a configuração principal da aplicação Spring Boot,
 *       especificando pacotes relevantes para scan e excluindo a configuração de erro padrão do MVC.</li>
 *   <li>{@link EnableJpaRepositories} - Habilita repositórios JPA no pacote do {@link UserRepository}.</li>
 *   <li>{@link EntityScan} - Habilita o scan de entidades JPA no pacote do {@link User}.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Esta classe também estende {@link SpringBootServletInitializer}, permitindo
 * a implantação da aplicação em servidores servlet tradicionais (WAR).
 * </p>
 *
 * <p>
 * Métodos principais:
 * <ul>
 *   <li>{@link #main(String[])} - Método de entrada padrão para executar a aplicação via linha de comando.</li>
 *   <li>{@link #configure(SpringApplicationBuilder)} - Configuração para implantação como WAR.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Dependências principais:
 * <ul>
 *   <li>{@link SecurityConfiguration} - Configuração de segurança da aplicação.</li>
 *   <li>{@link MainView} - Vista principal da aplicação Vaadin.</li>
 *   <li>{@link UserService} - Serviço para gestão de utilizadores.</li>
 * </ul>
 * </p>
 *
 * @author LETI-106804
 * @since 1.0
 */
@SpringBootApplication(scanBasePackageClasses = { SecurityConfiguration.class, MainView.class, Application.class,
		UserService.class }, exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = { UserRepository.class })
@EntityScan(basePackageClasses = { User.class })
public class Application extends SpringBootServletInitializer {

	/**
	 * Método principal para inicializar a aplicação Spring Boot.
	 *
	 * @param args argumentos de linha de comando
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Configuração do {@link SpringApplicationBuilder} para execução em servidor servlet tradicional (WAR).
	 *
	 * @param application o construtor da aplicação
	 * @return configuração da aplicação com a classe {@link Application} como fonte
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
