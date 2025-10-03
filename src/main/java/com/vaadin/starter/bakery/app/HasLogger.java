package com.vaadin.starter.bakery.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface funcional que fornece capacidade de logging para classes que a implementam.
 * <p>
 * Útil especialmente em ambientes onde a serialização é necessária, pois evita o uso de campos estáticos.
 * Ao implementar esta interface, a classe pode obter facilmente um logger SLF4J associado à sua própria classe.
 */
public interface HasLogger {

    /**
     * Retorna uma instância de {@link Logger} associada à classe que implementa esta interface.
     * <p>
     * O logger é obtido dinamicamente com base na classe em tempo de execução, permitindo logging contextualizado.
     *
     * @return um logger SLF4J para a classe atual
     */
    default Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }
}
